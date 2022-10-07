package com.strategy.strategymaker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.dbmanager.commonlibraries.DBService;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.wrapper.CatchEventWrapper;
import com.scriptparser.parserdatastructure.wrapper.GroupWrapper;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import com.scriptparser.parserdatastructure.wrapper.VariableWrapper;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import com.strategy.strategymaker.additionalinfo.AdditionalInfo;
import com.strategy.strategymaker.additionalinfo.CustomVariableInfo;

public class VariableInfoMaker {
    private static Map<String, VariableTypeWrapper> variableStore = new HashMap<>();

    public static String makeKey(ServiceWrapper service, String variable) {
        return service.getService().getName() + "_" + variable;
    }

    public static String makeKey(ServiceWrapper service, VariableWrapper variable) {
        return makeKey(service, variable.getName());
    }

    public static void makeVariableInfoList(MissionWrapper mission, AdditionalInfo additionalInfo,
            List<RobotImplWrapper> robotList) {
        try {
            for (RobotImplWrapper robot : robotList) {
                String team = robot.getGroupList().get(0);
                TransitionWrapper mainTransition = mission.getTransition(team);
                traverseTransition(mainTransition, robot, additionalInfo, mission);
            }
            for (RobotImplWrapper robot : robotList) {
                String team = robot.getGroupList().get(0);
                TransitionWrapper mainTransition = mission.getTransition(team);
                traverseTransition(mainTransition, robot, additionalInfo, mission);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, VariableTypeWrapper> traverseTransition(TransitionWrapper transition,
            RobotImplWrapper robot, AdditionalInfo additionalInfo, MissionWrapper mission)
            throws Exception {
        Map<String, VariableTypeWrapper> variableMap = new HashMap<>();
        ModeWrapper defaultMode = transition.getDefaultMode().getMode();
        variableMap.putAll(visitMode(defaultMode, robot, additionalInfo, mission));
        for (List<CatchEventWrapper> ceList : transition.getTransitionMap().values()) {
            for (CatchEventWrapper ce : ceList) {
                variableMap.putAll(visitMode(

                        ce.getMode().getMode(), robot, additionalInfo, mission));
            }
        }
        return variableMap;
    }

    private static void getVariableListOfAction(RobotImplWrapper robot, ServiceWrapper service,
            StatementWrapper statement, Map<String, VariableTypeWrapper> figuredVariables,
            Set<String> notFiguredVariables) throws Exception {
        ActionStatement actionStatement = (ActionStatement) statement.getStatement();
        ActionTypeWrapper action = robot.getActionType(actionStatement.getActionName());
        for (int i = 0; i < actionStatement.getInputList().size(); i++) {
            IdentifierSet inputSet = actionStatement.getInputList().get(i);
            for (Identifier input : inputSet.getIdentifierSet()) {
                if (input.getType().equals(IdentifierType.VARIABLE)) {
                    VariableWrapper variable = null;
                    VariableTypeWrapper variableType = null;
                    for (VariableWrapper v : statement.getVariableList()) {
                        if (v.getName().equals(input.getId())) {
                            variable = v;
                            break;
                        }
                    }
                    if (inputSet.getIdentifierSet().size() <= 1) {
                        variableType = action.getVariableInputList().get(i);
                    } else {
                        VariableTypeWrapper complexVariableType =
                                action.getVariableInputList().get(i);
                        variableType = new VariableTypeWrapper();
                        variableType.setVariableType(DBService.getVariable(
                                complexVariableType.getVariableType().getType().toString()));
                    }
                    notFiguredVariables.remove(makeKey(service, variable));
                    figuredVariables.put(makeKey(service, variable), variableType);
                }
            }
        }
        for (int i = 0; i < actionStatement.getOutputList().size(); i++) {
            Identifier output = actionStatement.getOutputList().get(i);
            VariableWrapper variable = null;
            VariableTypeWrapper variableType = null;
            for (VariableWrapper v : statement.getVariableList()) {
                if (v.getName().equals(output.getId())) {
                    variable = v;
                    break;
                }
            }
            variableType = action.getVariableOutputList().get(i);
            notFiguredVariables.remove(makeKey(service, variable));
            figuredVariables.put(makeKey(service, variable), variableType);
        }
    }

    private static void getVariablesOfOtherStatement(ServiceWrapper service,
            StatementWrapper statement, Map<String, VariableTypeWrapper> figuredVariables,
            Set<String> notFiguredVariables) {
        for (VariableWrapper variable : statement.getVariableList()) {
            if (figuredVariables.keySet().contains(makeKey(service, variable))) {
                continue;
            }
            if (variableStore.keySet().contains(makeKey(service, variable))) {
                figuredVariables.put(makeKey(service, variable),
                        variableStore.get(makeKey(service, variable)));
                continue;
            }
            if (variableStore.keySet()
                    .contains(makeKey(variable.getCreator().key, variable.getCreator().value))) {
                figuredVariables.put(makeKey(service, variable), variableStore
                        .get(makeKey(variable.getCreator().key, variable.getCreator().value)));
                variableStore.put(makeKey(service, variable),
                        figuredVariables.get(makeKey(service, variable)));
                continue;
            }
            notFiguredVariables.add(makeKey(service, variable));
        }
    }

    private static Set<StatementWrapper> exploreService(RobotImplWrapper robot,
            ServiceWrapper service, StatementWrapper statement,
            Map<String, VariableTypeWrapper> figuredVariables, Set<String> notFiguredVariables,
            Set<StatementWrapper> visitedStatement) throws Exception {
        if (visitedStatement.contains(statement)) {
            return visitedStatement;
        } else {
            visitedStatement.add(statement);
        }

        if (statement.getStatement().getStatementType().equals(StatementType.ACTION)) {
            getVariableListOfAction(robot, service, statement, figuredVariables,
                    notFiguredVariables);
        } else {
            getVariablesOfOtherStatement(service, statement, figuredVariables, notFiguredVariables);
        }

        for (StatementWrapper nextStatement : statement.getConnectedStatements()
                .values(StatementWrapper.class)) {
            visitedStatement.addAll(exploreService(robot, service, nextStatement, figuredVariables,
                    notFiguredVariables, visitedStatement));
        }

        return visitedStatement;
    }

    private static void referAdditionalInfo(AdditionalInfo additionalInfo, ServiceWrapper service,
            Map<String, VariableTypeWrapper> figuredVariables, Set<String> notFiguredVariables) {
        for (CustomVariableInfo cVariable : additionalInfo.getVariableList()) {
            if (notFiguredVariables.contains(makeKey(service, cVariable.getName()))) {
                VariableTypeWrapper variable = new VariableTypeWrapper();
                variable.setVariableType(DBService.getVariable(cVariable.getType()));
                variableStore.put(makeKey(service, cVariable.getName()), variable);
                figuredVariables.put(makeKey(service, cVariable.getName()), variable);
                notFiguredVariables.remove(makeKey(service, cVariable.getName()));
            }
        }
    }

    private static Map<String, VariableTypeWrapper> visitMode(ModeWrapper mode,
            RobotImplWrapper robot, AdditionalInfo additionalInfo, MissionWrapper mission)
            throws Exception {
        Map<String, VariableTypeWrapper> figuredVariables = new HashMap<>();
        Set<String> notFiguredVariables = new HashSet<>();
        for (ParallelServiceWrapper service : mode.getServiceList()) {
            StatementWrapper firstStatement = service.getService().getStatementList().get(0);
            Set<StatementWrapper> visitedStatement = new HashSet<>();
            exploreService(robot, service.getService(), firstStatement, figuredVariables,
                    notFiguredVariables, visitedStatement);
            referAdditionalInfo(additionalInfo, service.getService(), figuredVariables,
                    notFiguredVariables);
        }
        for (GroupWrapper group : mode.getGroupList()) {
            TransitionWrapper transition = group.getModeTransition().getModeTransition();
            traverseTransition(transition, robot, additionalInfo, mission);
        }
        return figuredVariables;
    }
}

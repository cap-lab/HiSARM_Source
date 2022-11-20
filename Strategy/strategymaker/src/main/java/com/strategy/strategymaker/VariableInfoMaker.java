package com.strategy.strategymaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbmanager.commonlibraries.DBService;
import com.dbmanager.datastructure.variable.PrimitiveType;
import com.dbmanager.datastructure.variable.Variable;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ConditionalStatement;
import com.scriptparser.parserdatastructure.entity.statement.Statement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import com.scriptparser.parserdatastructure.util.KeyValue;
import com.scriptparser.parserdatastructure.util.KeyValueList;
import com.scriptparser.parserdatastructure.util.ModeTransitionVisitor;
import com.scriptparser.parserdatastructure.util.StatementVisitor;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import com.scriptparser.parserdatastructure.wrapper.VariableWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import com.strategy.strategydatastructure.additionalinfo.CustomVariableInfo;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;

public class VariableInfoMaker {
    private static Map<KeyValue<ServiceWrapper, String>, VariableTypeWrapper> variableStore = new HashMap<>();

    private static class RobotVariableTypeListMaker implements ModeTransitionVisitor {
        private RobotImplWrapper robot;
        private AdditionalInfo additionalInfo;
        private KeyValueList<ServiceWrapper, String> notFiguredVariables = new KeyValueList<>();
        private Map<KeyValue<ServiceWrapper, String>, VariableTypeWrapper> figuredVariables = new HashMap<>();

        private class StatementVariableTypeListMaker implements StatementVisitor {
            private ServiceWrapper service;

            public StatementVariableTypeListMaker(ServiceWrapper service) {
                this.service = service;
            }

            @Override
            public void visitActionStatement(StatementWrapper wrapper, ActionStatement statement,
                    int index) {
                try {
                    ActionTypeWrapper action = robot.getActionType(statement.getActionName());
                    for (int i = 0; i < statement.getInputList().size(); i++) {
                        IdentifierSet inputSet = statement.getInputList().get(i);
                        for (Identifier input : inputSet.getIdentifierSet()) {
                            if (input.getType().equals(IdentifierType.VARIABLE)) {
                                VariableWrapper variable = null;
                                VariableTypeWrapper variableType = null;
                                for (VariableWrapper v : wrapper.getVariableList()) {
                                    if (v.getName().equals(input.getId())) {
                                        variable = v;
                                        break;
                                    }
                                }
                                if (inputSet.getIdentifierSet().size() <= 1) {
                                    variableType = action.getVariableInputList().get(i);
                                } else {
                                    VariableTypeWrapper complexVariableType = action.getVariableInputList().get(i);
                                    variableType = new VariableTypeWrapper();
                                    variableType.setVariableType(
                                            DBService.getVariable(complexVariableType
                                                    .getVariableType().getType().toString()));
                                }
                                notFiguredVariables.remove(service, variable.getName());
                                figuredVariables.put(makeKey(service, variable.getName()),
                                        variableType);
                            }
                        }
                    }
                    for (int i = 0; i < statement.getOutputList().size(); i++) {
                        Identifier output = statement.getOutputList().get(i);
                        VariableWrapper variable = null;
                        VariableTypeWrapper variableType = null;
                        for (VariableWrapper v : wrapper.getVariableList()) {
                            if (v.getName().equals(output.getId())) {
                                variable = v;
                                break;
                            }
                        }
                        variableType = action.getVariableOutputList().get(i);
                        notFiguredVariables.remove(service, variable.getName());
                        figuredVariables.put(makeKey(service, variable), variableType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void getVariablesOfOtherStatement(StatementWrapper statement) {
                for (VariableWrapper variable : statement.getVariableList()) {
                    if (figuredVariables.keySet().contains(makeKey(service, variable))) {
                        continue;
                    }
                    if (variableStore.keySet().contains(makeKey(service, variable))) {
                        figuredVariables.put(makeKey(service, variable),
                                variableStore.get(makeKey(service, variable)));
                        continue;
                    }
                    if (variableStore.keySet().contains(
                            makeKey(variable.getCreator().key, variable.getCreator().value))) {
                        figuredVariables.put(makeKey(service, variable), variableStore.get(
                                makeKey(variable.getCreator().key, variable.getCreator().value)));
                        variableStore.put(makeKey(service, variable),
                                figuredVariables.get(makeKey(service, variable)));
                        continue;
                    }
                    notFiguredVariables.add(makeKey(service, variable));
                }
            }

            @Override
            public void visitCommunicationalStatement(StatementWrapper wrapper,
                    CommunicationalStatement statement, int index) {
                getVariablesOfOtherStatement(wrapper);
            }

            @Override
            public void visitConditionalStatement(StatementWrapper wrapper,
                    ConditionalStatement statement, int index) {
                getVariablesOfOtherStatement(wrapper);
            }

            @Override
            public void visitThrowStatement(StatementWrapper wrapper, ThrowStatement statement,
                    int index) {
                getVariablesOfOtherStatement(wrapper);
            }

            @Override
            public void visitOtherStatement(StatementWrapper wrapper, Statement statement,
                    int index) {
            }
        }

        public RobotVariableTypeListMaker(RobotImplWrapper robot, AdditionalInfo additionalInfo) {
            this.robot = robot;
            this.additionalInfo = additionalInfo;
        }

        private void referAdditionalInfo(ServiceWrapper service) {
            for (CustomVariableInfo cVariable : additionalInfo.getVariableList()) {
                if (notFiguredVariables.containKeyValue(makeKey(service, cVariable.getName()))) {
                    VariableTypeWrapper variable = new VariableTypeWrapper();
                    variable.setVariableType(DBService.getVariable(cVariable.getType()));
                    variableStore.put(makeKey(service, cVariable.getName()), variable);
                    figuredVariables.put(makeKey(service, cVariable.getName()), variable);
                    notFiguredVariables.remove(service, cVariable.getName());
                }
            }
        }

        @Override
        public void visitMode(ModeWrapper mode, String modeId, String groupId) {
            for (ParallelServiceWrapper service : mode.getServiceList()) {
                StatementVariableTypeListMaker visitor = new StatementVariableTypeListMaker(service.getService());
                service.getService().traverseService(visitor);
                referAdditionalInfo(service.getService());
            }
        }

        @Override
        public void visitTransition(TransitionWrapper transition, String transitionId,
                String groupId) {
        }

        public Map<KeyValue<ServiceWrapper, String>, VariableTypeWrapper> getFiguredVariables() {
            return figuredVariables;
        }

    }

    public static KeyValue<ServiceWrapper, String> makeKey(ServiceWrapper service,
            VariableWrapper variable) {
        return makeKey(service, variable.getName());
    }

    public static KeyValue<ServiceWrapper, String> makeKey(ServiceWrapper service,
            String variable) {
        return new KeyValue<ServiceWrapper, String>(service, variable);
    }

    public static void makePrimitiveVariableTypeList(RobotImplWrapper robot) {
        for (PrimitiveType ptype : PrimitiveType.values()) {
            VariableTypeWrapper variableType = new VariableTypeWrapper();
            Variable type = new Variable();
            type.setCount(1);
            type.setName(ptype.getValue());
            type.setSize(ptype.getSize());
            type.setType(ptype);
            variableType.setVariableType(type);
            robot.getPrimitiveVariableMap().put(ptype, variableType);
        }
    }

    public static void makeVariableInfoList(MissionWrapper mission, AdditionalInfo additionalInfo,
            List<RobotImplWrapper> robotList) {
        try {
            for (RobotImplWrapper robot : robotList) {
                RobotVariableTypeListMaker maker = new RobotVariableTypeListMaker(robot, additionalInfo);
                TransitionWrapper mainTransition = mission.getTransition(robot.getTeam());
                mainTransition.traverseTransition(new String(), robot.getTeam(), new ArrayList<>(),
                        null, maker, null);
                robot.setVariableMap(maker.getFiguredVariables());
            }
            for (RobotImplWrapper robot : robotList) {
                RobotVariableTypeListMaker maker = new RobotVariableTypeListMaker(robot, additionalInfo);
                TransitionWrapper mainTransition = mission.getTransition(robot.getTeam());
                mainTransition.traverseTransition(new String(), robot.getTeam(), new ArrayList<>(),
                        null, maker, null);
                robot.setVariableMap(maker.getFiguredVariables());
            }
            for (RobotImplWrapper robot : robotList) {
                makePrimitiveVariableTypeList(robot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

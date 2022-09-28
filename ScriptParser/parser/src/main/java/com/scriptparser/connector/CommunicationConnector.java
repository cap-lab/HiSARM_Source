package com.scriptparser.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.wrapper.CatchEventWrapper;
import com.scriptparser.parserdatastructure.wrapper.GroupWrapper;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.scriptparser.parserdatastructure.wrapper.TeamWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;

public class CommunicationConnector {

    private static Set<StatementWrapper> exploreServiceCounter(ServiceWrapper service, String team,
            String variable, StatementType type, List<IdentifierSet> arguments) {
        Set<StatementWrapper> counterStatements = new HashSet<>();
        List<Identifier> parameterList = service.getParameterList();
        Map<String, IdentifierSet> pair = mappingParameter(parameterList, arguments);
        for (StatementWrapper statement : service.getStatementList()) {
            if ((statement.getStatement().getStatementType().equals(StatementType.PUBLISH)
                    && type.equals(StatementType.SUBSCRIBE))
                    || (statement.getStatement().getStatementType().equals(StatementType.SEND)
                            && type.equals(StatementType.RECEIVE))) {
                CommunicationalStatement cStatement =
                        (CommunicationalStatement) statement.getStatement();
                String counterTeam = cStatement.getCounterTeam();
                for (String key : pair.keySet()) {
                    if (counterTeam.equals(key)) {
                        counterTeam = pair.get(key).getIdentifierSet().get(0).getId();
                    }
                }
                if (team.equals(counterTeam) && cStatement.getMessage().getId().equals(variable)) {
                    counterStatements.add(statement);
                }
            }
        }
        return counterStatements;
    }

    private static Set<StatementWrapper> visitModeCounter(MissionWrapper mission, ModeWrapper mode,
            String team, String variable, StatementType type, List<IdentifierSet> arguments)
            throws Exception {
        Set<StatementWrapper> counterStatements = new HashSet<>();
        List<Identifier> parameterList = mode.getParameterList();
        Map<String, IdentifierSet> pair = mappingParameter(parameterList, arguments);
        for (ParallelServiceWrapper service : mode.getServiceList()) {
            List<IdentifierSet> nextArguments =
                    replaceVariableToConstant(service.getInputList(), pair);
            counterStatements.addAll(exploreServiceCounter(service.getService(), team, variable,
                    type, nextArguments));
        }
        for (GroupWrapper group : mode.getGroupList()) {
            List<IdentifierSet> nextArguments =
                    replaceVariableToConstant(group.getModeTransition().getInputList(), pair);
            counterStatements.addAll(traverseTransitionCounter(mission,
                    group.getModeTransition().getModeTransition(), team, variable, type,
                    nextArguments));
        }
        return counterStatements;
    }

    private static Set<StatementWrapper> traverseTransitionCounter(MissionWrapper mission,
            TransitionWrapper transition, String team, String variable, StatementType type,
            List<IdentifierSet> arguments) throws Exception {
        List<Identifier> parameterList = transition.getParameterList();
        Map<String, IdentifierSet> pair = mappingParameter(parameterList, arguments);
        Set<StatementWrapper> counterStatements = new HashSet<>();
        TransitionModeWrapper defaultMode = transition.getDefaultMode();
        counterStatements.addAll(visitModeCounter(mission, defaultMode.getMode(), team, variable,
                type, replaceVariableToConstant(defaultMode.getInputList(), pair)));
        for (List<CatchEventWrapper> ceList : transition.getTransitionMap().values()) {
            for (CatchEventWrapper ce : ceList) {
                List<IdentifierSet> nextArguments =
                        replaceVariableToConstant(ce.getMode().getInputList(), pair);
                counterStatements.addAll(visitModeCounter(mission, ce.getMode().getMode(), team,
                        variable, type, nextArguments));
            }
        }
        return counterStatements;
    }

    private static void exploreService(MissionWrapper mission, TeamWrapper team,
            ServiceWrapper service, List<IdentifierSet> arguments) throws Exception {
        List<Identifier> parameterList = service.getParameterList();
        Map<String, IdentifierSet> pair = mappingParameter(parameterList, arguments);
        for (StatementWrapper statement : service.getStatementList()) {
            if (statement.getStatement().getStatementType().equals(StatementType.RECEIVE)
                    || statement.getStatement().getStatementType()
                            .equals(StatementType.SUBSCRIBE)) {
                CommunicationalStatement cStatement =
                        (CommunicationalStatement) statement.getStatement();
                String counterTeam = cStatement.getCounterTeam();
                for (String key : pair.keySet()) {
                    if (counterTeam.equals(key)) {
                        counterTeam = pair.get(key).getIdentifierSet().get(0).getId();
                    }
                }
                statement.setCounterStatements(new ArrayList<>(
                        traverseTransitionCounter(mission, mission.getTransition(counterTeam),
                                team.getTeam().getName(), cStatement.getMessage().getId(),
                                statement.getStatement().getStatementType(), new ArrayList<>())));
            }
        }
    }

    private static void visitMode(MissionWrapper mission, TeamWrapper team, ModeWrapper mode,
            List<IdentifierSet> arguments) throws Exception {
        List<Identifier> parameterList = mode.getParameterList();
        Map<String, IdentifierSet> pair = mappingParameter(parameterList, arguments);
        for (ParallelServiceWrapper service : mode.getServiceList()) {
            List<IdentifierSet> nextArguments =
                    replaceVariableToConstant(service.getInputList(), pair);
            exploreService(mission, team, service.getService(), nextArguments);
        }
        for (GroupWrapper group : mode.getGroupList()) {
            List<IdentifierSet> nextArguments =
                    replaceVariableToConstant(group.getModeTransition().getInputList(), pair);
            traverseTransition(mission, team, group.getModeTransition().getModeTransition(),
                    nextArguments);
        }
    }

    private static List<IdentifierSet> replaceVariableToConstant(List<IdentifierSet> inputSetList,
            Map<String, IdentifierSet> pair) {
        List<IdentifierSet> nextArguments = new ArrayList<>();
        if (inputSetList == null) {
            return nextArguments;
        }
        for (IdentifierSet inputSet : inputSetList) {
            if (inputSet.getIdentifierSet().size() == 1) {
                Identifier input = inputSet.getIdentifierSet().get(0);
                if (input.getType().equals(IdentifierType.VARIABLE)) {
                    nextArguments.add(pair.get(input.getId()));
                } else {
                    nextArguments.add(inputSet);
                }
            } else {
                List<Identifier> argumentSet = new ArrayList<>();
                for (Identifier input : inputSet.getIdentifierSet()) {
                    if (input.getType().equals(IdentifierType.VARIABLE)) {
                        argumentSet.add(pair.get(input.getId()).getIdentifierSet().get(0));
                    } else {
                        argumentSet.add(input);
                    }
                }
            }
        }
        return nextArguments;
    }

    private static Map<String, IdentifierSet> mappingParameter(List<Identifier> parameterList,
            List<IdentifierSet> arguments) {
        Map<String, IdentifierSet> pair = new HashMap<>();
        for (int i = 0; i < arguments.size(); i++) {
            pair.put(parameterList.get(i).getId(), arguments.get(i));
        }
        return pair;
    }

    private static void traverseTransition(MissionWrapper mission, TeamWrapper team,
            TransitionWrapper transition, List<IdentifierSet> arguments) throws Exception {
        List<Identifier> parameterList = transition.getParameterList();
        Map<String, IdentifierSet> pair = mappingParameter(parameterList, arguments);
        TransitionModeWrapper defaultMode = transition.getDefaultMode();
        visitMode(mission, team, defaultMode.getMode(),
                replaceVariableToConstant(defaultMode.getInputList(), pair));
        for (List<CatchEventWrapper> ceList : transition.getTransitionMap().values()) {
            for (CatchEventWrapper ce : ceList) {
                List<IdentifierSet> nextArguments =
                        replaceVariableToConstant(ce.getMode().getInputList(), pair);
                visitMode(mission, team, ce.getMode().getMode(), nextArguments);
            }
        }
    }

    public static void connectCommunication(MissionWrapper mission) throws Exception {
        for (TeamWrapper team : mission.getTeamList()) {
            TransitionWrapper transition;
            transition = mission.getTransition(team.getTeam().getName());
            traverseTransition(mission, team, transition, new ArrayList<>());
        }
    }
}

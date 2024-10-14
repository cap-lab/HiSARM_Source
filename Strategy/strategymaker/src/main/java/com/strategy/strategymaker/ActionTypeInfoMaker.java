package com.strategy.strategymaker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.dbmanager.commonlibraries.DBService;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.util.ModeTransitionVisitor;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;

public class ActionTypeInfoMaker {
    private static Set<ActionTypeWrapper> actionTypeStore = new HashSet<>();

    public static void makeActionTypeList(MissionWrapper mission,
            List<RobotImplWrapper> robotList) {
        for (RobotImplWrapper robot : robotList) {
            try {
                ModeTransitionVisitor collector = new ActionTypeCollector(robot);
                mission.getTransition(robot.getTeam()).traverseTransition(new String(),
                        robot.getTeam(), new ArrayList<String>(),
                        new ArrayList<String>(robot.getGroupMap().keySet()), collector, null);
                robot.setActionTypeList(
                        new ArrayList<>(((ActionTypeCollector) collector).getActionTypeSet()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class ActionTypeCollector implements ModeTransitionVisitor {
        private Set<ActionTypeWrapper> actionTypeSet = new HashSet<>();
        private RobotImplWrapper robot;

        public ActionTypeCollector(RobotImplWrapper robot) {
            this.robot = robot;
        }

        public Set<ActionTypeWrapper> getActionTypeSet() {
            return actionTypeSet;
        }

        @Override
        public void visitMode(ModeWrapper mode, String modeId, String currentGroup,
                String newGroupPrefix) {
            try {
                actionTypeSet.addAll(makeActionTypeSet(mode, robot));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private boolean alreadyGetAction(String actionName) {
            if (getActionType(actionName) == null) {
                return false;
            } else {
                return true;
            }
        }

        private ActionTypeWrapper getActionType(String actionName) {
            for (ActionTypeWrapper action : actionTypeStore) {
                if (action.getAction().getName().equals(actionName)) {
                    return action;
                }
            }
            return null;

        }

        private void getVariableType(ActionTypeWrapper actionType, List<String> variableNameList,
                List<VariableTypeWrapper> store) {
            for (String variableName : variableNameList) {
                VariableTypeWrapper variableType = new VariableTypeWrapper();
                variableType.setVariableType(DBService.getVariable(variableName));
                store.add(variableType);
            }
        }

        private Set<ActionTypeWrapper> makeActionTypeSet(ModeWrapper mode, RobotImplWrapper robot)
                throws Exception {
            Set<ActionTypeWrapper> actionTypeSet = new HashSet<>();
            for (ParallelServiceWrapper service : mode.getServiceList()) {
                for (StatementWrapper statement : service.getService().getStatementList()) {
                    if (statement.getStatement().getStatementType().equals(StatementType.ACTION)) {
                        ActionStatement action = (ActionStatement) statement.getStatement();
                        ActionTypeWrapper actionType = new ActionTypeWrapper();
                        if (alreadyGetAction(action.getActionName())) {
                            actionType = getActionType(action.getActionName());
                        } else if (DBService.isExistentAction(action.getActionName())) {
                            actionType.setAction(DBService.getAction(action.getActionName()));
                            actionType.setActionId(actionTypeStore.size());
                            getVariableType(actionType, actionType.getAction().getInputList(),
                                    actionType.getVariableInputList());
                            getVariableType(actionType, actionType.getAction().getOutputList(),
                                    actionType.getVariableOutputList());
                            if (actionType.getAction().getGroupAction() != null) {
                                getVariableType(actionType,
                                        actionType.getAction().getGroupAction().getSharedDataList(),
                                        actionType.getVariableSharedList());
                            }
                        } else {
                            throw new Exception("no action in DB :" + action.getActionName());
                        }
                        actionTypeSet.add(actionType);
                        actionTypeStore.add(actionType);
                    }
                }
            }
            return actionTypeSet;
        }

        @Override
        public void visitTransition(TransitionWrapper arg0, String arg1, String arg2) {}
    }
}

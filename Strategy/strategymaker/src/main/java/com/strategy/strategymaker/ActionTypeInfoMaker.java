package com.strategy.strategymaker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.dbmanager.commonlibraries.DBService;
import com.dbmanager.datastructure.action.GroupAction;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.wrapper.CatchEventWrapper;
import com.scriptparser.parserdatastructure.wrapper.GroupWrapper;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import com.strategy.strategymaker.constant.StrategyConstant;

public class ActionTypeInfoMaker {
    private static Set<ActionTypeWrapper> actionTypeStore = new HashSet<>();

    public static void makeActionTypeList(MissionWrapper mission,
            List<RobotImplWrapper> robotList) {
        for (RobotImplWrapper robot : robotList) {
            try {
                robot.setActionTypeList(new ArrayList<>(makeActionTypeList(mission, robot)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Set<ActionTypeWrapper> makeActionTypeList(MissionWrapper mission,
            RobotImplWrapper robot) throws Exception {
        Set<ActionTypeWrapper> actionTypeSet = new HashSet<>();
        String team = robot.getGroup(StrategyConstant.GROUP_DEFAULT);
        actionTypeSet.addAll(traverseTransition(mission.getTransition(team), robot, team));
        return actionTypeSet;
    }

    private static Set<ActionTypeWrapper> traverseTransition(TransitionWrapper transition,
            RobotImplWrapper robot, String currentKey) throws Exception {
        Set<ModeWrapper> modeSet = new HashSet<>();
        modeSet.add(transition.getDefaultMode().getMode());
        for (List<CatchEventWrapper> ceList : transition.getTransitionMap().values()) {
            for (CatchEventWrapper ce : ceList) {
                if (ce.getMode().getMode().getMode().getName().equals("FINISH")
                        || ce.getMode().getMode().getMode().getName().equals("PREVIOUS_MODE")) {
                    continue;
                }
                modeSet.add(ce.getMode().getMode());
            }
        }

        Set<ActionTypeWrapper> actionTypeSet = new HashSet<>();
        for (ModeWrapper mode : modeSet) {
            actionTypeSet.addAll(
                    visitMode(mode, robot, GroupAllocator.makeGroupKeyForMode(currentKey, mode)));
        }
        return actionTypeSet;
    }

    private static boolean alreadyGetAction(String actionName) {
        if (getActionType(actionName) == null) {
            return false;
        } else {
            return true;
        }
    }

    private static ActionTypeWrapper getActionType(String actionName) {
        for (ActionTypeWrapper action : actionTypeStore) {
            if (action.getAction().getName().equals(actionName)) {
                return action;
            }
        }
        return null;

    }

    private static void getVariableType(ActionTypeWrapper actionType, List<String> variableNameList,
            List<VariableTypeWrapper> store) {
        for (String variableName : variableNameList) {
            VariableTypeWrapper variableType = new VariableTypeWrapper();
            variableType.setVariableType(DBService.getVariable(variableName));
            store.add(variableType);
        }
    }

    private static Set<ActionTypeWrapper> makeActionTypeSet(ModeWrapper mode,
            RobotImplWrapper robot) throws Exception {
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
                        actionType.setGroupAction(false);
                        getVariableType(actionType, actionType.getAction().getInputList(),
                                actionType.getVariableInputList());
                        getVariableType(actionType, actionType.getAction().getOutputList(),
                                actionType.getVariableOutputList());
                    } else if (DBService.isExistentGroupAction(action.getActionName())) {
                        actionType.setAction(DBService.getGroupAction(action.getActionName()));
                        actionType.setGroupAction(true);
                        getVariableType(actionType, actionType.getAction().getInputList(),
                                actionType.getVariableInputList());
                        getVariableType(actionType, actionType.getAction().getOutputList(),
                                actionType.getVariableOutputList());
                        getVariableType(actionType,
                                ((GroupAction) (actionType.getAction())).getSharedDataList(),
                                actionType.getVariableSharedList());
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

    private static Set<ActionTypeWrapper> visitMode(ModeWrapper mode, RobotImplWrapper robot,
            String currentKey) throws Exception {
        Set<ActionTypeWrapper> actionTypeSet = makeActionTypeSet(mode, robot);
        if (robot.getGroupMap().containsKey(currentKey)) {
            for (GroupWrapper group : mode.getGroupList()) {
                actionTypeSet
                        .addAll(traverseTransition(group.getModeTransition().getModeTransition(),
                                robot, GroupAllocator.makeGroupKeyForTransition(currentKey,
                                        group.getModeTransition().getModeTransition())));
            }
        }
        return actionTypeSet;
    }
}

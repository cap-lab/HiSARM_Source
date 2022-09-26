package com.strategy.strategymaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.dbmanager.commonlibraries.DBService;
import com.dbmanager.datastructure.controlstrategy.ControlStrategyElement;
import com.strategy.strategydatastructure.wrapper.ActionImplWrapper;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import com.strategy.strategydatastructure.wrapper.ControlStrategyWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategymaker.additionalinfo.AdditionalInfo;
import com.strategy.strategymaker.additionalinfo.ControlStrategyInfo;
import com.strategy.strategymaker.constant.StrategyConstant;

public class ControlStrategyInfoMaker {
    private static Map<String, ControlStrategyWrapper> controlStrategyStore = new HashMap<>();
    private static Map<String, ActionImplWrapper> actionImplStore = new HashMap<>();

    public static void makeControlStrategyList(AdditionalInfo additionalInfo,
            List<RobotImplWrapper> robotList) {
        try {
            for (RobotImplWrapper robot : robotList) {
                String team = robot.getGroup(StrategyConstant.GROUP_DEFAULT);
                Set<ControlStrategyWrapper> controlStrategySet = new HashSet<>();
                for (ActionTypeWrapper action : robot.getActionTypeList()) {
                    String controlStrategyId = StrategyConstant.CONTROL_STRATEGY_DEFAULT;
                    ControlStrategyWrapper controlStrategy = new ControlStrategyWrapper();
                    if (containControlStrategyInfo(additionalInfo, team, robot, action)) {
                        controlStrategyId =
                                getControlStrategyInfo(additionalInfo, team, robot, action)
                                        .getStrategyId();
                    }
                    if (controlStrategyStore.keySet().contains(controlStrategyId)) {
                        controlStrategy = controlStrategyStore.get(controlStrategyId);
                    } else {
                        Set<ActionImplWrapper> actionImplSet = new HashSet<>();
                        controlStrategy
                                .setControlStrategy(DBService.getStrategy(controlStrategyId));
                        for (ControlStrategyElement csElement : controlStrategy.getControlStrategy()
                                .getStrategyImplList()) {
                            ActionImplWrapper actionImpl = new ActionImplWrapper();
                            if (actionImplStore.keySet().contains(csElement.getActionImplId())) {
                                actionImpl = actionImplStore.get(csElement.getActionImplId());
                            } else {
                                actionImpl.setActionImpl(
                                        DBService.getActionImpl(csElement.getActionImplId()));
                                actionImpl.setAction(action);
                                actionImplStore.put(csElement.getActionImplId(), actionImpl);
                            }
                            actionImplSet.add(actionImpl);
                        }
                        controlStrategy.setActionList(new ArrayList<>(actionImplSet));
                    }
                    controlStrategySet.add(controlStrategy);
                }
                robot.setControlStrategyList(new ArrayList<>(controlStrategySet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ControlStrategyInfo getControlStrategyInfo(AdditionalInfo additionalInfo,
            String team, RobotImplWrapper robot, ActionTypeWrapper action) {
        for (ControlStrategyInfo controlStrategyInfo : additionalInfo.getStrategyList()) {
            if (controlStrategyInfo.getActionName().equals(action.getAction().getName())
                    || controlStrategyInfo.getRobotClass()
                            .equals(robot.getRobotType().getRobotType().getRobotClass())
                    || controlStrategyInfo.getTeamName().equals(team)) {
                return controlStrategyInfo;
            }
        }
        return null;
    }

    private static boolean containControlStrategyInfo(AdditionalInfo additionalInfo, String team,
            RobotImplWrapper robot, ActionTypeWrapper action) {
        if (getControlStrategyInfo(additionalInfo, team, robot, action) == null) {
            return false;
        } else {
            return true;
        }
    }
}

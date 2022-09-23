package com.strategy.strategymaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.scriptparser.parserdatastructure.wrapper.CatchEventWrapper;
import com.scriptparser.parserdatastructure.wrapper.GroupWrapper;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TeamWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;

public class GroupAllocator {
    private static List<String> exploredList = new ArrayList<>();


    private static boolean robotMatchWithGroupConstraint(RobotImplWrapper robot,
            GroupWrapper group) {
        return true;
    }

    private static boolean robotAlreadyAllocated(
            Map<GroupWrapper, List<RobotImplWrapper>> allocationMap, RobotImplWrapper robot) {
        for (List<RobotImplWrapper> robotList : allocationMap.values()) {
            if (robotList.contains(robot)) {
                return true;
            }
        }
        return false;
    }

    private static void traverseMode(String modeId, ModeWrapper mode,
            List<RobotImplWrapper> robotList) throws Exception {
        List<RobotImplWrapper> tmpRobotList = new ArrayList<>(robotList);
        if (mode.getGroupList().size() <= 0 || exploredList.contains(modeId)) {
            return;
        }
        exploredList.add(modeId);

        Map<GroupWrapper, List<RobotImplWrapper>> allocationMap = new HashMap<>();
        for (GroupWrapper group : mode.getGroupList()) {
            int count = group.getGroup().getMin();
            List<RobotImplWrapper> allocatedList = new ArrayList<>();
            allocationMap.put(group, allocatedList);
            for (RobotImplWrapper robot : tmpRobotList) {
                if (robotAlreadyAllocated(allocationMap, robot)) {
                    continue;
                }
                if (robotMatchWithGroupConstraint(robot, group)) {
                    robot.getGroupMap().put(modeId, group.getGroup().getName());
                    allocatedList.add(robot);
                    count = count - 1;
                    if (count == 0) {
                        break;
                    }
                }
            }
            if (count != 0) {
                throw new Exception("not enough robot for group");
            }
        }
        for (GroupWrapper group : mode.getGroupList()) {
            int count = group.getGroup().getProper() - group.getGroup().getMin();
            List<RobotImplWrapper> allocatedList = allocationMap.get(group);
            for (RobotImplWrapper robot : tmpRobotList) {
                if (robotAlreadyAllocated(allocationMap, robot)) {
                    continue;
                }
                if (robotMatchWithGroupConstraint(robot, group)) {
                    robot.getGroupMap().put(modeId, group.getGroup().getName());
                    allocatedList.add(robot);
                    count = count - 1;
                    if (count == 0) {
                        break;
                    }
                }
            }
        }


        for (GroupWrapper group : allocationMap.keySet()) {
            traverseTransition(
                    modeId + group.getModeTransition().getModeTransition().getTransition()
                            .getName(),
                    group.getModeTransition().getModeTransition(), allocationMap.get(group));
        }
    }

    private static void traverseTransition(String transitionId, TransitionWrapper transition,
            List<RobotImplWrapper> robotList) throws Exception {
        traverseMode(transitionId + transition.getDefaultMode().getMode().getMode().getName(),
                transition.getDefaultMode().getMode(), robotList);
        for (ModeWrapper key : transition.getTransitionMap().keySet()) {
            traverseMode(transitionId + key.getMode().getName(), key, robotList);
            for (CatchEventWrapper catchEvent : transition.getTransitionMap().get(key)) {
                traverseMode(transitionId + catchEvent.getMode().getMode().getMode().getName(),
                        catchEvent.getMode().getMode(), robotList);
            }
        }

    }

    public static void allocateGroup(MissionWrapper mission, List<RobotImplWrapper> robotList) {
        try {
            for (TeamWrapper team : mission.getTeamList()) {
                TransitionWrapper mainTransition = mission.getTransition(team.getTeam().getName());
                traverseTransition(team.getTeam().getName(), mainTransition, robotList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

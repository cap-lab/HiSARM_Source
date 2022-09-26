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

    private static int a(Map<GroupWrapper, List<RobotImplWrapper>> allocationMap,
            List<RobotImplWrapper> candidateRobotList, List<RobotImplWrapper> allocatedList,
            GroupWrapper group, String modeId, int count) {
        for (RobotImplWrapper robot : candidateRobotList) {
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
        return count;
    }

    private static void allocateRequired(Map<GroupWrapper, List<RobotImplWrapper>> allocationMap,
            List<RobotImplWrapper> candidateRobotList, ModeWrapper mode, String modeId)
            throws Exception {
        for (GroupWrapper group : mode.getGroupList()) {
            int count = group.getGroup().getMin();
            List<RobotImplWrapper> allocatedList = new ArrayList<>();
            allocationMap.put(group, allocatedList);
            count = a(allocationMap, candidateRobotList, allocatedList, group, modeId, count);
            if (count != 0) {
                throw new Exception("not enough robot for group");
            }
        }
    }

    private static void allocateAdditional(Map<GroupWrapper, List<RobotImplWrapper>> allocationMap,
            List<RobotImplWrapper> candidateRobotList, ModeWrapper mode, String modeId) {
        for (GroupWrapper group : mode.getGroupList()) {
            int count = group.getGroup().getProper() - group.getGroup().getMin();
            List<RobotImplWrapper> allocatedList = allocationMap.get(group);
            a(allocationMap, candidateRobotList, allocatedList, group, modeId, count);
        }
    }

    public static String makeGroupKeyForTransition(String previousKey,
            TransitionWrapper transition) {
        return previousKey + transition.getTransition().getName();
    }

    public static String makeGroupKeyForMode(String previousKey, ModeWrapper mode) {
        return previousKey + mode.getMode().getName();
    }

    private static void traverseMode(String modeId, ModeWrapper mode,
            List<RobotImplWrapper> robotList) throws Exception {
        List<RobotImplWrapper> candidateRobotList = new ArrayList<>(robotList);
        if (mode.getGroupList().size() <= 0 || exploredList.contains(modeId)) {
            return;
        }
        exploredList.add(modeId);

        Map<GroupWrapper, List<RobotImplWrapper>> allocationMap = new HashMap<>();
        allocateRequired(allocationMap, candidateRobotList, mode, modeId);
        allocateAdditional(allocationMap, candidateRobotList, mode, modeId);

        for (GroupWrapper group : allocationMap.keySet()) {
            traverseTransition(
                    makeGroupKeyForTransition(modeId,
                            group.getModeTransition().getModeTransition()),
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
                traverseMode(makeGroupKeyForMode(transitionId, catchEvent.getMode().getMode()),
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

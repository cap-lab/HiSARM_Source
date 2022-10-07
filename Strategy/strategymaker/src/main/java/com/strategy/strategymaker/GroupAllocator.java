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

    private static boolean robotAlreadyAllocated(Map<String, List<RobotImplWrapper>> allocationMap,
            RobotImplWrapper robot) {
        for (List<RobotImplWrapper> robotList : allocationMap.values()) {
            if (robotList.contains(robot)) {
                return true;
            }
        }
        return false;
    }

    private static int allocate(Map<String, List<RobotImplWrapper>> allocationMap,
            List<RobotImplWrapper> candidateRobotList, List<RobotImplWrapper> allocatedList,
            GroupWrapper group, String groupId, int count) {
        for (RobotImplWrapper robot : candidateRobotList) {
            if (robotAlreadyAllocated(allocationMap, robot)) {
                continue;
            }
            if (robotMatchWithGroupConstraint(robot, group)) {
                robot.getGroupList().add(groupId);
                allocatedList.add(robot);
                count = count - 1;
                if (count == 0) {
                    break;
                }
            }
        }
        return count;
    }

    public static String makeGroupKey(String previousKey, String choosedGroup) {
        return previousKey + "_" + choosedGroup;
    }

    public static String makeTransitionId(String previousKey, TransitionWrapper transition) {
        return previousKey + "_" + transition.getTransition().getName();
    }

    public static String makeModeId(String previousKey, ModeWrapper mode) {
        return previousKey + "_" + mode.getMode().getName();
    }


    private static void allocateRequired(Map<String, List<RobotImplWrapper>> allocationMap,
            List<RobotImplWrapper> candidateRobotList, ModeWrapper mode, String previousGroupId)
            throws Exception {
        for (GroupWrapper group : mode.getGroupList()) {
            String newGroupId = makeGroupKey(previousGroupId, group.getGroup().getName());
            int count = group.getGroup().getMin();
            List<RobotImplWrapper> allocatedList = new ArrayList<>();
            allocationMap.put(newGroupId, allocatedList);
            count = allocate(allocationMap, candidateRobotList, allocatedList, group, newGroupId,
                    count);
            if (count != 0) {
                throw new Exception("not enough robot for group");
            }
        }
    }

    private static void allocateAdditional(Map<String, List<RobotImplWrapper>> allocationMap,
            List<RobotImplWrapper> candidateRobotList, ModeWrapper mode, String previousGroupId) {
        for (GroupWrapper group : mode.getGroupList()) {
            String newGroupId = makeGroupKey(previousGroupId, group.getGroup().getName());
            int count = group.getGroup().getProper() - group.getGroup().getMin();
            List<RobotImplWrapper> allocatedList = allocationMap.get(newGroupId);
            allocate(allocationMap, candidateRobotList, allocatedList, group, newGroupId, count);
        }
    }

    private static void traverseMode(String modeId, String currentGroup, ModeWrapper mode,
            List<RobotImplWrapper> robotList) throws Exception {
        List<RobotImplWrapper> candidateRobotList = new ArrayList<>(robotList);
        if (mode.getGroupList().size() <= 0 || exploredList.contains(modeId)) {
            return;
        }
        exploredList.add(modeId);

        Map<String, List<RobotImplWrapper>> allocationMap = new HashMap<>();
        allocateRequired(allocationMap, candidateRobotList, mode, currentGroup);
        allocateAdditional(allocationMap, candidateRobotList, mode, currentGroup);

        for (GroupWrapper group : mode.getGroupList()) {
            String newGroupId = makeGroupKey(currentGroup, group.getGroup().getName());
            traverseTransition(
                    makeTransitionId(modeId, group.getModeTransition().getModeTransition()),
                    newGroupId, group.getModeTransition().getModeTransition(),
                    allocationMap.get(newGroupId));
        }
    }

    private static void traverseTransition(String transitionId, String currentGroup,
            TransitionWrapper transition, List<RobotImplWrapper> robotList) throws Exception {
        traverseMode(makeModeId(transitionId, transition.getDefaultMode().getMode()), currentGroup,
                transition.getDefaultMode().getMode(), robotList);
        for (ModeWrapper key : transition.getTransitionMap().keySet()) {
            for (CatchEventWrapper ce : transition.getTransitionMap().get(key)) {
                ModeWrapper mode = ce.getMode().getMode();
                traverseMode(makeModeId(transitionId, mode), currentGroup, mode, robotList);
            }
        }
    }

    public static void allocateGroup(MissionWrapper mission, List<RobotImplWrapper> robotList) {
        try {
            for (TeamWrapper team : mission.getTeamList()) {
                TransitionWrapper mainTransition = mission.getTransition(team.getTeam().getName());
                traverseTransition(team.getTeam().getName(), team.getTeam().getName(),
                        mainTransition, robotList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

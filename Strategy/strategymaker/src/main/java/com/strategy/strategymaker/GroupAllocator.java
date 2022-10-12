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
    private static Map<String, List<RobotImplWrapper>> allocationMap = new HashMap<>();

    private static boolean robotMatchWithGroupConstraint(RobotImplWrapper robot,
            GroupWrapper group) {
        return true;
    }

    private static int allocate(List<RobotImplWrapper> candidateRobotList,
            List<RobotImplWrapper> allocatedList, GroupWrapper group, String groupId, int count) {
        int candidateNum = candidateRobotList.size();
        for (int i = 0; i < candidateNum && count > 0; i++) {
            RobotImplWrapper robot = candidateRobotList.get(i);
            if (robotMatchWithGroupConstraint(robot, group)) {
                robot.getGroupList().add(groupId);
                allocatedList.add(robot);
                candidateRobotList.remove(i);
                candidateNum = candidateNum - 1;
                i = i - 1;
                count = count - 1;
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


    private static void allocateRequired(List<RobotImplWrapper> candidateRobotList,
            ModeWrapper mode, String previousGroupId) throws Exception {
        for (GroupWrapper group : mode.getGroupList()) {
            String newGroupId = makeGroupKey(previousGroupId, group.getGroup().getName());
            int count = group.getGroup().getMin();
            List<RobotImplWrapper> allocatedList = new ArrayList<>();
            allocationMap.put(newGroupId, allocatedList);
            count = allocate(candidateRobotList, allocatedList, group, newGroupId, count);
            if (count != 0) {
                throw new Exception("not enough robot for group");
            }
        }
    }

    private static void allocateAdditional(List<RobotImplWrapper> candidateRobotList,
            ModeWrapper mode, String previousGroupId) {
        for (GroupWrapper group : mode.getGroupList()) {
            String newGroupId = makeGroupKey(previousGroupId, group.getGroup().getName());
            int count = group.getGroup().getProper() - group.getGroup().getMin();
            List<RobotImplWrapper> allocatedList = allocationMap.get(newGroupId);
            allocate(candidateRobotList, allocatedList, group, newGroupId, count);
        }
    }

    private static void traverseMode(String modeId, String currentGroup, ModeWrapper mode,
            List<RobotImplWrapper> robotList) throws Exception {
        List<RobotImplWrapper> candidateRobotList = new ArrayList<>(robotList);
        if (mode.getGroupList().size() <= 0 || exploredList.contains(modeId)) {
            return;
        }
        exploredList.add(modeId);

        allocateRequired(candidateRobotList, mode, currentGroup);
        allocateAdditional(candidateRobotList, mode, currentGroup);

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
                List<RobotImplWrapper> candidateRobotList = new ArrayList<>();
                for (RobotImplWrapper robot : robotList) {
                    if (robot.getTeam().equals(team.getTeam().getName())) {
                        candidateRobotList.add(robot);
                    }
                }
                TransitionWrapper mainTransition = mission.getTransition(team.getTeam().getName());
                traverseTransition(team.getTeam().getName(), team.getTeam().getName(),
                        mainTransition, candidateRobotList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

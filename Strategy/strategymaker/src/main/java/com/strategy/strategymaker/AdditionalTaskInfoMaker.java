package com.strategy.strategymaker;

import java.util.List;
import com.dbmanager.commonlibraries.DBService;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import com.strategy.strategydatastructure.enumeration.AdditionalTaskType;
import com.strategy.strategydatastructure.wrapper.GroupingAlgorithmWrapper;
import com.strategy.strategydatastructure.wrapper.LeaderSelectionAlgorithmWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;

public class AdditionalTaskInfoMaker {
    public static void makeAddtionalTaskInfo(List<RobotImplWrapper> robotList,
            AdditionalInfo additionalInfo) {
        for (RobotImplWrapper robot : robotList) {
            makeLeaderTaskInfo(robot, additionalInfo.getLeaderSelectionAlgorithm());
            makeGroupingTaskInfo(robot, additionalInfo.getGroupingAlgorithm());
        }
    }

    private static void makeLeaderTaskInfo(RobotImplWrapper robot, String leaderSelectionId) {
        LeaderSelectionAlgorithmWrapper task = new LeaderSelectionAlgorithmWrapper();
        task.setType(AdditionalTaskType.LEADER_SELECTION);
        task.setLeaderSelectionAlgorithm(DBService.getLeaderSelectionAlgorithm(leaderSelectionId));
        task.setTask(DBService.getTask(task.getLeaderSelectionAlgorithm().getTaskId()));
        robot.getAdditionalTaskList().add(task);
    }

    private static void makeGroupingTaskInfo(RobotImplWrapper robot, String groupingId) {
        GroupingAlgorithmWrapper task = new GroupingAlgorithmWrapper();
        task.setType(AdditionalTaskType.GROUP_SELECTION);
        task.setGroupingAlgorithm(DBService.getGroupingAlgorithm(groupingId));
        task.setTask(DBService.getTask(task.getGroupingAlgorithm().getRunTimeTask()));
        robot.getAdditionalTaskList().add(task);
    }
}

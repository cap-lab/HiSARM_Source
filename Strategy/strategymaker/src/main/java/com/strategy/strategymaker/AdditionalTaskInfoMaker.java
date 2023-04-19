package com.strategy.strategymaker;

import java.util.List;
import com.dbmanager.commonlibraries.DBService;
import com.dbmanager.datastructure.groupingalgorithm.GroupingAlgorithm;
import com.dbmanager.datastructure.leaderselectionalgorithm.LeaderSelectionAlgorithm;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import com.strategy.strategydatastructure.enumeration.AdditionalTaskType;
import com.strategy.strategydatastructure.wrapper.AdditionalTaskWrapper;
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
        AdditionalTaskWrapper additionalTask = new AdditionalTaskWrapper();
        additionalTask.setType(AdditionalTaskType.LEADER_SELECTION);
        LeaderSelectionAlgorithm leaderSelection =
                DBService.getLeaderSelectionAlgorithm(leaderSelectionId);
        additionalTask.setTask(DBService.getTask(leaderSelection.getTaskId()));
        robot.getAdditionalTaskList().add(additionalTask);
    }

    private static void makeGroupingTaskInfo(RobotImplWrapper robot, String groupingId) {
        AdditionalTaskWrapper additionalTask = new AdditionalTaskWrapper();
        additionalTask.setType(AdditionalTaskType.GROUP_SELECTION);
        GroupingAlgorithm grouping = DBService.getGroupingAlgorithm(groupingId);
        additionalTask.setTask(DBService.getTask(grouping.getRunTimeTask()));
        robot.getAdditionalTaskList().add(additionalTask);
    }
}

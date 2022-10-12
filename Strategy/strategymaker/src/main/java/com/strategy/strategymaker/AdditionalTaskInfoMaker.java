package com.strategy.strategymaker;

import java.util.List;
import com.dbmanager.commonlibraries.DBService;
import com.strategy.strategydatastructure.enumeration.AdditionalTaskType;
import com.strategy.strategydatastructure.wrapper.AdditionalTaskWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;

public class AdditionalTaskInfoMaker {
    public static void makeAddtionalTaskInfo(List<RobotImplWrapper> robotList) {
        for (RobotImplWrapper robot : robotList) {
            AdditionalTaskWrapper additionalTask = new AdditionalTaskWrapper();
            additionalTask.setType(AdditionalTaskType.LEADER_SELECTION);
            additionalTask
                    .setTask(DBService.getTask(AdditionalTaskType.LEADER_SELECTION.getValue()));
            robot.getAdditionalTaskList().add(additionalTask);
        }
    }
}

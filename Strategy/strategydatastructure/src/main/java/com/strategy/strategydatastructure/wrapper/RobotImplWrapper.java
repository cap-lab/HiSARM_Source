package com.strategy.strategydatastructure.wrapper;

import java.util.List;
import com.dbmanager.datastructure.robot.RobotImpl;
import com.dbmanager.datastructure.task.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotImplWrapper {
    private RobotImpl robot;
    private RobotTypeWrapper robotType;
    private List<String> groupList;
    private List<ControlStrategyWrapper> controlStrategyList;
    private List<Task> additionalTaskList;
}

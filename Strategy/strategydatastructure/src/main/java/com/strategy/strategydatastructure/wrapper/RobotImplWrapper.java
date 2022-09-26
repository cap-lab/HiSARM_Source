package com.strategy.strategydatastructure.wrapper;

import java.util.List;
import java.util.Map;
import com.dbmanager.datastructure.robot.RobotImpl;
import com.dbmanager.datastructure.task.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotImplWrapper {
    private RobotImpl robot;
    private RobotTypeWrapper robotType;
    private Map<String, String> groupMap;
    private List<ControlStrategyWrapper> controlStrategyList;
    private List<ActionTypeWrapper> actionTypeList;
    private List<Task> additionalTaskList;

    public String getGroup(String groupKey) throws Exception {
        if (!groupMap.containsKey(groupKey)) {
            throw new Exception(
                    "No group whose a key is " + groupKey + " in the robot " + robot.getRobotId());
        } else {
            return groupMap.get(groupKey);
        }
    }
}

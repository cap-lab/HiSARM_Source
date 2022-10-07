package com.strategy.strategydatastructure.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.dbmanager.datastructure.robot.RobotImpl;
import com.dbmanager.datastructure.task.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotImplWrapper {
    private RobotImpl robot;
    private int robotIndex;
    private RobotTypeWrapper robotType;
    private List<String> groupList = new ArrayList<>();
    private List<ControlStrategyWrapper> controlStrategyList = new ArrayList<>();
    private List<ActionTypeWrapper> actionTypeList = new ArrayList<>();
    private List<Task> additionalTaskList = new ArrayList<>();
    private Map<String, VariableTypeWrapper> variableList = new HashMap<>();

    public ActionTypeWrapper getActionType(String actionType) throws Exception {
        Optional<ActionTypeWrapper> team = getActionTypeList().stream()
                .filter(t -> t.getAction().getName().equals(actionType)).findAny();
        if (team.isPresent()) {
            return team.get();
        } else {
            throw new Exception("No action whose name is " + actionType);
        }
    }
}

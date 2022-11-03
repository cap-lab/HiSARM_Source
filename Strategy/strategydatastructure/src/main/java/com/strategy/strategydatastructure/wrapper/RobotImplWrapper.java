package com.strategy.strategydatastructure.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.dbmanager.datastructure.robot.RobotImpl;
import com.scriptparser.parserdatastructure.util.KeyValue;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
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
    private Map<KeyValue<ServiceWrapper, String>, VariableTypeWrapper> variableMap =
            new HashMap<>();
    private List<AdditionalTaskWrapper> additionalTaskList = new ArrayList<>();

    public ActionTypeWrapper getActionType(String actionType) throws Exception {
        Optional<ActionTypeWrapper> action = getActionTypeList().stream()
                .filter(t -> t.getAction().getName().equals(actionType)).findAny();
        if (action.isPresent()) {
            return action.get();
        } else {
            throw new Exception("No action whose name is " + actionType);
        }
    }

    public void addTeam(String team) {
        groupList.add(0, team);
    }

    public String getTeam() throws Exception {
        if (groupList.size() > 0) {
            return groupList.get(0);
        } else {
            throw new Exception("A team is not yet allocated");
        }
    }
}

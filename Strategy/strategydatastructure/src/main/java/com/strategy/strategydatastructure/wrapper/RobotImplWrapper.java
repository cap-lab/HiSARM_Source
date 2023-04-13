package com.strategy.strategydatastructure.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.dbmanager.datastructure.robot.RobotImpl;
import com.dbmanager.datastructure.variable.PrimitiveType;
import com.scriptparser.parserdatastructure.util.KeyValue;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.strategy.strategydatastructure.enumeration.AdditionalTaskType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotImplWrapper {
    private RobotImpl robot;
    private int robotIndex;
    private RobotTypeWrapper robotType;
    private String team;
    private Map<String, Integer> groupMap = new HashMap<>();
    private List<ControlStrategyWrapper> controlStrategyList = new ArrayList<>();
    private List<ActionTypeWrapper> actionTypeList = new ArrayList<>();
    private List<ResourceWrapper> resourceList = new ArrayList<>();
    private GroupingAlgorithmWrapper groupingAlgorithm;
    private Map<KeyValue<ServiceWrapper, String>, VariableTypeWrapper> variableMap =
            new HashMap<>();
    private Map<PrimitiveType, VariableTypeWrapper> primitiveVariableMap = new HashMap<>();
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

    public void addTeam(String team, int groupIndex) {
        this.team = team;
        if (!groupMap.containsKey(team)) {
            groupMap.put(team, groupIndex);
        }
    }

    public String getTeam() throws Exception {
        if (team != null) {
            return team;
        } else {
            throw new Exception("A team is not yet allocated");
        }
    }

    public ResourceWrapper getResource(String resourceId) throws Exception {
        Optional<ResourceWrapper> resource = resourceList.stream()
                .filter(r -> r.getResource().getResourceId().equals(resourceId)).findAny();
        if (resource.isPresent()) {
            return resource.get();
        } else {
            throw new Exception("No resource which name is " + resourceId + " for "
                    + robotType.getRobotType().getRobotClass());
        }
    }

    public AdditionalTaskWrapper getAdditionalTask(AdditionalTaskType type) throws Exception {
        Optional<AdditionalTaskWrapper> task =
                additionalTaskList.stream().filter(t -> t.getType().equals(type)).findAny();
        if (task.isPresent()) {
            return task.get();
        } else {
            throw new Exception("No additional task which type is " + type.getValue());
        }
    }

    public VariableTypeWrapper getVariableType(ServiceWrapper service, String variableName) {
        for (KeyValue<ServiceWrapper, String> key : variableMap.keySet()) {
            if (key.key.equals(service) && key.value.equals(variableName)) {
                return variableMap.get(key);
            }
        }
        return null;
    }
}

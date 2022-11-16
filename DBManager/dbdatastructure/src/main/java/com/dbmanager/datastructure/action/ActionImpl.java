package com.dbmanager.datastructure.action;

import java.util.ArrayList;
import java.util.List;

public class ActionImpl {
    private String actionImplId;
    private String robotClass;
    private String actionName;
    private String taskId;
    private List<String> neededResource = new ArrayList<>();
    private boolean returnImmediate;

    public void setActionImplId(String actionId) {
        this.actionImplId = actionId;
    }

    public String getActionImplId() {
        return actionImplId;
    }

    public void setRobotClass(String robotClass) {
        this.robotClass = robotClass;
    }

    public String getRobotClass() {
        return robotClass;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public List<String> getNeededResource() {
        return neededResource;
    }

    public void setNeededResource(List<String> neededResource) {
        this.neededResource = neededResource;
    }

    public boolean getReturnImmediate() {
        return returnImmediate;
    }

    public void setReturnImmediate(boolean returnImmediate) {
        this.returnImmediate = returnImmediate;
    }
}

package com.dbmanager.datastructure.resource;

import java.util.ArrayList;
import java.util.List;

public class Resource {
    public enum ResourceType {
        SENSOR("sensor"), ACTUATOR("actuator"), VIRTUAL_SENSOR("virtual sensor"), VIRTUAL_ACTUATOR(
                "virtual actuator");

        private String value;

        ResourceType(String value) {
            this.value = value;
        }

        public static ResourceType getResourceType(String value) {
            for (ResourceType resourceType : ResourceType.values()) {
                if (resourceType.getValue().equals(value)) {
                    return resourceType;
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }
    }

    private String resourceId;
    private String robotClass;
    private String taskId;
    private int dataSize;
    private boolean conflict;
    private ResourceType resourceType;
    private List<String> requiredResources;

    public Resource() {
        requiredResources = new ArrayList<>();
    }

    public void setRequiredResources(List<String> requiredResources) {
        this.requiredResources = requiredResources;
    }

    public List<String> getRequiredResources() {
        return requiredResources;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getRobotClass() {
        return robotClass;
    }

    public void setRobotClass(String robotClass) {
        this.robotClass = robotClass;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public boolean isConflict() {
        return conflict;
    }

    public void setConflict(boolean conflict) {
        this.conflict = conflict;
    }

}

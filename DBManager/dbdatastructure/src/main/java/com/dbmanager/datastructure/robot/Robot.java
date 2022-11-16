package com.dbmanager.datastructure.robot;

import java.util.ArrayList;
import java.util.List;

public class Robot {
    private String robotClass;
    private String primaryArchitecture;
    private List<String> architectureList = new ArrayList<>();
    private List<String> resourceList = new ArrayList<>();

    public String getRobotClass() {
        return robotClass;
    }

    public void setRobotClass(String robotClass) {
        this.robotClass = robotClass;
    }

    public String getPrimaryArchitecture() {
        return primaryArchitecture;
    }

    public void setPrimaryArchitecture(String primaryArchitecture) {
        this.primaryArchitecture = primaryArchitecture;
    }

    public List<String> getArchitectureList() {
        return architectureList;
    }

    public void setArchitectureList(List<String> architectureList) {
        this.architectureList = architectureList;
    }

    public List<String> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<String> resourceList) {
        this.resourceList = resourceList;
    }
}

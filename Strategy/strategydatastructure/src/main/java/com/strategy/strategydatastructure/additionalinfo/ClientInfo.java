package com.strategy.strategydatastructure.additionalinfo;

import java.util.List;

public class ClientInfo {
    private String id;
    private List<SimulationRobot> robotMappingInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SimulationRobot> getRobotMappingInfo() {
        return robotMappingInfo;
    }

    public void setRobotMappingInfo(List<SimulationRobot> robotList) {
        this.robotMappingInfo = robotList;
    }
}

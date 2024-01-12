package com.strategy.strategydatastructure.additionalinfo;

import java.util.List;

public class ClientInfo {
    private String id;
    private List<SimulationRobot> robotList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SimulationRobot> getRobotList() {
        return robotList;
    }

    public void setRobotList(List<SimulationRobot> robotList) {
        this.robotList = robotList;
    }
}

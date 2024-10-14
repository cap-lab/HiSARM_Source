package com.dbmanager.datastructure.robot;

import java.util.HashMap;
import java.util.Map;

public class RobotImpl {
    private String robotId;
    private String robotClass;
    private Map<ConnectionType, CommunicationAddress> communicationInfoMap = new HashMap<>();

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public String getRobotClass() {
        return robotClass;
    }

    public void setRobotClass(String robotClass) {
        this.robotClass = robotClass;
    }

    public Map<ConnectionType, CommunicationAddress> getCommunicationInfoMap() {
        return communicationInfoMap;
    }

    public void setCommunicationInfoMap(
            Map<ConnectionType, CommunicationAddress> communicationInfoMap) {
        this.communicationInfoMap = communicationInfoMap;
    }

}

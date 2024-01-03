package com.dbmanager.datastructure.simulationdevice;

import java.util.HashMap;
import java.util.Map;
import com.dbmanager.datastructure.robot.CommunicationAddress;
import com.dbmanager.datastructure.robot.ConnectionType;

public class SimulationDevice {
    private String deviceId;
    private String architecture;
    private Map<ConnectionType, CommunicationAddress> communicationInfoMap = new HashMap<>();

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public Map<ConnectionType, CommunicationAddress> getCommunicationInfoMap() {
        return communicationInfoMap;
    }

    public void setCommunicationInfoMap(
            Map<ConnectionType, CommunicationAddress> communicationInfoMap) {
        this.communicationInfoMap = communicationInfoMap;
    }
}

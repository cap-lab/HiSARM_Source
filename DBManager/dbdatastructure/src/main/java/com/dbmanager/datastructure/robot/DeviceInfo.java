package com.dbmanager.datastructure.robot;

import java.util.List;
import java.util.Map;

public class DeviceInfo {
    private String device;
    private Map<ConnectionType, List<CommunicationAddress>> communicationInfoList;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Map<ConnectionType, List<CommunicationAddress>> getCommunicationInfoList() {
        return communicationInfoList;
    }

    public void setCommunicationInfoList(
            Map<ConnectionType, List<CommunicationAddress>> communicationInfoList) {
        this.communicationInfoList = communicationInfoList;
    }
}

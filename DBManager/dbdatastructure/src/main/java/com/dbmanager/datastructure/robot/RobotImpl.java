package com.dbmanager.datastructure.robot;

import java.util.List;

public class RobotImpl {
    private String robotId;
    private int robotIndex;
    private String robotClass;
    private List<DeviceInfo> deviceList;

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public int getRobotIndex() {
        return robotIndex;
    }

    public void setRobotIndex(int robotIndex) {
        this.robotIndex = robotIndex;
    }

    public String getRobotClass() {
        return robotClass;
    }

    public void setRobotClass(String robotClass) {
        this.robotClass = robotClass;
    }

    public List<DeviceInfo> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceInfo> deviceList) {
        this.deviceList = deviceList;
    }
}

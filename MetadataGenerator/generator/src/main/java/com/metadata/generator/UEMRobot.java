package com.metadata.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.metadata.generator.algorithm.task.UEMRobotTask;
import com.metadata.generator.architecture.UEMArchitectureDevice;

public class UEMRobot {
    UEMRobotTask robotTask;
    List<UEMArchitectureDevice> deviceList = new ArrayList<>();

    public UEMRobotTask getRobotTask() {
        return robotTask;
    }

    public void setRobotTask(UEMRobotTask robotTask) {
        this.robotTask = robotTask;
    }

    public List<UEMArchitectureDevice> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<UEMArchitectureDevice> deviceList) {
        this.deviceList = deviceList;
    }

    public UEMArchitectureDevice getDevice(String deviceName) throws Exception {
        Optional<UEMArchitectureDevice> device = getDeviceList().stream()
                .filter(d -> d.getDeviceName().equals(deviceName)).findAny();
        if (device.isPresent()) {
            return device.get();
        } else {
            throw new Exception("No device whose name is " + deviceName + " in the robot "
                    + robotTask.getName());
        }
    }

}

package com.metadata.generator.mapping;

import java.util.List;
import com.metadata.generator.UEMRobot;
import com.metadata.generator.algorithm.UEMTaskGraph;
import com.metadata.generator.algorithm.task.UEMActionTask;
import com.metadata.generator.algorithm.task.UEMTask;
import com.metadata.generator.architecture.UEMArchitectureDevice;
import com.metadata.generator.architecture.UEMArchitectureElementType;
import com.metadata.generator.constant.AlgorithmConstant;
import com.metadata.generator.constant.MappingConstant;
import hopes.cic.exception.CICXMLException;
import hopes.cic.xml.HardwarePlatformType;
import hopes.cic.xml.YesNoType;
import hopes.cic.xml.handler.CICMappingXMLHandler;

public class MappingGenerator {
    private static CICMappingXMLHandler handler = new CICMappingXMLHandler();
    private static UEMMapping mapping = new UEMMapping();

    public static void generate(List<UEMRobot> robotList) {
        try {
            for (UEMRobot robot : robotList) {
                for (UEMActionTask actionTask : robot.getRobotTask().getActionTaskList()) {
                    for (UEMTaskGraph taskGraph : actionTask.getSubTaskGraphs()) {
                        for (UEMTask task : taskGraph.getTaskList()) {
                            if (task.getHasSubGraph().equals(AlgorithmConstant.NO)) {
                                mapping.getTask().add(mappingTaskToDevice(task, robot));
                            }
                        }
                    }
                }
                for (String group : robot.getRobotTask().getRobot().getGroupList()) {
                    if (mapping.getMulticast(group) == null) {
                        UEMMappingMulticast multicast = new UEMMappingMulticast();
                        multicast.setGroupName(group);
                        multicast.setConnectionType(new UEMMappingMulticastConnection());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static UEMMappingTask mappingTaskToDevice(UEMTask task, UEMRobot robot)
            throws Exception {
        UEMMappingTask mapping = new UEMMappingTask();
        mapping.setName(task.getName());
        UEMMappingDevice mappingDevice = new UEMMappingDevice();
        UEMArchitectureDevice device = null;
        if (task.getIsHardwareDependent().equals(YesNoType.NO)) {
            device = robot.getDevice(robot.getRobotTask().getRobot().getRobotType().getRobotType()
                    .getPrimaryArchitecture());
        } else {
            for (UEMArchitectureDevice d : robot.getDeviceList()) {
                HardwarePlatformType hwInfo = task.getHardwareDependency().getHardware().get(0);
                if (hwInfo.getArchitecture().equals(d.getArchitecture())) {
                    device = d;
                    break;
                }
            }
        }
        mappingDevice.setName(device.getArchitecture());
        UEMMappingProcessor mappingProcessor = new UEMMappingProcessor();
        mappingProcessor.setPool(device.getElements().getElement().stream()
                .filter(e -> ((UEMArchitectureElementType) e).isProcessor()).findFirst().get()
                .getName());
        mappingProcessor.setLocalId(MappingConstant.DEFAULT_LOCAL_ID);
        mappingDevice.getProcessor().add(mappingProcessor);
        mapping.getDevice().add(mappingDevice);
        return mapping;
    }

    public String getXMLString() {
        try {
            return handler.getXMLString();
        } catch (CICXMLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

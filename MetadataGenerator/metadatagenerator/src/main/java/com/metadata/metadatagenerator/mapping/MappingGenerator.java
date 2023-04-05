package com.metadata.metadatagenerator.mapping;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.metadata.UEMRobot;
import com.metadata.algorithm.UEMAlgorithm;
import com.metadata.algorithm.UEMMulticastGroup;
import com.metadata.algorithm.UEMTaskGraph;
import com.metadata.algorithm.task.UEMActionTask;
import com.metadata.algorithm.task.UEMResourceTask;
import com.metadata.algorithm.task.UEMTask;
import com.metadata.architecture.UEMArchitectureDevice;
import com.metadata.architecture.UEMArchitectureElementType;
import com.metadata.constant.AlgorithmConstant;
import com.metadata.constant.MappingConstant;
import com.metadata.mapping.UEMMapping;
import com.metadata.mapping.UEMMappingDevice;
import com.metadata.mapping.UEMMappingProcessor;
import com.metadata.mapping.UEMMappingTask;
import com.metadata.metadatagenerator.constant.MetadataConstant;
import hopes.cic.exception.CICXMLException;
import hopes.cic.xml.HardwarePlatformType;
import hopes.cic.xml.MulticastGroupType;
import hopes.cic.xml.YesNoType;
import hopes.cic.xml.handler.CICMappingXMLHandler;

public class MappingGenerator {
    private UEMMapping mapping = new UEMMapping();

    private List<UEMMappingTask> makeSubGraphTask(List<UEMTaskGraph> taskGraphList, UEMRobot robot)
            throws Exception {
        List<UEMMappingTask> taskList = new ArrayList<>();
        for (UEMTaskGraph taskGraph : taskGraphList) {
            for (UEMTask task : taskGraph.getTaskList()) {
                if (task.getHasSubGraph().equals(AlgorithmConstant.NO)) {
                    mapping.getTask().add(mappingTaskToDevice(task, robot));
                }
            }
        }
        return taskList;
    }

    public void generate(List<UEMRobot> robotList, UEMAlgorithm algorithm) {
        try {
            for (UEMRobot robot : robotList) {
                for (UEMActionTask actionTask : robot.getRobotTask().getActionTaskList()) {
                    if (actionTask.getHasSubGraph().equals(AlgorithmConstant.NO)) {
                        mapping.getTask().add(mappingTaskToDevice(actionTask, robot));
                    } else {
                        mapping.getTask()
                                .addAll(makeSubGraphTask(actionTask.getSubTaskGraphs(), robot));
                    }
                }
                for (UEMResourceTask resourceTask : robot.getRobotTask().getResourceTaskList()) {
                    if (resourceTask.getHasSubGraph().equals(AlgorithmConstant.NO)) {
                        mapping.getTask().add(mappingTaskToDevice(resourceTask, robot));
                    } else {
                        mapping.getTask()
                                .addAll(makeSubGraphTask(resourceTask.getSubTaskGraphs(), robot));
                    }
                }
                mapping.getTask()
                        .add(mappingTaskToDevice(robot.getRobotTask().getListenTask(), robot));
                mapping.getTask()
                        .add(mappingTaskToDevice(robot.getRobotTask().getReportTask(), robot));
                mapping.getTask()
                        .add(mappingTaskToDevice(robot.getRobotTask().getControlTask(), robot));
                mapping.getTask()
                        .add(mappingTaskToDevice(robot.getRobotTask().getLeaderTask(), robot));
                for (MulticastGroupType multicast : algorithm.getAlgorithm().getMulticastGroups()
                        .getMulticastGroup()) {
                    UEMMulticastGroup multicastGroup = (UEMMulticastGroup) multicast;
                    if (multicastGroup.isExport()) {
                        mapping.addMulticast(multicast.getGroupName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UEMMappingTask mappingTaskToDevice(UEMTask task, UEMRobot robot) throws Exception {
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
                if (hwInfo.getPlatform().equals(d.getPlatform())) {
                    device = d;
                    break;
                }
            }
        }
        mappingDevice.setName(device.getName());
        UEMMappingProcessor mappingProcessor = new UEMMappingProcessor();
        mappingProcessor.setPool(device.getElements().getElement().stream()
                .filter(e -> ((UEMArchitectureElementType) e).isProcessor()).findFirst().get()
                .getName());
        mappingProcessor.setLocalId(MappingConstant.DEFAULT_LOCAL_ID);
        mappingDevice.getProcessor().add(mappingProcessor);
        mapping.getDevice().add(mappingDevice);
        return mapping;
    }

    public boolean generateMappingXML(Path rootDirectory, String projectName) {
        try {
            Path filePath = Paths.get(rootDirectory.toString(),
                    projectName + MetadataConstant.MAPPING_SUFFIX);
            CICMappingXMLHandler handler = new CICMappingXMLHandler();
            handler.setMapping(mapping);
            handler.storeXMLString(filePath.toString());
            return true;
        } catch (CICXMLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

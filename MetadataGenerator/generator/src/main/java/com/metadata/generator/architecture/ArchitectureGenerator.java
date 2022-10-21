package com.metadata.generator.architecture;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.architecture.Architecture;
import com.dbmanager.datastructure.architecture.Processor;
import com.dbmanager.datastructure.robot.CommunicationAddress;
import com.dbmanager.datastructure.robot.ConnectionType;
import com.dbmanager.datastructure.robot.IPBasedAddress;
import com.dbmanager.datastructure.robot.PortBasedAddress;
import com.metadata.generator.UEMRobot;
import com.metadata.generator.algorithm.UEMAlgorithm;
import com.metadata.generator.algorithm.task.UEMRobotTask;
import com.metadata.generator.constant.MetadataConstant;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import hopes.cic.exception.CICXMLException;
import hopes.cic.xml.ArchitectureElementCategoryType;
import hopes.cic.xml.ModuleListType;
import hopes.cic.xml.ModuleType;
import hopes.cic.xml.handler.CICArchitectureXMLHandler;

public class ArchitectureGenerator {
    private UEMArchitecture architecture = new UEMArchitecture();

    public List<UEMRobot> generate(MissionWrapper mission, AdditionalInfo additionalInfo,
            UEMAlgorithm algorithm) {
        try {
            List<UEMRobot> robotList = new ArrayList<>();
            for (UEMRobotTask robotTask : algorithm.getRobotTaskList()) {
                UEMRobot robot = new UEMRobot();
                robotList.add(robot);
                robot.setRobotTask(robotTask);
                makeTarget(robot);
                makeElementType(robot);
                makeEachRobotArchitecture(robot);
                makeIntraRobotConnection(robot);
            }
            makeInterRobotConnection(algorithm, robotList);
            return robotList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void makeTarget(UEMRobot robot) {
        for (Architecture device : robot.getRobotTask().getRobot().getRobotType().getDeviceList()) {
            architecture.setTarget(device.getDeviceName());
            break;
        }
    }

    private void makeElementType(UEMRobot robot) {
        for (Architecture device : robot.getRobotTask().getRobot().getRobotType().getDeviceList()) {
            makeProcessorElement(device);
            makeMemoryElement(device);
        }
    }

    private void makeProcessorElement(Architecture device) {
        for (Processor processor : device.getProcessors()) {
            if (!architecture.getElementTypes().getElementType().stream()
                    .filter(e -> e.getCategory().equals(ArchitectureElementCategoryType.PROCESSOR))
                    .anyMatch(e -> e.getName().equals(processor.getProcessorName().toString()))) {
                UEMArchitectureElementTypeType element = new UEMArchitectureElementTypeType(true);
                element.setName(processor.getProcessorName().getValue());
                element.setModel(processor.getProcessorName().getValue());
                element.setClock(processor.getClockFrequency());
                element.setSubcategory(processor.getType().toString());
                architecture.getElementTypes().getElementType().add(element);
            }
        }
    }

    private void makeMemoryElement(Architecture device) {
        if (!architecture.getElementTypes().getElementType().stream()
                .filter(e -> e.getCategory().equals(ArchitectureElementCategoryType.MEMORY))
                .anyMatch(e -> e.getSlavePort().get(0).getSize().intValue() == device.getMemory()
                        .getSize()
                        && e.getSlavePort().get(0).getMetric().value()
                                .equals(device.getMemory().getUnit()))) {
            UEMArchitectureElementTypeType element = new UEMArchitectureElementTypeType(false);
            element.setName(device.getMemory().getValue());
            element.setMemory(device.getMemory().getSize(), device.getMemory().getUnit());
            architecture.getElementTypes().getElementType().add(element);
        }
    }

    private void setDeviceAttribute(UEMArchitectureDevice device, String namePrefix,
            Architecture deviceInfo) {
        device.setPlatform(deviceInfo.getSWPlatform().getValue());
        device.setArchitecture(deviceInfo.getCPU().getValue());
        device.setName(namePrefix, deviceInfo.getDeviceName());
    }

    private void setDeviceProcessor(UEMArchitectureDevice device, Architecture deviceInfo) {
        for (Processor processor : deviceInfo.getProcessors()) {
            device.getElements().getElement()
                    .add(new UEMArchitectureElementType(processor.getNumberOfCores(),
                            processor.getProcessorName().getValue(),
                            device.getName() + "_" + processor.getProcessorName().getValue()));
        }
    }

    private void setDeviceMemory(UEMArchitectureDevice device, Architecture deviceInfo) {
        device.getElements().getElement().add(new UEMArchitectureElementType(
                deviceInfo.getMemory().getValue(), device.getName() + "_" + "SHARED_MEMORY"));
    }

    private void setDeviceElement(UEMArchitectureDevice device, Architecture deviceInfo) {
        setDeviceProcessor(device, deviceInfo);
        setDeviceMemory(device, deviceInfo);
    }

    private void setModuleList(UEMArchitectureDevice device, Architecture deviceInfo) {
        if (device.getModules() == null) {
            device.setModules(new ModuleListType());
        }
        deviceInfo.getModuleList().forEach(m -> {
            ModuleType module = new ModuleType();
            module.setName(m);
            device.getModules().getModule().add(module);
        });
    }


    private void setDeviceEnvironmentVariable(UEMArchitectureDevice device,
            Architecture deviceInfo) {
        deviceInfo.getEnvironmentVariableList().forEach(e -> {
            device.getEnvironmentVariables().getVariable()
                    .add(new UEMEnvironmentVariable(e.getName(), e.getValue()));
        });
    }

    public void makeEachRobotArchitecture(UEMRobot robot) {
        RobotImplWrapper robotImpl = robot.getRobotTask().getRobot();
        for (Architecture robotArchitecture : robotImpl.getRobotType().getDeviceList()) {
            UEMArchitectureDevice architectureDevice = new UEMArchitectureDevice();
            setDeviceAttribute(architectureDevice, robotImpl.getRobot().getRobotId(),
                    robotArchitecture);
            setDeviceElement(architectureDevice, robotArchitecture);
            setModuleList(architectureDevice, robotArchitecture);
            setDeviceEnvironmentVariable(architectureDevice, robotArchitecture);
            architecture.getDevices().getDevice().add(architectureDevice);
            robot.getDeviceList().add(architectureDevice);
        }
    }

    private UEMTCPConnection makeTCPConnection(IPBasedAddress address, boolean isServer) {
        UEMTCPConnection connection = new UEMTCPConnection();

        connection.setIp((String) address.getAddress().get(IPBasedAddress.IP));
        connection.setPort((Integer) address.getAddress().get(IPBasedAddress.PORT));
        connection.setName(isServer);
        connection.setRole(isServer);

        return connection;
    }

    private UEMSerialConnection makePortConnection(PortBasedAddress address,
            boolean isMaster) {
        UEMSerialConnection connection = new UEMSerialConnection();

        connection.setName(isMaster);
        connection.setRole(isMaster);
        if (address != null) {
            connection.setPortAddress((String) address.getAddress().get(PortBasedAddress.PORT));
        }

        return connection;
    }

    private void setDeviceConnection(ConnectionType type, UEMArchitectureDevice targetDevice,
            CommunicationAddress address, boolean isServer_Master) {
        if (targetDevice.hasConnection(type, isServer_Master)) {
            return;
        }
        switch (type) {
            case ETHERNET_WIFI:
                targetDevice.addTCPConnection(
                        makeTCPConnection((IPBasedAddress) address, isServer_Master));
            case USB:
                targetDevice.addUSBConnection(
                        makePortConnection((PortBasedAddress) address, isServer_Master));
        }
    }

    private void setConnection(String masterDevice, String masterConnection,
            String slaveDevice, String slaveConnection) {
        UEMArchitectureConnection connection =
                architecture.getConnection(masterDevice, masterConnection);
        if (connection == null) {
            connection = new UEMArchitectureConnection();
            connection.setMaster(masterDevice, masterConnection);
            architecture.getConnections().getConnection().add(connection);
        }
        if (connection.getSlave(slaveDevice, slaveConnection) == null) {
            connection.setSlave(slaveDevice, slaveConnection);
        }
    }

    private void makeIntraRobotConnection(UEMRobot robot) throws Exception {
        RobotImplWrapper robotImpl = robot.getRobotTask().getRobot();
        if (robotImpl.getRobotType().getRobotType().getArchitectureList().size() > 1) {
            String primaryDevice = robotImpl.getRobotType().getRobotType().getPrimaryArchitecture();
            PortBasedAddress address = (PortBasedAddress) robotImpl.getRobot()
                    .getCommunicationInfoMap().get(ConnectionType.USB);
            for (String device : robotImpl.getRobotType().getRobotType().getArchitectureList()) {
                if (device.equals(primaryDevice)) {
                    setDeviceConnection(ConnectionType.USB, robot.getDevice(device), address, true);
                } else {
                    setDeviceConnection(ConnectionType.USB, robot.getDevice(device), address,
                            false);
                    setConnection(primaryDevice, UEMSerialConnection.makeName(true), device,
                            UEMSerialConnection.makeName(false));
                }
            }
        }
    }

    private UEMArchitectureDevice findRobotPrimaryArchitecture(List<UEMRobot> robotList,
            String robotId) throws Exception {
        for (UEMRobot robot : robotList) {
            if (robot.getRobotTask().getName().equals(robotId)) {
                return robot.getDevice(robot.getRobotTask().getRobot().getRobotType().getRobotType()
                        .getPrimaryArchitecture());
            }
        }
        return null;
    }

    private void makeChannelConnection(UEMAlgorithm algorithm, List<UEMRobot> robotList)
            throws Exception {
        for (UEMRobotTask src : algorithm.getRobotConnectionMap().keySet()) {
            UEMArchitectureDevice srcDevice =
                    findRobotPrimaryArchitecture(robotList, src.getName());
            IPBasedAddress address = (IPBasedAddress) src.getRobot().getRobot()
                    .getCommunicationInfoMap().get(ConnectionType.ETHERNET_WIFI);
            setDeviceConnection(ConnectionType.ETHERNET_WIFI, srcDevice, address, true);
            for (UEMRobotTask dst : algorithm.getRobotConnectionMap().get(src)) {
                UEMArchitectureDevice dstDevice =
                        findRobotPrimaryArchitecture(robotList, dst.getName());
                setDeviceConnection(ConnectionType.ETHERNET_WIFI, dstDevice, address, false);
                setConnection(srcDevice.getDeviceName(), UEMTCPConnection.makeName(true),
                        dstDevice.getName(), UEMTCPConnection.makeName(false));
            }
        }
    }

    private void makeInterRobotConnection(UEMAlgorithm algorithm, List<UEMRobot> robotList)
            throws Exception {
        makeChannelConnection(algorithm, robotList);
        for (UEMRobot robot : robotList) {
            robot.getDevice(robot.getRobotTask().getRobot().getRobotType().getRobotType()
                    .getPrimaryArchitecture()).addUDPConnection(new UEMUDPConnection());
        }
    }

    public boolean generateArchitectureXML(Path rootDirectory, String projectName) {
        try {
            Path filePath = Paths.get(rootDirectory.toString(),
                    projectName + MetadataConstant.ARCHITECTURE_SUFFIX);
            CICArchitectureXMLHandler handler = new CICArchitectureXMLHandler();
            handler.setArchitecture(architecture);
            handler.storeXMLString(filePath.toString());
            return true;
        } catch (CICXMLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

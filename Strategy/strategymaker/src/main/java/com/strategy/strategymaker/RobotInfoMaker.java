package com.strategy.strategymaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.dbmanager.commonlibraries.DBService;
import com.dbmanager.datastructure.architecture.Architecture;
import com.dbmanager.datastructure.robot.RobotImpl;
import com.dbmanager.datastructure.simulationdevice.SimulationDevice;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.RobotWrapper;
import com.scriptparser.parserdatastructure.wrapper.TeamWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import com.strategy.strategydatastructure.additionalinfo.ClientInfo;
import com.strategy.strategydatastructure.additionalinfo.SimulationRobot;
import com.strategy.strategydatastructure.constant.AdditionalInfoConstant;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.wrapper.RobotTypeWrapper;

public class RobotInfoMaker {
    private static List<RobotTypeWrapper> robotTypeList = new ArrayList<>();
    private static Set<Architecture> architectureStore = new HashSet<>();

    public static List<RobotImplWrapper> makeRobotImplList(MissionWrapper mission,
            AdditionalInfo additionalInfo) {
        if (additionalInfo.getEnvironment().equals(AdditionalInfoConstant.SIMULATION)) {
            return allocateSimulationRobot(mission, additionalInfo);
        } else if (additionalInfo.getEnvironment().equals(AdditionalInfoConstant.DEPLOYMENT)) {
            return allocateRealRobot(makeRobotMap(mission),
                    makeRobotImplList(additionalInfo.getRobotList()));
        } else {
            throw new RuntimeException("Unknown environment type ("
                    + additionalInfo.getEnvironment() + ") in AdditionalInfo");
        }
    }

    private static List<RobotImplWrapper> allocateSimulationMethodEvenlyDistribute(
            MissionWrapper mission,
            AdditionalInfo additionalInfo) {
        List<RobotImplWrapper> robotImplList = new ArrayList<>();
        int numOfRobot = 0;
        for (TeamWrapper team : mission.getTeamList()) {
            for (RobotWrapper robot : team.getRobotList()) {
                numOfRobot += robot.getRobot().getCount();
            }
        }
        int teamIndex = 0;
        int numOfDevice = additionalInfo.getSimulationClient().getClientList().size();
        int numOfRobotPerDevice = numOfRobot / numOfDevice;
        int robotIndex = 0;
        int clientIndex = 0;
        for (TeamWrapper team : mission.getTeamList()) {
            for (RobotWrapper robot : team.getRobotList()) {
                for (int i = 0; i < robot.getRobot().getCount(); i++) {
                    String clientId = additionalInfo.getSimulationClient().getClientList()
                            .get(clientIndex).getId();
                    SimulationDevice device = DBService.getSimulationDevice(clientId);
                    RobotImpl robotImpl = new RobotImpl();
                    robotImpl.setRobotId(robot.getRobot().getType() + "_" + robotIndex);
                    robotImpl.setRobotClass(robot.getRobot().getType());
                    robotImpl.setCommunicationInfoMap(device.getCommunicationInfoMap());
                    RobotImplWrapper robotImplWrapper = new RobotImplWrapper();
                    robotImplWrapper.setRobot(robotImpl);
                    robotImplWrapper.setRobotIndex(robotIndex);
                    robotImplWrapper
                            .setRobotType(getSimRobotType(robot.getRobot().getType(), device));
                    robotImplWrapper.addTeam(team.getTeam().getName(), teamIndex);
                    robotImplList.add(robotImplWrapper);
                    robotIndex++;
                    if (robotIndex % numOfRobotPerDevice == 0) {
                        clientIndex++;
                        clientIndex = clientIndex % numOfDevice;
                    }
                }
            }
            teamIndex++;
        }
        return robotImplList;
    }

    private static List<RobotImplWrapper> allocateSimulationMethodDesignation(
            MissionWrapper mission, AdditionalInfo additionalInfo) {
        List<RobotImplWrapper> robotImplList = new ArrayList<>();
        int robotIndex = 0;
        int teamIndex = 0;
        boolean assign;
        for (TeamWrapper team : mission.getTeamList()) {
            for (RobotWrapper robot : team.getRobotList()) {
                for (int i = 0; i < robot.getRobot().getCount(); i++) {
                    assign = false;
                    for (ClientInfo client : additionalInfo.getSimulationClient().getClientList()) {
                        for (SimulationRobot robotType : client.getRobotMappingInfo()) {
                            if (robot.getRobot().getType().equals(robotType.getType())
                                    && robotType.getNum() > 0) {
                                assign = true;
                                SimulationDevice device =
                                        DBService.getSimulationDevice(client.getId());
                                RobotImpl robotImpl = new RobotImpl();
                                robotImpl.setRobotId(robot.getRobot().getType() + "_" + robotIndex);
                                robotImpl.setRobotClass(robot.getRobot().getType());
                                robotImpl.setCommunicationInfoMap(device.getCommunicationInfoMap());
                                RobotImplWrapper robotImplWrapper = new RobotImplWrapper();
                                robotImplWrapper.setRobot(robotImpl);
                                robotImplWrapper.setRobotType(
                                        getSimRobotType(robot.getRobot().getType(), device));
                                robotImplWrapper.addTeam(team.getTeam().getName(), teamIndex);
                                robotImplList.add(robotImplWrapper);
                                robotType.setNum(robotType.getNum() - 1);
                                break;
                            }
                        }
                        if (assign) {
                            break;
                        }
                    }
                    if (!assign) {
                        throw new RuntimeException(
                                "Cannot find assign the robot " + robot.getRobot().getType());
                    }
                    robotIndex++;
                }
            }
            teamIndex++;
        }
        return robotImplList;
    }

    private static List<RobotImplWrapper> allocateSimulationRobot(MissionWrapper mission,
            AdditionalInfo additionalInfo) {
        if (additionalInfo.getSimulationClient().getAllocationMethod()
                .equals(AdditionalInfoConstant.SIMULATION_ALLOCATION_METHOD_DISTRIBUTE)) {
            return allocateSimulationMethodEvenlyDistribute(mission, additionalInfo);
        } else if (additionalInfo.getSimulationClient().getAllocationMethod()
                .equals(AdditionalInfoConstant.SIMULATION_ALLOCATION_METHOD_DESIGNATION)) {
            return allocateSimulationMethodDesignation(mission, additionalInfo);
        } else {
            throw new RuntimeException("Unknown allocation method ("
                    + additionalInfo.getSimulationClient().getAllocationMethod()
                    + ") in AdditionalInfo");
        }
    }

    private static Architecture getArchitecture(String architectureName) {
        for (Architecture architecture : architectureStore) {
            if (architecture.getDeviceName().equals(architectureName)) {
                return architecture;
            }
        }
        return null;
    }

    private static boolean containArchitecture(String architectureName) {
        if (getArchitecture(architectureName) == null) {
            return false;
        } else {
            return true;
        }
    }

    private static RobotTypeWrapper getSimRobotType(String type, SimulationDevice device) {
        for (RobotTypeWrapper t : robotTypeList) {
            if (t.getRobotType().getRobotClass().equals(type)
                    && t.getDeviceList().get(0).getDeviceName().equals(device.getDeviceId())) {
                return t;
            }
        }
        RobotTypeWrapper newType = new RobotTypeWrapper();
        newType.setRobotType(DBService.getRobot(type));
        if (containArchitecture(device.getDeviceId())) {
            newType.getDeviceList().add(getArchitecture(device.getDeviceId()));
        } else {
            Architecture newArchitecture = DBService.getArchitecture(device.getArchitecture());
            newArchitecture.setDeviceName(device.getDeviceId());
            newType.getDeviceList().add(newArchitecture);
            architectureStore.add(newArchitecture);
        }
        return newType;
    }

    private static RobotTypeWrapper getRobotType(String type, String robotId) {
        for (RobotTypeWrapper t : robotTypeList) {
            if (t.getRobotType().getRobotClass().equals(type)) {
                return t;
            }
        }
        RobotTypeWrapper newType = new RobotTypeWrapper();
        newType.setRobotType(DBService.getRobot(type));
        for (String architectureName : newType.getRobotType().getArchitectureList()) {
            if (containArchitecture(architectureName)) {
                newType.getDeviceList().add(getArchitecture(architectureName));
            } else {
                Architecture architecture = DBService.getArchitecture(architectureName);
                architecture.setDeviceName(architectureName + "_" + robotId);
                newType.getDeviceList().add(architecture);
                architectureStore.add(architecture);
            }
        }

        return newType;
    }

    private static List<RobotImplWrapper> allocateRealRobot(
            Map<String, List<RobotWrapper>> robotList, List<RobotImpl> robotCandidateList) {
        List<RobotImplWrapper> robotImplList = new ArrayList<>();
        int robotIndex = 0;
        int teamIndex = 0;
        for (String teamName : robotList.keySet()) {
            for (RobotWrapper robot : robotList.get(teamName)) {
                for (int i = 0; i < robot.getRobot().getCount(); i++) {
                    RobotImplWrapper robotImpl = new RobotImplWrapper();
                    robotImpl.setRobotIndex(robotIndex);
                    robotImpl.addTeam(teamName, teamIndex);
                    for (int index = 0; index < robotCandidateList.size(); index++) {
                        if (robotCandidateList.get(index).getRobotClass()
                                .equals(robot.getRobot().getType())) {
                            robotImpl.setRobot(robotCandidateList.get(index));
                            robotCandidateList.remove(index);
                            break;
                        }
                    }
                    robotImpl.setRobotType(getRobotType(robot.getRobot().getType(),
                            robotImpl.getRobot().getRobotId()));
                    robotImplList.add(robotImpl);
                    robotIndex = robotIndex + 1;
                }
            }
            teamIndex++;
        }
        return robotImplList;
    }

    private static List<RobotImpl> makeRobotImplList(List<String> robotIdList) {
        List<RobotImpl> robotCandidateList = new ArrayList<>();
        for (String robotId : robotIdList) {
            robotCandidateList.add(DBService.getRobotImpl(robotId));
        }
        return robotCandidateList;
    }

    private static Map<String, List<RobotWrapper>> makeRobotMap(MissionWrapper mission) {
        Map<String, List<RobotWrapper>> result = new HashMap<>();
        for (TeamWrapper team : mission.getTeamList()) {
            result.put(team.getTeam().getName(), team.getRobotList());
        }
        return result;
    }
}

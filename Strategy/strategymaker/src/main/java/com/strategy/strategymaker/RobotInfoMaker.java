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
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.RobotWrapper;
import com.scriptparser.parserdatastructure.wrapper.TeamWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.wrapper.RobotTypeWrapper;
import com.strategy.strategymaker.additionalinfo.AdditionalInfo;

public class RobotInfoMaker {
    private static List<RobotTypeWrapper> robotTypeList = new ArrayList<>();
    private static Set<Architecture> architectureStore = new HashSet<>();

    public static List<RobotImplWrapper> makeRobotImplList(MissionWrapper mission,
            AdditionalInfo additionalInfo) {
        return allocateRealRobot(makeRobotMap(mission),
                makeRobotImplList(additionalInfo.getRobotList()));
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

    private static RobotTypeWrapper getRobotType(String type) {
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
        for (String teamName : robotList.keySet()) {
            for (RobotWrapper robot : robotList.get(teamName)) {
                for (int i = 0; i < robot.getRobot().getCount(); i++) {
                    RobotImplWrapper robotImpl = new RobotImplWrapper();
                    robotImpl.setRobotIndex(robotIndex);
                    robotImpl.addTeam(teamName);
                    robotImpl.setRobotType(getRobotType(robot.getRobot().getType()));
                    for (int index = 0; index < robotCandidateList.size(); index++) {
                        if (robotCandidateList.get(index).getRobotClass()
                                .equals(robot.getRobot().getType())) {
                            robotImpl.setRobot(robotCandidateList.get(index));
                            robotCandidateList.remove(index);
                            break;
                        }
                    }
                    robotImplList.add(robotImpl);
                    robotIndex = robotIndex + 1;
                }
            }
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

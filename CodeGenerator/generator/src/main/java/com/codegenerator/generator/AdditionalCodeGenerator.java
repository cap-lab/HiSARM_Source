package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codegenerator.constant.GroupActionTaskConstant;
import com.codegenerator.constant.GroupCodeConstant;
import com.codegenerator.constant.GroupingTaskConstant;
import com.codegenerator.constant.LeaderTaskConstant;
import com.codegenerator.constant.SimulationTaskConstant;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.codegenerator.generator.util.LocalFileCopier;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.metadata.algorithm.library.UEMGroupingLibrary;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;

public class AdditionalCodeGenerator {
    public void generateAdditionalCode(Path targetDir, AdditionalInfo additionalInfo,
            List<CodeRobotWrapper> robotList, MissionWrapper mission) {
        try {
            copyGroupCode(targetDir);
            for (CodeRobotWrapper robot : robotList) {
                generateGroupActionLibraryTaskCode(targetDir, robot, robotList.size());
            }
            for (CodeRobotWrapper robot : robotList) {
                copyLeaderFiles(robot, targetDir);
                generateLeaderLibraryCode(targetDir, robot, robotList.size());
            }
            for (CodeRobotWrapper robot : robotList) {
                generateGroupingLibraryTaskCode(targetDir, robot, robotList.size());
            }
            for (CodeRobotWrapper robot : robotList) {
                generateTeamCode(targetDir, robot, mission);
        }

        if (additionalInfo.getEnvironment().equals("simulation")) {
            for (CodeRobotWrapper robot : robotList) {
                generateSimulationCode(targetDir, robot, additionalInfo);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void copyGroupCode(Path targetDir) throws Exception {
    LocalFileCopier.copyFile(CodeGeneratorConstant.GROUP_HEADER_PATH,
            Paths.get(targetDir.toString(), CodeGeneratorConstant.GROUP_HEADER));
    LocalFileCopier.copyFile(CodeGeneratorConstant.GROUP_SOURCE_PATH,
            Paths.get(targetDir.toString(), CodeGeneratorConstant.GROUP_SOURCE));
    }

    public void generateGroupActionLibraryTaskCode(Path targetDir, CodeRobotWrapper robot,
            int size) {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(GroupActionTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(GroupActionTaskConstant.GROUP_ACTION_LIST,
                robot.getRobot().getRobotTask().getGroupActionTask().getGroupActionList());
        rootHash.put(GroupActionTaskConstant.NUM_OF_ROBOT, size);

        FTLHandler.getInstance()
                .generateCode(CodeGeneratorConstant.GROUP_ACTION_SOURCE_TEMPLATE,
                        Paths.get(targetDir.toString(),
                                robot.getRobot().getRobotTask().getGroupActionTask().getFile()),
                        rootHash);
    }

    public void generateLeaderLibraryCode(Path targetDir, CodeRobotWrapper robot, int robotNum)
            throws Exception {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(LeaderTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(LeaderTaskConstant.GROUP_LIST,
                new ArrayList<>(robot.getRobot().getRobotTask().getRobot().getGroupMap().keySet()));
        rootHash.put(LeaderTaskConstant.INITIAL_TEAM,
                robot.getRobot().getRobotTask().getRobot().getTeam());
        rootHash.put(LeaderTaskConstant.MAX_ROBOT_NUM, robotNum);
        rootHash.put(LeaderTaskConstant.SHARED_DATA_SIZE,
                robot.getRobot().getRobotTask().getLeaderLibraryTask().getSharedDataSize() > 4
                        ? robot.getRobot().getRobotTask().getLeaderLibraryTask().getSharedDataSize()
                        : 4);

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.LEADER_DATA_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.LEADER_DATA_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.LEADER_DATA_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.LEADER_DATA_SOURCE_SUFFIX),
                rootHash);
    }

    private void copyLeaderFiles(CodeRobotWrapper robot, Path targetDir) throws Exception {
        LocalFileCopier.copyFile(CodeGeneratorConstant.LEADER_HEADER_PATH,
                Paths.get(targetDir.toString(), CodeGeneratorConstant.LEADER_HEADER));
        LocalFileCopier.copyFile(CodeGeneratorConstant.LEADER_SOURCE_PATH,
                Paths.get(targetDir.toString(), CodeGeneratorConstant.LEADER_SOURCE));
        LocalFileCopier.copyFile(CodeGeneratorConstant.LEADER_LIBRARY_PATH,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.LEADER_LIBRARY_SUFFIX));
    }

    public void generateGroupingLibraryTaskCode(Path targetDir, CodeRobotWrapper robot,
            int robotNum) {
        Map<String, Object> rootHash = new HashMap<>();
        UEMGroupingLibrary groupingLib = robot.getRobot().getRobotTask().getGroupingLibrary();

        rootHash.put(GroupingTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(GroupingTaskConstant.GROUPING_LIBRARY, groupingLib);
        rootHash.put(GroupingTaskConstant.MAX_ROBOT_NUM, robotNum);
        rootHash.put(GroupingTaskConstant.SHARED_DATA_SIZE, groupingLib.getSharedDataSize());
        rootHash.put(GroupingTaskConstant.GROUPING_MODE_SET, groupingLib.getModeSet());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.GROUPING_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobot().getRobotTask().getGroupingLibrary().getFile()),
                rootHash);
    }

    private void generateTeamCode(Path targetDir, CodeRobotWrapper robot, MissionWrapper mission)
            throws Exception {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(GroupCodeConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(GroupCodeConstant.TEAM_LIST,
                mission.getTeamList().parallelStream().map(t -> t.getTeam().getName()).toArray());

        FTLHandler.getInstance()
                .generateCode(CodeGeneratorConstant.TEAM_HEADER_TEMPLATE,
                        Paths.get(targetDir.toString(),
                                robot.getRobotName() + CodeGeneratorConstant.TEAM_HEADER_SUFFIX),
                        rootHash);
    }


    private void generateSimulationCode(Path targetDir, CodeRobotWrapper robot,
            AdditionalInfo additionalInfo) {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(SimulationTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(SimulationTaskConstant.IP, additionalInfo.getEnvironmentInfo().get(0).getIp());
        rootHash.put(SimulationTaskConstant.PORT,
                additionalInfo.getEnvironmentInfo().get(0).getPort());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.SIMULATION_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.SIMULATION_HEADER_SUFFIX),
                rootHash);
    }
}

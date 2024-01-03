package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codegenerator.constant.CommunicationTaskConstant;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.codegenerator.generator.util.LocalFileCopier;
import com.metadata.UEMRobot;
import com.metadata.algorithm.task.UEMListenTask;
import com.metadata.algorithm.task.UEMReportTask;

public class CommunicationCodeGenerator {
    public void generateCommunicationCode(Path targetDir, List<UEMRobot> robotList) {
        try {
            copyPortCode(targetDir);
            copyCommunicationCode(targetDir);
            copyListenTaskCode(targetDir);
            copyReportTaskCode(targetDir);
            for (UEMRobot robot : robotList) {
                generateListenTaskCode(targetDir, robot, robotList.size());
                generateReportTaskCode(targetDir, robot, robotList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyPortCode(Path targetDir) throws Exception {
        LocalFileCopier.copyFile(CodeGeneratorConstant.PORT_HEADER_PATH,
                Paths.get(targetDir.toString(), CodeGeneratorConstant.PORT_HEADER));
    }

    private void copyCommunicationCode(Path targetDir) throws Exception {
        LocalFileCopier.copyFile(CodeGeneratorConstant.COMMUNICATION_HEADER_PATH,
                Paths.get(targetDir.toString(), CodeGeneratorConstant.COMMUNICATION_HEADER));
        LocalFileCopier.copyFile(CodeGeneratorConstant.COMMUNICATION_SOURCE_PATH,
                Paths.get(targetDir.toString(), CodeGeneratorConstant.COMMUNICATION_SOURCE));
    }

    private void copyListenTaskCode(Path targetDir) throws Exception {
        LocalFileCopier.copyFile(CodeGeneratorConstant.LISTEN_TASK_CODE_PATH,
                Paths.get(targetDir.toString(), CodeGeneratorConstant.LISTEN_TASK_CIC));
    }

    private void copyReportTaskCode(Path targetDir) throws Exception {
        LocalFileCopier.copyFile(CodeGeneratorConstant.REPORT_TASK_CODE_PATH,
                Paths.get(targetDir.toString(), CodeGeneratorConstant.REPORT_TASK_CIC));
    }

    private void generateListenTaskCode(Path targetDir, UEMRobot robot, int robotNum) {
        UEMListenTask task = robot.getRobotTask().getListenTask();
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(CommunicationTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(CommunicationTaskConstant.CHANNEL_PORT_MAP, task.getChannelPortMap());
        rootHash.put(CommunicationTaskConstant.MULTICAST_PORT_MAP, task.getMulticastPortMap());
        rootHash.put(CommunicationTaskConstant.SHARED_DATA_PORT_MAP, task.getSharedDataPortMap());
        rootHash.put(CommunicationTaskConstant.GROUP_ACTION_PORT_LIST,
                task.getGroupActionPortList());
        rootHash.put(CommunicationTaskConstant.LEADER_PORT_MAP, task.getLeaderPortMap());
        rootHash.put(CommunicationTaskConstant.LEADER_SHARED_DATA_SIZE,
                robot.getRobotTask().getLeaderLibraryTask().getSharedDataSize() > 4
                        ? robot.getRobotTask().getLeaderLibraryTask().getSharedDataSize()
                        : 4);
        rootHash.put(CommunicationTaskConstant.GROUPING_PORT_LIST, task.getGroupingPortList());
        rootHash.put(CommunicationTaskConstant.GROUPING_LIB_PORT, task.getGroupingPort());
        rootHash.put(CommunicationTaskConstant.MAX_ROBOT_NUM, robotNum);

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.LISTEN_PORT_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.LISTEN_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.LISTEN_PORT_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.LISTEN_SOURCE_SUFFIX),
                rootHash);
    }

    private void generateReportTaskCode(Path targetDir, UEMRobot robot, int robotNum) {
        UEMReportTask task = robot.getRobotTask().getReportTask();
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(CommunicationTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(CommunicationTaskConstant.CHANNEL_PORT_MAP, task.getChannelPortMap());
        rootHash.put(CommunicationTaskConstant.MULTICAST_PORT_MAP, task.getMulticastPortMap());
        rootHash.put(CommunicationTaskConstant.SHARED_DATA_PORT_MAP, task.getSharedDataPortMap());
        rootHash.put(CommunicationTaskConstant.GROUP_ACTION_PORT_LIST,
                task.getGroupActionPortList());
        rootHash.put(CommunicationTaskConstant.LEADER_PORT_MAP, task.getLeaderPortMap());
        rootHash.put(CommunicationTaskConstant.LEADER_SHARED_DATA_SIZE,
                robot.getRobotTask().getLeaderLibraryTask().getSharedDataSize() > 4
                        ? robot.getRobotTask().getLeaderLibraryTask().getSharedDataSize()
                        : 4);
        rootHash.put(CommunicationTaskConstant.GROUPING_PORT_LIST, task.getGroupingPortList());
        rootHash.put(CommunicationTaskConstant.GROUPING_LIB_PORT, task.getGroupingPort());
        rootHash.put(CommunicationTaskConstant.MAX_ROBOT_NUM, robotNum);

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.REPORT_PORT_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.REPORT_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.REPORT_PORT_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.REPORT_SOURCE_SUFFIX),
                rootHash);
    }

}

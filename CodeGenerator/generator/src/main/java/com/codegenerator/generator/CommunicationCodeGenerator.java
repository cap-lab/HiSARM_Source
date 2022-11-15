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
        for (UEMRobot robot : robotList) {
            generateListenTaskCode(targetDir, robot);
            generateReportTaskCode(targetDir, robot);
            copyPortCode(targetDir);
            copyCommunicationCode(targetDir);
        }
    }

    private void generateListenTaskCode(Path targetDir, UEMRobot robot) {
        UEMListenTask task = robot.getRobotTask().getListenTask();
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(CommunicationTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(CommunicationTaskConstant.CHANNEL_PORT_MAP, task.getChannelPortMap());
        rootHash.put(CommunicationTaskConstant.MULTICAST_PORT_MAP, task.getMulticastPortMap());
        rootHash.put(CommunicationTaskConstant.SHARED_DATA_PORT_MAP, task.getSharedDataPortMap());
        rootHash.put(CommunicationTaskConstant.LEADER_PORT_MAP, task.getLeaderPortMap());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.LISTEN_TASK_TEMPLATE,
                Paths.get(targetDir.toString(), task.getFile()), rootHash);
    }

    private void generateReportTaskCode(Path targetDir, UEMRobot robot) {
        UEMReportTask task = robot.getRobotTask().getReportTask();
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(CommunicationTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(CommunicationTaskConstant.CHANNEL_PORT_MAP, task.getChannelPortMap());
        rootHash.put(CommunicationTaskConstant.MULTICAST_PORT_MAP, task.getMulticastPortMap());
        rootHash.put(CommunicationTaskConstant.SHARED_DATA_PORT_MAP, task.getSharedDataPortMap());
        rootHash.put(CommunicationTaskConstant.LEADER_PORT_MAP, task.getLeaderPortMap());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.REPORT_TASK_TEMPLATE,
                Paths.get(targetDir.toString(), task.getFile()), rootHash);
    }

    private void copyPortCode(Path targetDir) {
        try {
            LocalFileCopier.copyFile(CodeGeneratorConstant.PORT_HEADER_CODE,
                    Paths.get(targetDir.toString(), CodeGeneratorConstant.PORT_HEADER));
            LocalFileCopier.copyFile(CodeGeneratorConstant.PORT_SOURCE_CODE,
                    Paths.get(targetDir.toString(), CodeGeneratorConstant.PORT_SOURCE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyCommunicationCode(Path targetDir) {
        try {
            LocalFileCopier.copyFile(CodeGeneratorConstant.COMMUNICATION_HEADER_CODE,
                    Paths.get(targetDir.toString(), CodeGeneratorConstant.COMMUNICATION_HEADER));
            LocalFileCopier.copyFile(CodeGeneratorConstant.COMMUNICATION_SOURCE_CODE,
                    Paths.get(targetDir.toString(), CodeGeneratorConstant.COMMUNICATION_SOURCE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

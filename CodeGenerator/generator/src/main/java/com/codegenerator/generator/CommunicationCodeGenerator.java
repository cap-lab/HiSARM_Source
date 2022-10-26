package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.codegenerator.generator.constant.CommunicationTaskConstant;
import com.metadata.UEMRobot;
import com.metadata.algorithm.task.UEMListenTask;
import com.metadata.algorithm.task.UEMReportTask;

public class CommunicationCodeGenerator {
    public void generateCommunicationCode(Path targetDir, List<UEMRobot> robotList) {
        for (UEMRobot robot : robotList) {
            generateListenTaskCode(targetDir, robot);
            generateReportTaskCode(targetDir, robot);
        }
    }

    private void generateListenTaskCode(Path targetDir, UEMRobot robot) {
        UEMListenTask task = robot.getRobotTask().getListenTask();
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(CommunicationTaskConstant.TASK_NAME, task.getName());
        rootHash.put(CommunicationTaskConstant.CHANNEL_PORT_MAP, task.getChannelPortMap());
        rootHash.put(CommunicationTaskConstant.MULTICAST_PORT_MAP, task.getMulticastPortMap());
        rootHash.put(CommunicationTaskConstant.SHARED_DATA_PORT_MAP, task.getSharedDataPortMap());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.LISTEN_TASK_TEMPLATE,
                Paths.get(targetDir.toString(), task.getFile()), rootHash);
    }

    private void generateReportTaskCode(Path targetDir, UEMRobot robot) {
        UEMReportTask task = robot.getRobotTask().getReportTask();
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(CommunicationTaskConstant.TASK_NAME, task.getName());
        rootHash.put(CommunicationTaskConstant.CHANNEL_PORT_MAP, task.getChannelPortMap());
        rootHash.put(CommunicationTaskConstant.MULTICAST_PORT_MAP, task.getMulticastPortMap());
        rootHash.put(CommunicationTaskConstant.SHARED_DATA_PORT_MAP, task.getSharedDataPortMap());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.REPORT_TASK_TEMPLATE,
                Paths.get(targetDir.toString(), task.getFile()), rootHash);
    }
}

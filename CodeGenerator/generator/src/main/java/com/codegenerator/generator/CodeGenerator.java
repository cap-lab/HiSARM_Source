package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codegenerator.generator.mapper.ModeServiceMapper;
import com.codegenerator.generator.mapper.RobotModeMapper;
import com.codegenerator.generator.mapper.RobotResourceMapper;
import com.codegenerator.generator.mapper.ServiceStatementMapper;
import com.codegenerator.generator.mapper.VariableMapper;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.metadata.UEMRobot;
import com.metadata.algorithm.UEMAlgorithm;
import com.metadata.algorithm.task.UEMTask;
import com.metadata.constant.AlgorithmConstant;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import hopes.cic.xml.TaskType;

public class CodeGenerator {

    private List<CodeRobotWrapper> makeDataStructure(MissionWrapper mission,
            List<UEMRobot> robotList) {
        RobotModeMapper rmMapper = new RobotModeMapper();
        List<CodeRobotWrapper> codeRobotList = rmMapper.mapRobotMode(mission, robotList);
        for (CodeRobotWrapper codeRobot : codeRobotList) {
            for (CodeModeWrapper codeMode : codeRobot.getModeList()) {
                ModeServiceMapper msMapper = new ModeServiceMapper();
                codeMode.setServiceList(msMapper.mapModeService(codeMode));
                codeRobot.getServiceList().addAll(codeMode.getServiceList());
                for (CodeServiceWrapper service : codeRobot.getServiceList()) {
                    ServiceStatementMapper ssMapper =
                            new ServiceStatementMapper(codeRobot, mission);
                    ssMapper.mapServiceStatement(service);
                }
            }
            RobotResourceMapper rrMaper = new RobotResourceMapper();
            rrMaper.mapResource(codeRobot);
        }
        VariableMapper vMapper = new VariableMapper();
        vMapper.mapVariable(codeRobotList);
        return codeRobotList;
    }

    private void setNewTaskId(UEMAlgorithm algorithm, List<UEMRobot> robotList) {
        Map<String, Integer> taskIdMap = new HashMap<>();
        for (UEMRobot robot : robotList) {
            String robotName = robot.getRobotName();
            if (!taskIdMap.containsKey(taskIdMap)) {
                taskIdMap.put(robotName, 0);
            }
        }
        for (TaskType task : algorithm.getAlgorithm().getTasks().getTask()) {
            UEMTask t = (UEMTask) task;
            for (String robotId : taskIdMap.keySet()) {
                if (task.getName().contains(robotId)
                        && task.getHasSubGraph().equals(AlgorithmConstant.NO)) {
                    t.setId(taskIdMap.get(robotId));
                    taskIdMap.put(robotId, taskIdMap.get(robotId) + 1);
                    break;
                }
            }
        }
    }

    public void codeGenerate(Path targetDir, MissionWrapper mission, AdditionalInfo additionalInfo,
            UEMAlgorithm algorithm, List<UEMRobot> robotList) {
        setNewTaskId(algorithm, robotList);
        List<CodeRobotWrapper> codeRobotList = makeDataStructure(mission, robotList);
        CommonCodeGenerator commonCodeGenerator = new CommonCodeGenerator();
        commonCodeGenerator.generate(targetDir, codeRobotList, mission, additionalInfo);
        AdditionalCodeGenerator additionalTaskCodeGenerator = new AdditionalCodeGenerator();
        additionalTaskCodeGenerator.generateAdditionalCode(targetDir, additionalInfo,
                codeRobotList);
        CommunicationCodeGenerator communicationCodeGenerator = new CommunicationCodeGenerator();
        communicationCodeGenerator.generateCommunicationCode(targetDir, robotList);
        SharedDataCodeGenerator sharedDataCodeGenerator = new SharedDataCodeGenerator();
        sharedDataCodeGenerator.generate(targetDir, codeRobotList);
        ControlTaskCodeGenerator controlTaskCodeGenerator = new ControlTaskCodeGenerator();
        controlTaskCodeGenerator.generateControlTaskCode(targetDir, codeRobotList);
        TaskCodeCopier taskCodeCopier = new TaskCodeCopier();
        taskCodeCopier.copyActionTaskCode(Paths.get(additionalInfo.getTaskServerPrefix()),
                targetDir, codeRobotList);
        taskCodeCopier.copyResourceTaskCode(Paths.get(additionalInfo.getTaskServerPrefix()),
                targetDir, codeRobotList);
        taskCodeCopier.copyLeaderSourceFile(Paths.get(additionalInfo.getTaskServerPrefix()),
                targetDir, codeRobotList);
        taskCodeCopier.copyGroupingTaskCode(Paths.get(additionalInfo.getTaskServerPrefix()),
                targetDir, codeRobotList);
        TranslatorCode translator = new TranslatorCode();
        translator.translate(targetDir, additionalInfo);
    }
}

package com.codegenerator.generator;

import java.nio.file.Path;
import java.util.List;
import com.codegenerator.generator.mapper.ModeServiceMapper;
import com.codegenerator.generator.mapper.RobotModeMapper;
import com.codegenerator.generator.mapper.ServiceStatementMapper;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.metadata.UEMRobot;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;

public class CodeGenerator {

    private List<CodeRobotWrapper> makeDataStructure(MissionWrapper mission,
            List<UEMRobot> robotList) {
        RobotModeMapper rmMapper = new RobotModeMapper();
        List<CodeRobotWrapper> codeRobotList = rmMapper.mapRobotMode(mission, robotList);
        for (CodeRobotWrapper codeRobot : codeRobotList) {
            for (CodeModeWrapper codeMode : codeRobot.getModeList()) {
                ModeServiceMapper msMapper = new ModeServiceMapper(codeRobot);
                List<CodeServiceWrapper> serviceList = msMapper.mapModeService(codeMode);
                for (CodeServiceWrapper service : serviceList) {
                    ServiceStatementMapper ssMapper = new ServiceStatementMapper(codeRobot);
                    ssMapper.mapServiceStatement(service);
                }
            }
        }
        return codeRobotList;
    }

    public void codeGenerate(Path targetDir, MissionWrapper mission, List<UEMRobot> robotList) {
        List<CodeRobotWrapper> codeRobotList = makeDataStructure(mission, robotList);
        CommonCodeGenerator commonCodeGenerator = new CommonCodeGenerator();
        commonCodeGenerator.generate(targetDir, codeRobotList);
        AdditionalTaskCodeGenerator additionalTaskCodeGenerator = new AdditionalTaskCodeGenerator();
        additionalTaskCodeGenerator.generateAdditionalCode(targetDir, codeRobotList);
        CommunicationCodeGenerator communicationCodeGenerator = new CommunicationCodeGenerator();
        communicationCodeGenerator.generateCommunicationCode(targetDir, robotList);
        SharedDataCodeGenerator sharedDataCodeGenerator = new SharedDataCodeGenerator();
        sharedDataCodeGenerator.generate(targetDir, codeRobotList);
        ControlTaskCodeGenerator controlTaskCodeGenerator = new ControlTaskCodeGenerator();
        controlTaskCodeGenerator.generateControlTaskCode(targetDir, codeRobotList);
    }
}

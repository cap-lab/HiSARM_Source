package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.codegenerator.generator.mapper.ModeServiceMapper;
import com.codegenerator.generator.mapper.RobotModeMapper;
import com.codegenerator.generator.mapper.ServiceStatementMapper;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.metadata.UEMRobot;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;

public class CodeGenerator {

    private List<CodeRobotWrapper> makeDataStructure(MissionWrapper mission,
            List<UEMRobot> robotList) {
        RobotModeMapper rmMapper = new RobotModeMapper();
        List<CodeRobotWrapper> codeRobotList = rmMapper.mapRobotMode(mission, robotList);
        for (CodeRobotWrapper codeRobot : codeRobotList) {
            for (CodeModeWrapper codeMode : codeRobot.getModeList()) {
                ModeServiceMapper msMapper = new ModeServiceMapper();
                List<CodeServiceWrapper> serviceList = msMapper.mapModeService(codeMode);
                for (CodeServiceWrapper service : serviceList) {
                    ServiceStatementMapper ssMapper = new ServiceStatementMapper(codeRobot);
                    ssMapper.mapServiceStatement(service);
                }
            }
        }
        return codeRobotList;
    }

    public void codeGenerate(Path targetDir, MissionWrapper mission, AdditionalInfo additionalInfo,
            List<UEMRobot> robotList) {
        List<CodeRobotWrapper> codeRobotList = makeDataStructure(mission, robotList);
        CommonCodeGenerator commonCodeGenerator = new CommonCodeGenerator();
        commonCodeGenerator.generate(targetDir, codeRobotList, additionalInfo);
        AdditionalCodeGenerator additionalTaskCodeGenerator = new AdditionalCodeGenerator();
        additionalTaskCodeGenerator.generateAdditionalCode(targetDir, additionalInfo,
                codeRobotList);
        CommunicationCodeGenerator communicationCodeGenerator = new CommunicationCodeGenerator();
        communicationCodeGenerator.generateCommunicationCode(targetDir, robotList);
        SharedDataCodeGenerator sharedDataCodeGenerator = new SharedDataCodeGenerator();
        sharedDataCodeGenerator.generate(targetDir, codeRobotList);
        ControlTaskCodeGenerator controlTaskCodeGenerator = new ControlTaskCodeGenerator();
        controlTaskCodeGenerator.generateControlTaskCode(targetDir, codeRobotList);
        ActionTaskCodeCopier actionTaskCodeCopier = new ActionTaskCodeCopier();
        actionTaskCodeCopier.copyActionTaskCode(Paths.get(additionalInfo.getTaskServerPrefix()),
                targetDir, codeRobotList);
        TranslatorCode translator = new TranslatorCode();
        translator.translate(targetDir, additionalInfo);
    }
}

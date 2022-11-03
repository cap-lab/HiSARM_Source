package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codegenerator.constant.RobotSpecificCommonConstant;
import com.codegenerator.constant.VariableConstant;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.codegenerator.wrapper.CodeVariableWrapper;
import com.metadata.UEMRobot;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;

public class CommonCodeGenerator {
    public void generate(Path targetDir, MissionWrapper mission, List<CodeRobotWrapper> robotList) {
        try {
            for (CodeRobotWrapper robot : robotList) {
                generateRobotSpecificCommon(targetDir, robot.getRobot());
                generateVariableCode(targetDir, mission, robot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateRobotSpecificCommon(Path targetDir, UEMRobot robot) {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(RobotSpecificCommonConstant.ROBOT_ID, robot.getRobotIndex());
        rootHash.put(RobotSpecificCommonConstant.CONTROL_TASK_ID,
                robot.getRobotTask().getControlTask().getId().intValue());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.ROBOT_SPECIFIC_COMMON_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.COMMON_HEADER_SUFFIX),
                rootHash);
    }

    private List<VariableTypeWrapper> makeVariableTypeList(CodeRobotWrapper robot) {
        List<VariableTypeWrapper> variableTypeList = new ArrayList<>();
        for (VariableTypeWrapper variableType : robot.getRobot().getRobotTask().getRobot()
                .getVariableMap().values()) {
            if (variableTypeList.stream().filter(v -> v.getVariableType().getName()
                    .equals(variableType.getVariableType().getName())).count() == 0) {
                variableTypeList.add(variableType);
            }
        }
        return variableTypeList;
    }

    private List<CodeVariableWrapper> makeVariableList(CodeRobotWrapper robot) {
        List<CodeVariableWrapper> variableList = new ArrayList<>();
        for (CodeModeWrapper mode : robot.getModeList()) {
            for (CodeServiceWrapper service : mode.getServiceList()) {
                for (CodeVariableWrapper variable : service.getVariableList()) {
                    if (variableList.contains(variable) == false) {
                        variableList.add(variable);
                    }
                }
            }
        }
        return variableList;
    }


    private void generateVariableCode(Path targetDir, MissionWrapper mission,
            CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(VariableConstant.VARIABLE_TYPE_LIST, makeVariableTypeList(robot));
        rootHash.put(VariableConstant.VARIABLE_MAP, makeVariableList(robot));

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.VARIABLE_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.VARIABLE_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.VARIABLE_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.VARIABLE_SOURCE_SUFFIX),
                rootHash);
    }
}

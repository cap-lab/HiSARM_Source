package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.codegenerator.constant.GroupCodeConstant;
import com.codegenerator.constant.RobotSpecificCommonConstant;
import com.codegenerator.constant.VariableConstant;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.codegenerator.generator.util.LocalFileCopier;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.codegenerator.wrapper.CodeStatementWrapper;
import com.codegenerator.wrapper.CodeTransitionWrapper;
import com.codegenerator.wrapper.CodeVariableWrapper;
import com.metadata.UEMRobot;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;

public class CommonCodeGenerator {
    public void generate(Path targetDir, List<CodeRobotWrapper> robotList, MissionWrapper mission,
            AdditionalInfo additionalInfo) {
        try {
            copyCommonCode(targetDir, additionalInfo);
            for (CodeRobotWrapper robot : robotList) {
                generateRobotSpecificCommon(targetDir, robot.getRobot());
                generateVariableCode(targetDir, robot);
                generateGroupCode(targetDir, robot, mission);
                copyVariableCode(targetDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyCommonCode(Path targetDir, AdditionalInfo additionalInfo) {
        try {
            LocalFileCopier.copyFile(CodeGeneratorConstant.COMMON_HEADER_CODE,
                    Paths.get(targetDir.toString(), CodeGeneratorConstant.COMMON_HEADER));
            LocalFileCopier.copyFile(CodeGeneratorConstant.LOGGER_HEADER_CODE,
                    Paths.get(targetDir.toString(), CodeGeneratorConstant.LOGGER_HEADER));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateRobotSpecificCommon(Path targetDir, UEMRobot robot) {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(RobotSpecificCommonConstant.ROBOT_ID, robot.getRobotIndex());
        rootHash.put(RobotSpecificCommonConstant.ROBOT_NAME, robot.getRobotName());
        rootHash.put(RobotSpecificCommonConstant.ROBOT_TYPE,
                robot.getRobotTask().getRobot().getRobotType().getRobotType().getRobotClass());
        rootHash.put(RobotSpecificCommonConstant.CONTROL_TASK_ID,
                robot.getRobotTask().getControlTask().getId().intValue());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.ROBOT_SPECIFIC_COMMON_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.COMMON_HEADER_SUFFIX),
                rootHash);
    }

    private List<CodeVariableWrapper> makeVariableList(CodeRobotWrapper robot) {
        List<CodeVariableWrapper> variableList = new ArrayList<>();
        for (CodeTransitionWrapper transition : robot.getTransitionList()) {
            for (CodeVariableWrapper variable : transition.getVariableList()) {
                boolean flag = true;
                for (CodeVariableWrapper variable2 : variableList) {
                    if (variable2.getId().equals(variable.getId())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    variableList.add(variable);
                }
            }
        }
        for (CodeModeWrapper mode : robot.getModeList()) {
            for (CodeVariableWrapper variable : mode.getVariableList()) {
                boolean flag = true;
                for (CodeVariableWrapper variable2 : variableList) {
                    if (variable2.getId().equals(variable.getId())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    variableList.add(variable);
                }
            }
        }
        for (CodeServiceWrapper service : robot.getServiceList()) {
            for (CodeVariableWrapper variable : service.getVariableList()) {
                boolean flag = true;
                for (CodeVariableWrapper variable2 : variableList) {
                    if (variable2.getId().equals(variable.getId())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    variableList.add(variable);
                }
            }
        }
        return new ArrayList<>(variableList);
    }

    private List<VariableTypeWrapper> makeVariableTypeList(CodeRobotWrapper robot,
            List<CodeVariableWrapper> variableList) {
        List<VariableTypeWrapper> variableTypeList = new ArrayList<>();
        for (CodeVariableWrapper variable : variableList) {
            VariableTypeWrapper variableType = variable.getType();
            if (variableTypeList.stream().filter(v -> v.getVariableType().getName()
                    .equals(variableType.getVariableType().getName())).count() == 0) {
                variableTypeList.add(variableType);
            }
        }
        for (CodeServiceWrapper service : robot.getServiceList()) {
            for (CodeStatementWrapper statement : service.getStatementList()) {
                for (CodeVariableWrapper variable : statement.getVariableList()) {
                    VariableTypeWrapper variableType = variable.getType();
                    if (variableTypeList.stream()
                            .filter(v -> v.getVariableType().getName()
                                    .equals(variableType.getVariableType().getName()))
                            .count() == 0) {
                        variableTypeList.add(variableType);
                    }
                }
            }
        }
        return variableTypeList;
    }

    private void generateVariableCode(Path targetDir, CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();
        List<CodeVariableWrapper> variableList = makeVariableList(robot);

        rootHash.put(VariableConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(VariableConstant.VARIABLE_LIST, variableList);
        rootHash.put(VariableConstant.VARIABLE_TYPE_LIST,
                makeVariableTypeList(robot, variableList));

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.VARIABLE_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.VARIABLE_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.VARIABLE_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.VARIABLE_SOURCE_SUFFIX),
                rootHash);
    }

    private void generateGroupCode(Path targetDir, CodeRobotWrapper robot, MissionWrapper mission)
            throws Exception {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(GroupCodeConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(GroupCodeConstant.TEAM, robot.getRobot().getRobotTask().getRobot().getTeam());
        rootHash.put(GroupCodeConstant.TEAM_LIST, mission.getTeamList().stream()
                .map(t -> t.getTeam().getName()).collect(Collectors.toList()));
        rootHash.put(GroupCodeConstant.GROUP_MAP,
                robot.getRobot().getRobotTask().getRobot().getGroupMap());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.GROUP_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.GROUP_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.GROUP_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.GROUP_SOURCE_SUFFIX),
                rootHash);

        FTLHandler.getInstance()
                .generateCode(CodeGeneratorConstant.TEAM_HEADER_TEMPLATE,
                        Paths.get(targetDir.toString(),
                                robot.getRobotName() + CodeGeneratorConstant.TEAM_HEADER_SUFFIX),
                        rootHash);
    }

    private void copyVariableCode(Path targetDir) {
        try {
            LocalFileCopier.copyFile(CodeGeneratorConstant.VARIABLE_HEADER_CODE,
                    Paths.get(targetDir.toString(), CodeGeneratorConstant.VARIABLE_HEADER));
            LocalFileCopier.copyFile(CodeGeneratorConstant.VARIABLE_SOURCE_CODE,
                    Paths.get(targetDir.toString(), CodeGeneratorConstant.VARIABLE_SOURCE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

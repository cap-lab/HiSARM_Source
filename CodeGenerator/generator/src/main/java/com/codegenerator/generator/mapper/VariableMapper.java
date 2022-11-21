package com.codegenerator.generator.mapper;

import java.util.ArrayList;
import java.util.List;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.codegenerator.wrapper.CodeTransitionElementWrapper;
import com.codegenerator.wrapper.CodeTransitionWrapper;
import com.codegenerator.wrapper.CodeVariableWrapper;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import com.scriptparser.parserdatastructure.util.VariableVisitor;
import com.scriptparser.parserdatastructure.wrapper.GroupModeTransitionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;

public class VariableMapper {
    private class Mapper implements VariableVisitor {
        private CodeRobotWrapper robot;

        public Mapper(CodeRobotWrapper robot) throws Exception {
            this.robot = robot;
        }

        @Override
        public void visitModeToService(ModeWrapper mode, ParallelServiceWrapper service,
                String groupId) {
            CodeModeWrapper codeMode =
                    robot.getMode(CodeModeWrapper.makeModeId(groupId, mode.getMode().getName()));
            List<CodeVariableWrapper> variableList = new ArrayList<>();
            CodeServiceWrapper codeService =
                    robot.getService(CodeServiceWrapper.makeServiceId(codeMode.getModeId(),
                            service.getService().getService().getName()));
            for (int i = 0; i < codeService.getParameterList().size(); i++) {
                Identifier input = service.getInputList().get(i).getIdentifierSet().get(0);
                CodeVariableWrapper param = codeService.getParameterList().get(i);
                if (input.getType() == IdentifierType.CONSTANT) {
                    String variableId =
                            CodeVariableWrapper.makeVariableId(codeService.getServiceId(), i);
                    CodeVariableWrapper variable = codeMode.getVariable(variableId);
                    if (variable == null) {
                        variable = new CodeVariableWrapper();
                        variable.getChildVariableList().add(variable);
                        variable.setDefaultValue(input.getId());
                        variable.setId(variableId);
                        variable.setName(variable.getId());
                        variable.setType(param.getType());
                        variable.setRealVariable(param.isRealVariable());
                        param.setDefaultValue(variable.getDefaultValue());
                        codeMode.getVariableList().add(variable);
                    }
                    variableList.add(variable);
                } else {
                    for (CodeVariableWrapper variable : codeMode.getParameterList()) {
                        if (variable.getName().equals(input.getId())) {
                            variable.setType(param.getType());
                            variable.setRealVariable(param.isRealVariable());
                            param.setDefaultValue(variable.getDefaultValue());
                            variableList.add(variable);
                            break;
                        }
                    }
                }
            }
            codeMode.getServiceMap().put(codeService, variableList);
        }

        @Override
        public void visitModeToTransition(ModeWrapper mode, GroupModeTransitionWrapper transition,
                String groupId) {
            CodeModeWrapper codeMode = robot.getMode(CodeModeWrapper.makeModeId(
                    groupId.substring(0, groupId.lastIndexOf("_")), mode.getMode().getName()));
            List<CodeVariableWrapper> variableList = new ArrayList<>();
            CodeTransitionWrapper codeTransition =
                    robot.getTransition(CodeTransitionWrapper.makeTransitionId(groupId,
                            transition.getModeTransition().getTransition().getName()));
            for (int i = 0; i < codeTransition.getParameterList().size(); i++) {
                CodeVariableWrapper param = codeTransition.getParameterList().get(i);
                Identifier input = transition.getInputList().get(i).getIdentifierSet().get(0);
                if (input.getType() == IdentifierType.CONSTANT) {
                    String variableId =
                            CodeVariableWrapper.makeVariableId(codeMode.getModeId(), groupId, i);
                    CodeVariableWrapper variable = codeMode.getVariable(variableId);
                    if (variable == null) {
                        variable = new CodeVariableWrapper();
                        variable.getChildVariableList().add(variable);
                        variable.setDefaultValue(input.getId());
                        param.setDefaultValue(input.getId());
                        variable.setId(variableId);
                        variable.setName(variable.getId());
                        codeMode.getVariableList().add(variable);
                    }
                    variableList.add(variable);
                } else {
                    for (CodeVariableWrapper argument : codeMode.getParameterList()) {
                        if (argument.getName().equals(input.getId())) {
                            param.setDefaultValue(argument.getDefaultValue());
                            variableList.add(argument);
                            break;
                        }
                    }
                }
            }
            codeMode.getGroupTransitionMap().put(codeTransition, variableList);
        }

        @Override
        public void visitTransitionToMode(TransitionWrapper transition, ModeWrapper srcMode,
                String event, TransitionModeWrapper dstMode, String groupId) {
            CodeTransitionWrapper codeTransition = robot.getTransition(CodeTransitionWrapper
                    .makeTransitionId(groupId, transition.getTransition().getName()));
            List<CodeVariableWrapper> variableList = new ArrayList<>();
            CodeModeWrapper srcCodeMode = null;
            if (event != null) {
                srcCodeMode = robot
                        .getMode(CodeModeWrapper.makeModeId(groupId, srcMode.getMode().getName()));
            }
            CodeTransitionElementWrapper transitionElement =
                    codeTransition.getTransitionElement(srcCodeMode, event);
            if (transitionElement != null) {
                return;
            } else {
                transitionElement = new CodeTransitionElementWrapper();
            }
            transitionElement.setSrcMode(srcCodeMode);
            transitionElement.setEvent(event);
            transitionElement.setDstMode(robot.getMode(
                    CodeModeWrapper.makeModeId(groupId, dstMode.getMode().getMode().getName())));
            CodeModeWrapper codeDstMode = transitionElement.getDstMode();
            for (int i = 0; i < transitionElement.getDstMode().getParameterList().size(); i++) {
                CodeVariableWrapper param = codeDstMode.getParameterList().get(i);
                Identifier identifier = dstMode.getInputList().get(i).getIdentifierSet().get(0);
                if (identifier.getType() == IdentifierType.CONSTANT) {
                    String variableId =
                            CodeVariableWrapper.makeVariableId(codeDstMode.getModeId(), groupId, i);
                    CodeVariableWrapper variable = codeTransition.getVariable(variableId);
                    if (variable == null) {
                        variable = new CodeVariableWrapper();
                        variable.setId(variableId);
                        variable.getChildVariableList().add(variable);
                        variable.setDefaultValue(identifier.getId());
                        param.setDefaultValue(variable.getDefaultValue());
                        variable.setName(variable.getId());
                        codeTransition.getVariableList().add(variable);
                    }
                    variableList.add(variable);
                } else {
                    for (CodeVariableWrapper argument : codeTransition.getParameterList()) {
                        if (argument.getName().equals(identifier.getId())) {
                            param.setDefaultValue(argument.getDefaultValue());
                            variableList.add(argument);
                            break;
                        }
                    }
                }
            }
            codeTransition.getModeMap().put(transitionElement, variableList);
        }
    }

    private void mapTransitionModeVariable(CodeRobotWrapper robot) {
        boolean flag = true;
        while (flag == true) {
            flag = false;
            for (CodeTransitionWrapper transition : robot.getTransitionList()) {
                for (CodeTransitionElementWrapper transitionElement : transition.getModeMap()
                        .keySet()) {
                    CodeModeWrapper dstMode = transitionElement.getDstMode();
                    List<CodeVariableWrapper> variableList =
                            transition.getModeMap().get(transitionElement);
                    for (int i = 0; i < variableList.size(); i++) {
                        CodeVariableWrapper variable = variableList.get(i);
                        CodeVariableWrapper param = dstMode.getParameterList().get(i);
                        if (variable.getType() == param.getType()
                                && variable.isRealVariable() == param.isRealVariable()
                                && param.getDefaultValue() == variable.getDefaultValue()) {
                            continue;
                        }
                        flag = true;
                        variable.setType(param.getType());
                        variable.setRealVariable(param.isRealVariable());
                        param.setDefaultValue(variable.getDefaultValue());
                    }
                }
            }
            for (CodeModeWrapper mode : robot.getModeList()) {
                for (CodeTransitionWrapper transition : mode.getGroupTransitionMap().keySet()) {
                    List<CodeVariableWrapper> variableList =
                            mode.getGroupTransitionMap().get(transition);
                    for (int i = 0; i < variableList.size(); i++) {
                        CodeVariableWrapper variable = variableList.get(i);
                        CodeVariableWrapper param = transition.getParameterList().get(i);
                        if (variable.getType() == param.getType()
                                && variable.isRealVariable() == param.isRealVariable()
                                && param.getDefaultValue() == variable.getDefaultValue()) {
                            continue;
                        }
                        flag = true;
                        variable.setType(param.getType());
                        variable.setRealVariable(param.isRealVariable());
                        param.setDefaultValue(variable.getDefaultValue());
                    }
                }
            }
        }
    }

    private void setDefaultValueForTeamVariable(CodeRobotWrapper robot) {
        robot.getTransitionList().forEach(t -> t.getVariableList().stream()
                .filter(v -> (v.isRealVariable() == false)).forEach(v -> v
                        .setDefaultValue(CodeGeneratorConstant.ID_TEAM + v.getDefaultValue())));
        robot.getModeList().forEach(m -> m.getVariableList().stream()
                .filter(v -> (v.isRealVariable() == false)).forEach(v -> v
                        .setDefaultValue(CodeGeneratorConstant.ID_TEAM + v.getDefaultValue())));
        robot.getServiceList().forEach(s -> s.getVariableList().stream()
                .filter(v -> (v.isRealVariable() == false)).forEach(v -> v
                        .setDefaultValue(CodeGeneratorConstant.ID_TEAM + v.getDefaultValue())));
    }

    public void mapVariable(List<CodeRobotWrapper> robotList) {
        try {
            for (CodeRobotWrapper robot : robotList) {
                Mapper mapper = new Mapper(robot);
                String teamName = robot.getRobot().getRobotTask().getRobot().getTeam();
                robot.getTransition(CodeTransitionWrapper.makeTransitionId(teamName, teamName))
                        .getTransition().traverseTransition(
                                new String(), teamName, null, new ArrayList<>(robot.getRobot()
                                        .getRobotTask().getRobot().getGroupMap().keySet()),
                                null, mapper);
                mapTransitionModeVariable(robot);
                setDefaultValueForTeamVariable(robot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.codegenerator.generator.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeTransitionWrapper;
import com.codegenerator.wrapper.CodeVariableWrapper;
import com.metadata.UEMRobot;
import com.scriptparser.parserdatastructure.util.ModeTransitionVisitor;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;

public class RobotModeMapper {
    private class ModeListMaker implements ModeTransitionVisitor {
        List<CodeModeWrapper> modeList = new ArrayList<CodeModeWrapper>();
        Map<String, CodeTransitionWrapper> transitionMap = new HashMap<>();
        Map<String, CodeModeWrapper> modeMap = new HashMap<>();

        @Override
        public void visitMode(ModeWrapper mode, String modeId, String groupId) {
            CodeModeWrapper codeMode = new CodeModeWrapper();
            codeMode.setModeId(modeId);
            codeMode.setMode(mode);
            codeMode.setGroupId(groupId);
            modeList.add(codeMode);
            transitionMap.get(groupId).addMode(codeMode);
            if (mode.getParameterList() != null) {
                mode.getParameterList().forEach(param -> {
                    String variableId =
                            CodeVariableWrapper.makeVariableId(codeMode.getModeId(), param.getId());
                    CodeVariableWrapper variable = new CodeVariableWrapper();
                    variable.setName(param.getId());
                    variable.getChildVariableList().add(variable);
                    variable.setId(variableId);
                    codeMode.getParameterList().add(variable);
                    codeMode.getVariableList().add(variable);
                });
            }
            modeMap.put(groupId, codeMode);
        }

        @Override
        public void visitTransition(TransitionWrapper transition, String transitionId,
                String groupId) {
            CodeTransitionWrapper codeTransition = new CodeTransitionWrapper();
            codeTransition.setTransition(transition);
            codeTransition.setGroupId(groupId);
            codeTransition.setTransitionId(transitionId);
            codeTransition.setDepth(groupId.split("_").length - 1);
            if (transition.getParameterList() != null) {
                transition.getParameterList().forEach(param -> {
                    String variableId = CodeVariableWrapper
                            .makeVariableId(codeTransition.getTransitionId(), param.getId());
                    CodeVariableWrapper variable = new CodeVariableWrapper();
                    variable.setName(param.getId());
                    variable.getChildVariableList().add(variable);
                    variable.setId(variableId);
                    codeTransition.getParameterList().add(variable);
                    codeTransition.getVariableList().add(variable);
                });
            }
            transitionMap.put(groupId, codeTransition);
        }

        public List<CodeModeWrapper> getModeList() {
            return modeList;
        }

        public Map<String, CodeTransitionWrapper> getTransitionMap() {
            return transitionMap;
        }
    }

    public List<CodeRobotWrapper> mapRobotMode(MissionWrapper mission, List<UEMRobot> robotList) {
        List<CodeRobotWrapper> codeRobotList = new ArrayList<>();
        try {
            for (UEMRobot robot : robotList) {
                CodeRobotWrapper codeRobot = new CodeRobotWrapper();
                TransitionWrapper transition =
                        mission.getTransition(robot.getRobotTask().getRobot().getTeam());
                codeRobot.setRobot(robot);
                CodeTransitionWrapper codeTransition = new CodeTransitionWrapper();
                ModeListMaker maker = new ModeListMaker();
                codeTransition.setTransition(transition);
                codeTransition.setGroupId(robot.getRobotTask().getRobot().getTeam());
                codeTransition.setTransitionId(robot.getRobotTask().getRobot().getTeam());
                codeTransition.setDepth(0);
                maker.getTransitionMap().put(robot.getRobotTask().getRobot().getTeam(),
                        codeTransition);
                transition.traverseTransition(new String(),
                        robot.getRobotTask().getRobot().getTeam(), new ArrayList<String>(),
                        new ArrayList<>(robot.getRobotTask().getRobot().getGroupMap().keySet()),
                        maker, null);
                codeRobot.setModeList(maker.getModeList());
                codeRobot.setTransitionList(new ArrayList<>(maker.getTransitionMap().values()));
                codeRobotList.add(codeRobot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codeRobotList;
    }
}

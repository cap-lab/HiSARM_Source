package com.codegenerator.generator.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeTransitionWrapper;
import com.metadata.UEMRobot;
import com.scriptparser.parserdatastructure.util.ModeVisitor;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import com.strategy.strategymaker.GroupAllocator;

public class RobotModeMapper {
    private class ModeListMaker implements ModeVisitor {
        List<CodeModeWrapper> modeList = new ArrayList<CodeModeWrapper>();
        Map<String, CodeTransitionWrapper> transitionMap = new HashMap<>();
        Set<String> groupList = new HashSet<String>();

        public ModeListMaker(Set<String> groupList) {
            this.groupList = groupList;
        }

        @Override
        public void visitMode(ModeWrapper mw, String modeId, String groupId) {
            CodeModeWrapper mode = new CodeModeWrapper();
            mode.setModeId(modeId);
            mode.setMode(mw);
            mode.setGroupId(groupId);
            modeList.add(mode);
            transitionMap.get(groupId).getModeList().add(mode);

            mw.getGroupList().forEach(g -> {
                CodeTransitionWrapper transition = new CodeTransitionWrapper();
                transition.setTransition(g.getModeTransition().getModeTransition());
                transition.setTransitionId(transition.getTransition().makeTransitionid(modeId));
                transition.setGroupId(GroupAllocator.makeGroupKey(groupId, g.getGroup().getName()));
                transition.setDepth(transition.getGroupId().split("_").length - 1);
                if (groupList.contains(transition.getGroupId())) {
                    transitionMap.put(transition.getGroupId(), transition);
                }
            });
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
                ModeListMaker maker =
                        new ModeListMaker(robot.getRobotTask().getRobot().getGroupMap().keySet());
                codeRobot.setRobot(robot);
                TransitionWrapper transition =
                        mission.getTransition(robot.getRobotTask().getRobot().getTeam());
                CodeTransitionWrapper codeTransition = new CodeTransitionWrapper();
                codeTransition.setTransition(transition);
                codeTransition.setGroupId(robot.getRobotTask().getRobot().getTeam());
                codeTransition.setTransitionId(robot.getRobotTask().getRobot().getTeam());
                codeTransition.setDepth(0);
                maker.getTransitionMap().put(robot.getRobotTask().getRobot().getTeam(),
                        codeTransition);
                transition.traverseTransition(new String(),
                        robot.getRobotTask().getRobot().getTeam(), new ArrayList<String>(),
                        new ArrayList<>(robot.getRobotTask().getRobot().getGroupMap().keySet()),
                        maker);
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

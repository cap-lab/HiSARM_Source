package com.codegenerator.mapper;

import java.util.ArrayList;
import java.util.List;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.metadata.UEMRobot;
import com.scriptparser.parserdatastructure.util.ModeVisitor;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;

public class RobotModeMapper {
    private class ModeListMaker implements ModeVisitor {
        List<CodeModeWrapper> modeList = new ArrayList<CodeModeWrapper>();

        @Override
        public void visitMode(ModeWrapper mw, String modeId, String groupId) {
            CodeModeWrapper mode = new CodeModeWrapper();
            mode.setModeId(modeId);
            mode.setMode(mw);
            mode.setGroupId(groupId);
            modeList.add(mode);
        }

        public List<CodeModeWrapper> getModeList() {
            return modeList;
        }
    }

    public List<CodeRobotWrapper> mapRobotMode(MissionWrapper mission, List<UEMRobot> robotList) {
        List<CodeRobotWrapper> codeRobotList = new ArrayList<>();
        try {
            for (UEMRobot robot : robotList) {
                CodeRobotWrapper codeRobot = new CodeRobotWrapper();
                ModeListMaker maker = new ModeListMaker();
                codeRobot.setRobot(robot);
                TransitionWrapper transition =
                        mission.getTransition(robot.getRobotTask().getRobot().getTeam());
                transition.traverseTransition(new String(),
                        robot.getRobotTask().getRobot().getTeam(), new ArrayList<String>(),
                        robot.getRobotTask().getRobot().getGroupList(), maker);
                codeRobot.setModeList(maker.getModeList());
                codeRobotList.add(codeRobot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codeRobotList;
    }
}

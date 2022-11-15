package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.metadata.UEMRobot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeRobotWrapper {
    private UEMRobot robot;
    private List<CodeModeWrapper> modeList = new ArrayList<>();
    private List<CodeTransitionWrapper> transitionList = new ArrayList<>();

    public String getRobotName() {
        return robot.getRobotName();
    }
}

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
    private List<CodeServiceWrapper> serviceList = new ArrayList<>();

    public String getRobotName() {
        return robot.getRobotName();
    }

    public CodeTransitionWrapper getTransition(String transitionId) {
        for (CodeTransitionWrapper transition : transitionList) {
            if (transition.getTransitionId().equals(transitionId)) {
                return transition;
            }
        }
        return null;
    }

    public CodeModeWrapper getMode(String modeId) {
        for (CodeModeWrapper mode : modeList) {
            if (mode.getModeId().equals(modeId)) {
                return mode;
            }
        }
        return null;
    }

    public CodeServiceWrapper getService(String serviceId) {
        for (CodeServiceWrapper service : serviceList) {
            if (service.getServiceId().equals(serviceId)) {
                return service;
            }
        }
        return null;
    }
}

package com.dbmanager.datastructure.controlstrategy;

import java.util.ArrayList;
import java.util.List;

public class ControlStrategy {
    private String actionName;
    private String robotClass;
    private List<ControlStrategyElement> strategyImplList;

    public ControlStrategy() {
        strategyImplList = new ArrayList<ControlStrategyElement>();
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getRobotClass() {
        return robotClass;
    }

    public void setRobotClass(String robotClass) {
        this.robotClass = robotClass;
    }

    public List<ControlStrategyElement> getStrategyImplList() {
        return strategyImplList;
    }

    public void setStrategyImplList(List<ControlStrategyElement> strategyImplList) {
        this.strategyImplList = strategyImplList;
    }
}

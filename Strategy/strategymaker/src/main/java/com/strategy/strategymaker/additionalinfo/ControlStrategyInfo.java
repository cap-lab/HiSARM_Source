package com.strategy.strategymaker.additionalinfo;

public class ControlStrategyInfo {
    private String actionName;
    private String teamName;
    private String robotClass;
    private String strategyId;

    public ControlStrategyInfo(String actionName, String teamName, String robotClass,
            String strategyId) {
        this.actionName = actionName;
        this.teamName = teamName;
        this.robotClass = robotClass;
        this.strategyId = strategyId;
    }

    public String getActionName() {
        return actionName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getRobotClass() {
        return robotClass;
    }

    public String getStrategyId() {
        return strategyId;
    }
}

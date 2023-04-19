package com.strategy.strategydatastructure.additionalinfo;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.strategy.strategydatastructure.constant.AdditionalInfoConstant;

public class AdditionalInfo {
    private String projectName;
    private String taskServerPrefix;
    private String jarDirectory;
    private String translatorDirectory;
    private List<DatabaseInfo> dbInfo;
    private List<String> robotList;
    private List<ControlStrategyInfo> strategyList;
    private List<CustomVariableInfo> variableList;
    private String leaderSelectionAlgorithm;
    private String groupingAlgorithm;

    public AdditionalInfo() {
        robotList = new ArrayList<String>();
        strategyList = new ArrayList<ControlStrategyInfo>();
        variableList = new ArrayList<CustomVariableInfo>();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaskServerPrefix() {
        return taskServerPrefix;
    }

    public void setTaskServerPrefix(String taskServerPrefix) {
        this.taskServerPrefix = taskServerPrefix;
    }

    public List<String> getRobotList() {
        return robotList;
    }

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public void setRobotList(List<String> robotList) {
        this.robotList = robotList;
    }

    public List<ControlStrategyInfo> getStrategyList() {
        return strategyList;
    }

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public void setStrategyList(List<ControlStrategyInfo> strategyList) {
        this.strategyList = strategyList;
    }

    public String findStrategy(String teamName, String robotClass, String actionName) {
        if (strategyList != null) {
            for (ControlStrategyInfo strategy : strategyList) {
                if ((strategy.getTeamName().equals(teamName)
                        || strategy.getTeamName().equals("ALL"))
                        && strategy.getRobotClass().equals(robotClass)
                        && strategy.getActionName().equals(actionName)) {
                    return strategy.getStrategyId();
                }
            }
        }
        return null;
    }

    public List<CustomVariableInfo> getVariableList() {
        return variableList;
    }

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public void setVariableList(List<CustomVariableInfo> variableList) {
        this.variableList = variableList;
    }

    public List<DatabaseInfo> getDbInfo() {
        return dbInfo;
    }

    public void setDbInfo(List<DatabaseInfo> dbInfo) {
        this.dbInfo = dbInfo;
    }

    public String getJarDirectory() {
        return jarDirectory;
    }

    public void setJarDirectory(String jarDirectory) {
        this.jarDirectory = jarDirectory;
    }

    public String getTranslatorDirectory() {
        return translatorDirectory;
    }

    public void setTranslatorDirectory(String translatorDirectory) {
        this.translatorDirectory = translatorDirectory;
    }

    public String getGroupingAlgorithm() {
        if (groupingAlgorithm != null) {
            return groupingAlgorithm;
        } else {
            return AdditionalInfoConstant.DEFAULT_GROUPING_ALGORITHM;
        }
    }

    public void setGroupingAlgorithm(String groupingAlgorithm) {
        this.groupingAlgorithm = groupingAlgorithm;
    }

    public String getLeaderSelectionAlgorithm() {
        if (leaderSelectionAlgorithm != null) {
            return leaderSelectionAlgorithm;
        } else {
            return AdditionalInfoConstant.DEFAULT_LEADER_SELECTION_ALGORITHM;
        }
    }

    public void setLeaderSelectionAlgorithm(String leaderSelectionAlgorithm) {
        this.leaderSelectionAlgorithm = leaderSelectionAlgorithm;
    }
}

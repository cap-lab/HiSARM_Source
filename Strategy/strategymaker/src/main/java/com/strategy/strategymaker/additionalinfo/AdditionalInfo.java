package com.strategy.strategymaker.additionalinfo;

import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.robot.ConnectionType;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

public class AdditionalInfo {
    private String projectName;
    private String taskServerPrefix;
    private List<DatabaseInfo> dbInfo;
    private List<String> robotList;
    private List<ControlStrategyInfo> strategyList;
    private List<ConnectionInfo> connectionList;
    private List<CustomVariableInfo> variableList;

    public AdditionalInfo() {
        robotList = new ArrayList<String>();
        strategyList = new ArrayList<ControlStrategyInfo>();
        connectionList = new ArrayList<ConnectionInfo>();
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

    public List<ConnectionInfo> getConnectionList() {
        return connectionList;
    }

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public void setConnectionList(List<ConnectionInfo> connectionList) {
        this.connectionList = connectionList;
    }

    public List<ConnectionType> getConnectionCandidates(String robot1, String robot2) {
        List<ConnectionType> connectionCandidateList = new ArrayList<ConnectionType>();
        for (ConnectionInfo connection : connectionList) {
            if (connection.getRobotList().contains(robot1)
                    && connection.getRobotList().contains(robot2)) {
                connectionCandidateList.add(connection.getConnection());
            }
        }
        return connectionCandidateList;
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

}

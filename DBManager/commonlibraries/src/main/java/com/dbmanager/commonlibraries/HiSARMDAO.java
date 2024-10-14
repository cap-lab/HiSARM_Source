package com.dbmanager.commonlibraries;

import org.bson.Document;

public interface HiSARMDAO {
    public void initializeDB(String ip, int port, String user, String epwd, String pwd,
            String dbName);

    public Document getStrategy(String strategyId);

    public Document getRobot(String robotClass);

    public Document getArchitecture(String deviceName);

    public Document getTask(String taskId);

    public Document getTaskType(String taskId);

    public Document getAction(String actionName);

    public Document getActionImpl(String robotClass, String actionName);

    public Document getActionImpl(String actionImplId);

    public Document getVariable(String variableName);

    public Document getVariableImpl(String robotClass, String variableName);

    public Document getRobotImpl(String robotId);

    public Document getResource(String robotClass, String resourceId);

    public Document getGroupingAlgorithm(String groupingId);

    public Document getLeaderSelectionAlgorithm(String leaderSelectionId);

    public Document getSimulationDevice(String deviceId);

    public void close();
}

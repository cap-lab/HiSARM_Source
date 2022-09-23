package com.strategy.strategymaker.additionalinfo;

import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.robot.ConnectionType;

public class ConnectionInfo {
    public List<String> robotList;
    public ConnectionType connection;

    public ConnectionInfo() {
        robotList = new ArrayList<String>();
    }

    public List<String> getRobotList() {
        return robotList;
    }

    public void setRobotList(List<String> robotList) {
        this.robotList = robotList;
    }

    public ConnectionType getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = ConnectionType.getEnumFromString(connection);
    }
}

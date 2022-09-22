package com.dbmanager.commonlibraries.Mapper;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.robot.Connection;
import com.dbmanager.datastructure.robot.Robot;

public class RobotMapper {
    private static List<Connection> makeConnectionList(List<Document> documentList) {
        List<Connection> connectionList = new ArrayList<Connection>();

        documentList.forEach(d -> {
            Connection connection = new Connection();
            connection.setServerDevice(d.getString("serverDevice"));
            connection.setClientDevice(d.getString("clientDevice"));
            connection.setMedium(d.getString("medium"));
            connectionList.add(connection);
        });

        return connectionList;
    }

    public static Robot mapToRobot(Bson bson) {
        Robot robot = new Robot();
        Document document = (Document) bson;

        try {
            robot.setRobotClass(document.getString("RobotClass"));
            robot.setResourceList(document.getList("Resource", String.class));
            robot.setPrimaryArchitecture(
                    document.get("Architecture", Document.class).getString("primaryArchitecture"));
            robot.setArchitectureList(document.get("Architecture", Document.class)
                    .getList("architectureList", String.class));
            robot.setConnectionList(makeConnectionList(document.get("Architecture", Document.class)
                    .getList("connection", Document.class)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return robot;
    }
}

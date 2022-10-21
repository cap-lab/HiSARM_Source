package com.dbmanager.commonlibraries.Mapper;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.robot.Robot;

public class RobotMapper {

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return robot;
    }
}

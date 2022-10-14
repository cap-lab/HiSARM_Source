package com.dbmanager.commonlibraries.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.robot.CommunicationAddress;
import com.dbmanager.datastructure.robot.ConnectionType;
import com.dbmanager.datastructure.robot.IPBasedAddress;
import com.dbmanager.datastructure.robot.PortBasedAddress;
import com.dbmanager.datastructure.robot.RobotImpl;

public class RobotImplMapper {
    private static Map<ConnectionType, CommunicationAddress> makeCommunicationMap(
            List<Document> documentList) {
        Map<ConnectionType, CommunicationAddress> communicationMap = new HashMap<>();

        documentList.forEach(d -> {
            ConnectionType type =
                    ConnectionType.getEnumFromString(d.getString("type").toUpperCase());
            CommunicationAddress address = null;
            switch (type) {
                case ETHERNET_WIFI:
                    address = d.get("address", IPBasedAddress.class);
                    break;
                case USB:
                    address = d.get("address", PortBasedAddress.class);
                    break;
            }
            communicationMap.put(type, address);
        });

        return communicationMap;
    }

    public static RobotImpl mapToRobotImpl(Bson bson) {
        RobotImpl robot = new RobotImpl();
        Document document = (Document) bson;

        try {
            robot.setRobotId(document.getString("RobotId"));
            robot.setRobotClass(document.getString("RobotClass"));
            robot.setCommunicationInfoMap(
                    makeCommunicationMap(document.getList("CommunicationInfo", Document.class)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return robot;
    }
}

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
            Document addressDoc = d.get("address", Document.class);
            switch (type) {
                case ETHERNET_WIFI:
                    IPBasedAddress ipAddress = new IPBasedAddress();
                    ipAddress.setIp(addressDoc.getString("ip"));
                    ipAddress.setPort(addressDoc.getInteger("port"));
                    communicationMap.put(type, ipAddress);
                    break;
                case USB:
                    PortBasedAddress portAddress = new PortBasedAddress();
                    portAddress.setPort(addressDoc.getString("port"));
                    communicationMap.put(type, portAddress);
                    break;
            }
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

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
import com.dbmanager.datastructure.simulationdevice.SimulationDevice;

public class SimulationDeviceMapper {
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

    public static SimulationDevice mapToSimulationDevice(Bson bson) {
        SimulationDevice device = new SimulationDevice();
        Document document = (Document) bson;

        try {
            device.setDeviceId(document.getString("DeviceId"));
            device.setArchitecture(document.getString("Architecture"));
            device.setCommunicationInfoMap(
                    makeCommunicationMap(document.getList("CommunicationInfo", Document.class)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return device;
    }
}

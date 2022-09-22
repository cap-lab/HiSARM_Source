package com.dbmanager.commonlibraries.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.commonlibraries.DBService;
import com.dbmanager.datastructure.architecture.SWPlatform;
import com.dbmanager.datastructure.robot.CommunicationAddress;
import com.dbmanager.datastructure.robot.ConnectionType;
import com.dbmanager.datastructure.robot.DeviceInfo;
import com.dbmanager.datastructure.robot.IPBasedAddress;
import com.dbmanager.datastructure.robot.MacBasedAddress;
import com.dbmanager.datastructure.robot.PinBasedAddress;
import com.dbmanager.datastructure.robot.PortBasedAddress;
import com.dbmanager.datastructure.robot.RobotImpl;

public class RobotImplMapper {
    private static List<CommunicationAddress> makeIpAddress(List<Document> documentList) {
        List<CommunicationAddress> addressList = new ArrayList<CommunicationAddress>();

        documentList.forEach(d -> {
            IPBasedAddress address = new IPBasedAddress();
            address.setIP(d.getString("ip"));
            address.setPort(d.getInteger("port"));
            addressList.add(address);
        });

        return addressList;
    }

    private static List<CommunicationAddress> makeMacAddress(List<Document> documentList) {
        List<CommunicationAddress> addressList = new ArrayList<CommunicationAddress>();

        documentList.forEach(d -> {
            MacBasedAddress address = new MacBasedAddress();
            address.setMac(d.getString("mac"));
            addressList.add(address);
        });

        return addressList;
    }

    private static List<CommunicationAddress> makePortAddress(List<Document> documentList) {
        List<CommunicationAddress> addressList = new ArrayList<CommunicationAddress>();

        documentList.forEach(d -> {
            PortBasedAddress address = new PortBasedAddress();
            address.setPort(d.getString("port"));
            addressList.add(address);
        });

        return addressList;
    }

    private static List<CommunicationAddress> makePinAddress(List<Document> documentList) {
        List<CommunicationAddress> addressList = new ArrayList<CommunicationAddress>();

        documentList.forEach(d -> {
            PinBasedAddress address = new PinBasedAddress();
            address.setBoardRXPinNumber(d.getInteger("rxPin"));
            address.setBoardTXPinNumber(d.getInteger("txPin"));
            addressList.add(address);
        });

        return addressList;
    }

    private static Map<ConnectionType, List<CommunicationAddress>> makeCommunicationMap(
            SWPlatform swPlatform, List<Document> documentList) {
        Map<ConnectionType, List<CommunicationAddress>> communicationList =
                new HashMap<ConnectionType, List<CommunicationAddress>>();

        documentList.forEach(d -> {
            ConnectionType type =
                    ConnectionType.getEnumFromString(d.getString("type").toUpperCase());
            List<CommunicationAddress> addressList = null;
            switch (type) {
                case ETHERNET_WIFI:
                    addressList = makeIpAddress(d.getList("address", Document.class));
                    break;
                case BLUETOOTH:
                    addressList = makeMacAddress(d.getList("address", Document.class));
                    break;
                case USB:
                case WIRE:
                    switch (swPlatform) {
                        case LINUX:
                        case WINDOWS:
                            addressList = makePortAddress(d.getList("address", Document.class));
                            break;
                        case ARDUINO:
                            addressList = makePinAddress(d.getList("address", Document.class));
                            break;
                        default:
                            // TODO: error
                    }
                    break;
                default:
                    // TODO: error
            }
            communicationList.put(type, addressList);
        });

        return communicationList;
    }

    private static List<DeviceInfo> makeDeviceList(List<Document> documentList) {
        List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();

        documentList.forEach(d -> {
            DeviceInfo device = new DeviceInfo();
            device.setDevice(d.getString("device"));
            device.setCommunicationInfoList(makeCommunicationMap(
                    DBService.getArchitecture(device.getDevice()).getSWPlatform(),
                    d.getList("communication", Document.class)));
            deviceList.add(device);
        });

        return deviceList;
    }

    public static RobotImpl mapToRobotImpl(Bson bson) {
        RobotImpl robot = new RobotImpl();
        Document document = (Document) bson;

        try {
            robot.setRobotId(document.getString("RobotId"));
            robot.setRobotIndex(document.getInteger("RobotIndex"));
            robot.setRobotClass(document.getString("RobotClass"));
            robot.setDeviceList(makeDeviceList(document.getList("DeviceInfo", Document.class)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return robot;
    }
}

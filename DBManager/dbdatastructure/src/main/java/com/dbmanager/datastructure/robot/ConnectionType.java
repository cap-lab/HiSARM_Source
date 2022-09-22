package com.dbmanager.datastructure.robot;

import java.util.ArrayList;
import java.util.List;

public enum ConnectionType {
    ETHERNET_WIFI("Ethernet/Wi-Fi"), BLUETOOTH("Bluetooth"), WIRE("Wire"), USB("USB");

    private final String value;

    private ConnectionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ConnectionType getEnumFromString(String value) {
        if (value.toUpperCase().equals("ETHERNET/WI-FI")) {
            return ConnectionType.ETHERNET_WIFI;
        } else {
            return ConnectionType.valueOf(value);
        }
    }

    public static List<ConnectionType> getConnectionPriority() {
        List<ConnectionType> connectionList = new ArrayList<ConnectionType>();
        connectionList.add(BLUETOOTH);
        connectionList.add(ETHERNET_WIFI);
        connectionList.add(WIRE);
        connectionList.add(USB);
        return connectionList;
    }
}

package com.dbmanager.datastructure.robot;

public enum ConnectionType {
    ETHERNET_WIFI("Ethernet/Wi-Fi"), USB("USB");

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
}

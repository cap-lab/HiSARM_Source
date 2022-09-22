package com.dbmanager.datastructure.robot;

import java.util.HashMap;
import java.util.Map;

public class IPBasedAddress implements CommunicationAddress {
    private String ip;
    private int port;

    public static final String IP = "ip";
    public static final String PORT = "port";

    public void setIP(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Map<String, Object> getAddress() {
        Map<String, Object> address = new HashMap<String, Object>();
        address.put(IP, ip);
        address.put(PORT, port);
        return address;
    }
}

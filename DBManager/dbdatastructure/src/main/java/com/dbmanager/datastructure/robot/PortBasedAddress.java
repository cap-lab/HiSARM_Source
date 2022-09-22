package com.dbmanager.datastructure.robot;

import java.util.HashMap;
import java.util.Map;

public class PortBasedAddress implements CommunicationAddress {
    private String port;
    private String role;

    public static final String PORT = "port";
    public static final String ROLE = "role";

    public void setPort(String port) {
        this.port = port;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Map<String, Object> getAddress() {
        Map<String, Object> address = new HashMap<String, Object>();
        address.put(PORT, port);
        address.put(ROLE, role);
        return address;
    }
}

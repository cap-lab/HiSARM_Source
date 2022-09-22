package com.dbmanager.datastructure.robot;

import java.util.HashMap;
import java.util.Map;

public class MacBasedAddress implements CommunicationAddress {
    private String mac;
    private String role;

    public static final String MAC = "mac";
    public static final String ROLE = "role";

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Map<String, Object> getAddress() {
        Map<String, Object> address = new HashMap<String, Object>();
        address.put(MAC, mac);
        address.put(ROLE, role);
        return address;
    }
}

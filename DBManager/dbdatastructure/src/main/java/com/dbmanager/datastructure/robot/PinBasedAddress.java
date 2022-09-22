package com.dbmanager.datastructure.robot;

import java.util.HashMap;
import java.util.Map;

public class PinBasedAddress implements CommunicationAddress {
    private int boardTXPinNumber;
    private int boardRXPinNumber;
    private String role;

    public static final String TX_PIN = "txPin";
    public static final String RX_PIN = "rxPin";
    public static final String ROLE = "role";

    public void setBoardTXPinNumber(int boardTXPinNumber) {
        this.boardTXPinNumber = boardTXPinNumber;
    }

    public void setBoardRXPinNumber(int boardRxPinNumber) {
        this.boardRXPinNumber = boardRxPinNumber;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Map<String, Object> getAddress() {
        Map<String, Object> address = new HashMap<String, Object>();
        address.put(TX_PIN, boardTXPinNumber);
        address.put(RX_PIN, boardRXPinNumber);
        address.put(ROLE, role);
        return address;
    }
}

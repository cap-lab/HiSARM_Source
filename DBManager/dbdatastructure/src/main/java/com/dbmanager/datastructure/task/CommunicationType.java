package com.dbmanager.datastructure.task;

public enum CommunicationType {
    CHANNEL("channel"), LIBRARY("library"), GROUP("group"), LEADER("leader"), RESOURCE(
            "resource"), SIMULATION("simulation");

    private final String value;

    private CommunicationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

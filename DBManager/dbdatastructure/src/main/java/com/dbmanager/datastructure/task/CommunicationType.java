package com.dbmanager.datastructure.task;

public enum CommunicationType {
    CHANNEL("channel"), LIBRARY("library"), GROUP("group"), LEADER("leader");

    private final String value;

    private CommunicationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

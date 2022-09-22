package com.dbmanager.datastructure.task;

public enum CommunicationType {
    CHANNEL("channel"), MULTICAST("multicast"), LIBRARY("library"), SYSREQUEST("sysrequest");

    private final String value;

    private CommunicationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package com.strategy.strategydatastructure.enumeration;

public enum AdditionalTaskType {
    LEADER_SELECTION("leader_selection"), GROUP_SELECTION("group_selection");

    private String value;

    private AdditionalTaskType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

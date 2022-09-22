package com.dbmanager.datastructure.architecture;

public enum SWPlatform {
    ARDUINO("Arduino"), LINUX("Linux"), WINDOWS("Windows");

    private final String value;

    private SWPlatform(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

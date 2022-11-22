package com.scriptparser.parserdatastructure.enumeration;

public enum TimeUnit {
    MSEC("MS"), SEC("S"), MINUTE("M"), HOUR("H"), DAY("D");

    private String value;

    private TimeUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

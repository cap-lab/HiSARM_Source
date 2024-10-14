package com.scriptparser.parserdatastructure.enumeration;

import lombok.Getter;

@Getter
public enum Priority {
    HIGH("H"), MIDDLE("M"), LOW("L");

    private String value;

    private Priority(String value) {
        this.value = value;
    }

    static public Priority valueFrom(String value) throws Exception {
        for (Priority p : Priority.values()) {
            if (p.getValue().equals(value)) {
                return p;
            }
        }
        throw new Exception("Strange Priority : " + value);
    }
}

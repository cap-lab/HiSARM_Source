package com.dbmanager.datastructure.architecture;

public class EnvironmentVariable {
    private String value;
    private String name;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

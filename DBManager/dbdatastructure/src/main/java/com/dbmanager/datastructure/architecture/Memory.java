package com.dbmanager.datastructure.architecture;

public class Memory {
    private int size;
    private String unit;

    public void setSize(int size) {
        this.size = size;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getSize() {
        return size;
    }

    public String getUnit() {
        return unit;
    }

    public String getValue() {
        return "MEMORY_" + String.valueOf(size) + unit;
    }
}

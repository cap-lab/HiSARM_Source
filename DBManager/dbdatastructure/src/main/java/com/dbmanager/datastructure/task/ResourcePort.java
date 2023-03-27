package com.dbmanager.datastructure.task;

public class ResourcePort implements Communication {
    private String name;
    private PortDirection direction;
    private int sampleSize;

    @Override
    public CommunicationType getCommunicationType() {
        return CommunicationType.RESOURCE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PortDirection getDirection() {
        return direction;
    }

    public void setDirection(PortDirection direction) {
        this.direction = direction;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }
}

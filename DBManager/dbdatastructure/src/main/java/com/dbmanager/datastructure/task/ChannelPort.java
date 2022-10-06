package com.dbmanager.datastructure.task;

public class ChannelPort implements Communication {
    private String name;
    private PortDirection direction;
    private int sampleSize;
    private int index;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CommunicationType getCommunicationType() {
        return CommunicationType.CHANNEL;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}

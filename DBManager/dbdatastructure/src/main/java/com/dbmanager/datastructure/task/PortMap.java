package com.dbmanager.datastructure.task;

public class PortMap {
    private String outsidePort;
    private String insideTask;
    private String insidePort;
    private PortDirection direction;

    public String getOutsidePort() {
        return outsidePort;
    }

    public void setOutsidePort(String outsidePort) {
        this.outsidePort = outsidePort;
    }

    public String getInsideTask() {
        return insideTask;
    }

    public void setInsideTask(String insideTask) {
        this.insideTask = insideTask;
    }

    public String getInsidePort() {
        return insidePort;
    }

    public void setInsidePort(String insidePort) {
        this.insidePort = insidePort;
    }

    public PortDirection getDirection() {
        return direction;
    }

    public void setDirection(PortDirection direction) {
        this.direction = direction;
    }
}

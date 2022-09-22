package com.dbmanager.datastructure.robot;

public class Connection {
    private String serverDevice;
    private String clientDevice;
    private String medium;

    public String getServerDevice() {
        return serverDevice;
    }

    public void setServerDevice(String serverDevice) {
        this.serverDevice = serverDevice;
    }

    public String getClientDevice() {
        return clientDevice;
    }

    public void setClientDevice(String clientDevice) {
        this.clientDevice = clientDevice;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }
}

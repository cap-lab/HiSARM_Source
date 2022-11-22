package com.metadata.architecture;

import com.dbmanager.datastructure.robot.ConnectionType;
import hopes.cic.xml.ArchitectureDeviceType;
import hopes.cic.xml.ArchitectureElementListType;
import hopes.cic.xml.DeviceConnectionListType;
import hopes.cic.xml.EnvironmentVariableListType;
import hopes.cic.xml.SerialConnectionType;
import hopes.cic.xml.TCPConnectionType;

public class UEMArchitectureDevice extends ArchitectureDeviceType {
    private String deviceName;

    public UEMArchitectureDevice() {
        setRuntime("native");
        setElements(new ArchitectureElementListType());
        setConnections(new DeviceConnectionListType());
        setEnvironmentVariables(new EnvironmentVariableListType());
    }

    public static String makeName(String prefix, String deviceName) {
        return prefix + "_" + deviceName;
    }

    public void setName(String prefix, String deviceName) {
        this.deviceName = deviceName;
        setName(makeName(prefix, deviceName));
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void addTCPConnection(UEMTCPConnection connection) {
        getConnections().getTCPConnection().add(connection);
    }

    public void addUSBConnection(UEMSerialConnection connection) {
        getConnections().getSerialConnection().add(connection);
    }

    public void addUDPConnection(UEMUDPConnection connection) {
        getConnections().getUDPConnection().add(connection);
    }

    public boolean hasConnection(ConnectionType type, boolean isServer_Master) {
        switch (type) {
            case ETHERNET_WIFI:
                for (TCPConnectionType connection : getConnections().getTCPConnection()) {
                    if (connection.getName().equals(UEMTCPConnection.makeName(isServer_Master))) {
                        return true;
                    }
                }
                break;
            case USB:
                for (SerialConnectionType connection : getConnections().getSerialConnection()) {
                    if (connection.getName()
                            .equals(UEMSerialConnection.makeName(isServer_Master))) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }
}

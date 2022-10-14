package com.metadata.generator.architecture;

import hopes.cic.xml.ArchitectureConnectType;
import hopes.cic.xml.ArchitectureConnectionListType;
import hopes.cic.xml.ArchitectureDeviceListType;
import hopes.cic.xml.ArchitectureElementTypeListType;
import hopes.cic.xml.CICArchitectureType;

public class UEMArchitecture extends CICArchitectureType {
    public UEMArchitecture() {
        setConnections(new ArchitectureConnectionListType());
        setDevices(new ArchitectureDeviceListType());
        setElementTypes(new ArchitectureElementTypeListType());
    }

    public UEMArchitectureConnection getConnection(String masterDevice, String masterConnection) {
        for (ArchitectureConnectType connection : getConnections().getConnection()) {
            if (connection.getMaster().equals(masterDevice)
                    && connection.getConnection().equals(masterConnection)) {
                return (UEMArchitectureConnection) connection;
            }
        }
        return null;
    }
}

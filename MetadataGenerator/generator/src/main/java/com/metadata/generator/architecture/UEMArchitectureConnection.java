package com.metadata.generator.architecture;

import hopes.cic.xml.ArchitectureConnectType;
import hopes.cic.xml.ArchitectureConnectionSlaveType;
import hopes.cic.xml.EncryptionType;

public class UEMArchitectureConnection extends ArchitectureConnectType {
    public UEMArchitectureConnection() {
        super();
        setEncryption(EncryptionType.NO);
    }

    public void setMaster(String masterDevice, String masterConnection) {
        setMaster(masterDevice);
        setConnection(masterConnection);
    }

    public void setSlave(String slaveDevice, String slaveConnection) {
        ArchitectureConnectionSlaveType slave = new ArchitectureConnectionSlaveType();
        slave.setDevice(slaveDevice);
        slave.setConnection(slaveConnection);
        getSlave().add(slave);
    }

    public ArchitectureConnectionSlaveType getSlave(String slaveDevice, String slaveConnection) {
        for (ArchitectureConnectionSlaveType slave : getSlave()) {
            if (slave.getDevice().equals(slaveDevice)
                    && slave.getConnection().equals(slaveConnection)) {
                return slave;
            }
        }
        return null;
    }
}

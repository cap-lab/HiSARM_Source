package com.metadata.generator.architecture;

import hopes.cic.xml.MasterSlaveRoleType;
import hopes.cic.xml.NetworkType;
import hopes.cic.xml.SerialConnectionType;

public class UEMSerialConnection extends SerialConnectionType {
    public UEMSerialConnection() {
        super();
        setNetwork(NetworkType.USB);
    }

    public static String makeName(boolean isMaster) {
        return NetworkType.USB.toString() + "_" + convertRole(isMaster).value();
    }

    public void setName(boolean isMaster) {
        super.setName(makeName(isMaster));
    }

    private static MasterSlaveRoleType convertRole(boolean isMaster) {
        if (isMaster == true) {
            return MasterSlaveRoleType.MASTER;
        } else {
            return MasterSlaveRoleType.SLAVE;
        }
    }

    public void setRole(boolean isMaster) {
        setRole(convertRole(isMaster));
    }
}

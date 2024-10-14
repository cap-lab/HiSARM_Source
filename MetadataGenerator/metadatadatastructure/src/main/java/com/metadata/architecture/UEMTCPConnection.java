package com.metadata.architecture;

import java.math.BigInteger;
import hopes.cic.xml.NetworkType;
import hopes.cic.xml.ServerClientRoleType;
import hopes.cic.xml.TCPConnectionType;

public class UEMTCPConnection extends TCPConnectionType {
    public UEMTCPConnection() {
        super();
        setSecure(false);
        setNetwork(NetworkType.ETHERNET_WI_FI);
    }

    public void setPort(int port) {
        setPort(BigInteger.valueOf(port));
    }

    public static String makeName(boolean isServer) {
        return NetworkType.ETHERNET_WI_FI.toString() + "_" + convertRole(isServer).value();
    }

    public void setName(boolean isServer, int index) {
        if (isServer == true) {
            super.setName(makeName(isServer));
        } else {
            super.setName(makeName(isServer) + String.valueOf(index));
        }
    }

    private static ServerClientRoleType convertRole(boolean isServer) {
        if (isServer == true) {
            return ServerClientRoleType.SERVER;
        } else {
            return ServerClientRoleType.CLIENT;
        }
    }

    public void setRole(boolean isServer) {
        setRole(convertRole(isServer));
    }

}

package com.metadata.architecture;

import hopes.cic.xml.NetworkType;
import hopes.cic.xml.UDPConnectionType;

public class UEMUDPConnection extends UDPConnectionType {
    public UEMUDPConnection() {
        setNetwork(NetworkType.ETHERNET_WI_FI);
        setName("Multicast");
        setRole("Multicast");
    }
}

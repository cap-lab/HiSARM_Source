package com.metadata.mapping;

import hopes.cic.xml.MappingMulticastConnectionType;

public class UEMMappingMulticastConnection extends MappingMulticastConnectionType {
    public UEMMappingMulticastConnection() {
        setUDP(new UEMMappingMulticastUDP());
    }
}

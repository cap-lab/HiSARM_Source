package com.metadata.mapping;

import java.math.BigInteger;
import com.metadata.constant.MappingConstant;
import hopes.cic.xml.MappingMulticastConnectionType;
import hopes.cic.xml.MappingMulticastType;
import hopes.cic.xml.MappingMulticastUDPType;

public class UEMMappingMulticast extends MappingMulticastType {
    public UEMMappingMulticast() {
        setConnectionType(new MappingMulticastConnectionType());
    }

    public void setUDP() {
        getConnectionType().setUDP(new MappingMulticastUDPType());
        getConnectionType().getUDP().setIp(MappingConstant.BROADCAST_IP);
        getConnectionType().getUDP()
                .setPort(BigInteger.valueOf(MappingConstant.DEFAULT_MULTICAST_PORT));

    }
}

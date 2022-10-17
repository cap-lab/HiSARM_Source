package com.metadata.generator.mapping;

import java.math.BigInteger;
import com.metadata.generator.constant.MappingConstant;
import hopes.cic.xml.MappingMulticastUDPType;

public class UEMMappingMulticastUDP extends MappingMulticastUDPType {
    public UEMMappingMulticastUDP() {
        setIp(MappingConstant.BROADCAST_IP);
        setPort(BigInteger.valueOf(MappingConstant.DEFAULT_MULTICAST_PORT));
    }

}

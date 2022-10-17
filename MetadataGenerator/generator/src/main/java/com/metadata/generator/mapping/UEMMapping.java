package com.metadata.generator.mapping;

import hopes.cic.xml.CICMappingType;
import hopes.cic.xml.MappingMulticastType;

public class UEMMapping extends CICMappingType {
    public UEMMappingMulticast getMulticast(String groupName) {
        for (MappingMulticastType multicast : getMulticast()) {
            if (multicast.getGroupName().equals(groupName)) {
                return (UEMMappingMulticast) multicast;
            }
        }
        return null;
    }
}

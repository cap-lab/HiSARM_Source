package com.metadata.mapping;

import hopes.cic.xml.CICMappingType;
import hopes.cic.xml.MappingMulticastType;

public class UEMMapping extends CICMappingType {
    private int multicastCount = 0;

    public UEMMappingMulticast getMulticast(String groupName) {
        for (MappingMulticastType multicast : getMulticast()) {
            if (multicast.getGroupName().equals(groupName)) {
                return (UEMMappingMulticast) multicast;
            }
        }
        return null;
    }

    public void addMulticast(String groupName, boolean isExport) {
        if (getMulticast(groupName) == null) {
            UEMMappingMulticast multicast = new UEMMappingMulticast();
            multicast.setGroupName(groupName);
            getMulticast().add(multicast);
            if (isExport == true) {
                multicast.setUDP(multicastCount);
                multicastCount++;
            }
        }
    }
}

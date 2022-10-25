package com.metadata.mapping;

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

    public void addMulticast(String groupName){
        if (getMulticast(groupName) == null){
            UEMMappingMulticast multicast = new UEMMappingMulticast();
            multicast.setGroupName(groupName);
            getMulticast().add(multicast);
        }
    }
}

package com.metadata.generator.algorithm;

import java.math.BigInteger;

import hopes.cic.xml.MulticastGroupType;

public class UEMMulticastGroup extends MulticastGroupType {
    public UEMMulticastGroup(String groupName, int size){
        setGroupName(groupName);
        setSize(BigInteger.valueOf(size));
    }
}

package com.metadata.algorithm;

import java.math.BigInteger;
import hopes.cic.xml.MulticastGroupType;

public class UEMMulticastGroup extends MulticastGroupType {
    private boolean isExport;

    public UEMMulticastGroup(String groupName, int size, int count, boolean isExport) {
        setGroupName(groupName);
        setElementSize(BigInteger.valueOf(size));
        setSize(BigInteger.valueOf(size * count));
        this.isExport = isExport;
    }

    public boolean isExport() {
        return isExport;
    }

}

package com.metadata.algorithm;

import java.math.BigInteger;
import hopes.cic.xml.MulticastGroupType;

public class UEMMulticastGroup extends MulticastGroupType {
    private boolean isExport;

    public UEMMulticastGroup(String groupName, int size, boolean isExport) {
        setGroupName(groupName);
        setSize(BigInteger.valueOf(size));
        this.isExport = isExport;
    }

    public boolean isExport() {
        return isExport;
    }

}

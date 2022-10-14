package com.metadata.generator.architecture;

import java.math.BigInteger;
import hopes.cic.xml.ArchitectureElementType;

public class UEMArchitectureElementType extends ArchitectureElementType {
    public UEMArchitectureElementType(int coreNum, String type, String name) {
        setPoolSize(BigInteger.valueOf(coreNum));
        setType(type);
        setName(name);
    }

    public UEMArchitectureElementType(String type, String name) {
        setType(type);
        setName(name);
    }
}

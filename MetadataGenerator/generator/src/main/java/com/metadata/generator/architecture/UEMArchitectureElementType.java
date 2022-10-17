package com.metadata.generator.architecture;

import java.math.BigInteger;
import hopes.cic.xml.ArchitectureElementCategoryType;
import hopes.cic.xml.ArchitectureElementType;

public class UEMArchitectureElementType extends ArchitectureElementType {
    private ArchitectureElementCategoryType elementType;

    public UEMArchitectureElementType(int coreNum, String type, String name) {
        setPoolSize(BigInteger.valueOf(coreNum));
        setType(type);
        setName(name);
        this.elementType = ArchitectureElementCategoryType.PROCESSOR;
    }

    public UEMArchitectureElementType(String type, String name) {
        setType(type);
        setName(name);
        this.elementType = ArchitectureElementCategoryType.MEMORY;
    }

    public boolean isProcessor() {
        return elementType.equals(ArchitectureElementCategoryType.PROCESSOR) ? true : false;
    }
}

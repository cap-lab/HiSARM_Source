package com.metadata.generator.mapping;

import java.math.BigInteger;
import hopes.cic.xml.MappingProcessorIdType;

public class UEMMappingProcessor extends MappingProcessorIdType {
    public void setLocalId(int id) {
        setLocalId(BigInteger.valueOf(id));
    }
}

package com.metadata.mapping;

import java.util.List;
import hopes.cic.xml.MappingDeviceType;
import hopes.cic.xml.MappingProcessorIdType;
import hopes.cic.xml.MappingSetType;

public class UEMMappingDevice extends MappingDeviceType {
    MappingSetType defaultMappingSet;

    public UEMMappingDevice() {
        super();
        defaultMappingSet = new MappingSetType();
        defaultMappingSet.setName("Default");
        getMappingSet().add(defaultMappingSet);
    }

    public List<MappingProcessorIdType> getProcessor() {
        return defaultMappingSet.getProcessor();
    }
}

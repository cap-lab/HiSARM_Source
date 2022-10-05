package com.metadata.generator.algorithm;

import hopes.cic.xml.PortMapType;
import hopes.cic.xml.PortMapTypeType;

public class UEMPortMap extends PortMapType {
    private String notFlattenedTask;
    private String childNotFlattenedTask;

    public String getNotFlattenedTask() {
        return notFlattenedTask;
    }

    public void setNotFlattenedTask(String notFlattenedTask) {
        this.notFlattenedTask = notFlattenedTask;
    }

    public String getChildNotFlattenedTask() {
        return childNotFlattenedTask;
    }

    public void setChildNotFlattenedTask(String childNotFlattenedTask) {
        this.childNotFlattenedTask = childNotFlattenedTask;
    }

    public UEMPortMap() {
        setType(PortMapTypeType.NORMAL);
    }

}

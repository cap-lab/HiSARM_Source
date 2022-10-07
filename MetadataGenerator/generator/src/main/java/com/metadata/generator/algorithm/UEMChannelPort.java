package com.metadata.generator.algorithm;

import java.math.BigInteger;
import com.dbmanager.datastructure.task.ChannelPort;
import com.dbmanager.datastructure.task.PortDirection;
import com.metadata.generator.constant.AlgorithmConstant;
import hopes.cic.xml.PortDirectionType;
import hopes.cic.xml.PortTypeType;
import hopes.cic.xml.TaskPortType;
import hopes.cic.xml.TaskRateType;

public class UEMChannelPort extends TaskPortType {
    private boolean export;
    private int index;

    public UEMChannelPort() {
        super();
    }

    public void setPortInfo(ChannelPort port) {
        setSampleSize(BigInteger.valueOf(port.getSampleSize()));
        setDirection(convertPortDirection(port.getDirection()));
        setType(PortTypeType.OVERWRITABLE);
        setName(port.getName());
        setIndex(port.getIndex());
        setRate();
    }

    private PortDirectionType convertPortDirection(PortDirection direcrtion) {
        return direction.equals(PortDirection.INPUT) ? PortDirectionType.INPUT
                : PortDirectionType.OUTPUT;
    }

    private void setRate() {
        TaskRateType rate = new TaskRateType();
        rate.setMode(AlgorithmConstant.DEFAULT);
        rate.setRate(BigInteger.ONE);
        getRate().add(rate);

    }

    public void makePortInfo(String name, PortDirectionType direction, int sampleSize) {
        setSampleSize(BigInteger.valueOf(sampleSize));
        setDirection(direction);
        setType(PortTypeType.FIFO);
        setName(name);
        setRate();
    }

    public void setExport(boolean export) {
        this.export = export;
    }

    public boolean getExport() {
        return export;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}

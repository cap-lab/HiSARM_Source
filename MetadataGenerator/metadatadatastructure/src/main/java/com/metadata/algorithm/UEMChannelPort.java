package com.metadata.algorithm;

import java.math.BigInteger;
import com.dbmanager.datastructure.task.ChannelPort;
import com.dbmanager.datastructure.task.PortDirection;
import com.metadata.constant.AlgorithmConstant;
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
        setDefaultRate();
    }

    public static String makePortNameFromCounterPort(String prefix, UEMChannelPort port) {
        return prefix + "_" + port.getName();
    }

    public void setPortInfo(String prefix, UEMChannelPort counterPort) {
        setName(makePortNameFromCounterPort(prefix, counterPort));
        setDirection(counterPort.getDirection().equals(PortDirectionType.INPUT)
                ? PortDirectionType.OUTPUT
                : PortDirectionType.INPUT);
        setType(counterPort.getType());
        setSampleSize(counterPort.getSampleSize());
        getRate().add(counterPort.getRate().get(0));
        setIndex(counterPort.getIndex());
    }

    private PortDirectionType convertPortDirection(PortDirection direction) {
        return direction.equals(PortDirection.IN) ? PortDirectionType.INPUT
                : PortDirectionType.OUTPUT;
    }

    public void setDefaultRate() {
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
        setDefaultRate();
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

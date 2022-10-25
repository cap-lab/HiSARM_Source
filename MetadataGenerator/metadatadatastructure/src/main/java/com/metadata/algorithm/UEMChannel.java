package com.metadata.algorithm;

import java.math.BigInteger;

import com.metadata.algorithm.task.UEMTask;

import hopes.cic.xml.ChannelPortType;
import hopes.cic.xml.ChannelType;
import hopes.cic.xml.ChannelTypeType;
import hopes.cic.xml.PortDirectionType;

public class UEMChannel extends ChannelType {
    public UEMChannel() {
        setInitialDataSize(BigInteger.ZERO);
    }

    public void setSrc(String task, String port) {
        ChannelPortType src = new ChannelPortType();
        src.setPort(port);
        src.setTask(task);
        getSrc().add(src);
    }

    public void setDst(String task, String port) {
        ChannelPortType dst = new ChannelPortType();
        dst.setPort(port);
        dst.setTask(task);
        getDst().add(dst);
    }

    public void setType(String type) {
        setType(ChannelTypeType.fromValue(type));
    }

    public static UEMChannel makeChannel(UEMTask task, UEMChannelPort port, UEMTask counterTask,
            UEMChannelPort counterPort) {
        UEMChannel channel = new UEMChannel();
        channel.setSampleSize(port.getSampleSize());
        channel.setType(port.getType().value());
        if (port.getDirection().equals(PortDirectionType.INPUT)) {
            channel.setSrc(counterTask.getName(), counterPort.getName());
            channel.setDst(task.getName(), port.getName());
        } else {
            channel.setSrc(task.getName(), port.getName());
            channel.setDst(counterTask.getName(), counterPort.getName());
        }
        return channel;
    }
}

package com.dbmanager.datastructure.architecture;

public class Processor {
    private ProcessorModel processorName;
    private ProcessorType type;
    private int numberOfCores;
    private int clockFrequency;

    public void setProcessorName(String processorName) {
        this.processorName = ProcessorModel.getEnumFromString(processorName);
    }

    public void setType(String type) {
        this.type = ProcessorType.valueOf(type);
    }

    public void setNumberOfCores(int numberOfCores) {
        this.numberOfCores = numberOfCores;
    }

    public void setClockFrequency(int clockFrequency) {
        this.clockFrequency = clockFrequency;
    }

    public ProcessorModel getProcessorName() {
        return processorName;
    }

    public ProcessorType getType() {
        return type;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }

    public int getClockFrequency() {
        return clockFrequency;
    }
}

package com.metadata.algorithm;

public class UEMCommPort extends UEMChannelPort {
    private String counterTeam;
    private String variableName;

    public UEMCommPort() {
        super();
    }

    public String getCounterTeam() {
        return counterTeam;
    }

    public void setCounterTeam(String counterTeam) {
        this.counterTeam = counterTeam;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public void setPortInfo(UEMCommPort port) {
        setDirection(port.getDirection());
        setName(port.getName());
        setSampleSize(port.getSampleSize());
        setSampleType(port.getSampleType());
        setType(port.getType());
        getRate().add(port.getRate().get(0));
    }
}

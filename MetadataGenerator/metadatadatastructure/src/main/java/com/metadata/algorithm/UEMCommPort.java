package com.metadata.algorithm;

import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;

public class UEMCommPort extends UEMChannelPort {
    private String counterTeam;
    private String message;
    private VariableTypeWrapper variable;

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
        return counterTeam + "_" + message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VariableTypeWrapper getVariable() {
        return variable;
    }

    public void setVariable(VariableTypeWrapper variable) {
        this.variable = variable;
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

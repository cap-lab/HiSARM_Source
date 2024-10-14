package com.metadata.algorithm;

import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;

public class UEMCommPort extends UEMChannelPort {
    private String counterTeam;
    private String counterTeamVariable;
    private String message;
    private VariableTypeWrapper variableType;

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

    public VariableTypeWrapper getVariableType() {
        return variableType;
    }

    public void setVariableType(VariableTypeWrapper variableType) {
        this.variableType = variableType;
    }

    public void setPortInfo(UEMCommPort port) {
        setDirection(port.getDirection());
        setName(port.getName());
        setSampleSize(port.getSampleSize());
        setSampleType(port.getSampleType());
        setType(port.getType());
        getRate().add(port.getRate().get(0));
    }

    public String getCounterTeamVariable() {
        return counterTeamVariable;
    }

    public void setCounterTeamVariable(String counterTeamVariable) {
        this.counterTeamVariable = counterTeamVariable;
    }

}

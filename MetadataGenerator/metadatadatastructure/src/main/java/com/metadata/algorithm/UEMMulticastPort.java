package com.metadata.algorithm;

import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.xml.MulticastPortType;

public class UEMMulticastPort extends MulticastPortType {
    private VariableTypeWrapper variableType;
    private String message;

    public VariableTypeWrapper getVariableType() {
        return variableType;
    }

    public void setVariableType(VariableTypeWrapper variableType) {
        this.variableType = variableType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVariableName() {
        return getGroup() + "_" + message;
    }

}

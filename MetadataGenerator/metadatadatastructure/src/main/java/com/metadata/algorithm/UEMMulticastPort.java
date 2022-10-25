package com.metadata.algorithm;

import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.xml.MulticastPortType;

public class UEMMulticastPort extends MulticastPortType {
    private VariableTypeWrapper variable;
    private String message;

    public VariableTypeWrapper getVariable() {
        return variable;
    }

    public void setVariable(VariableTypeWrapper variable) {
        this.variable = variable;
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

    public int getSize() {
        return variable.getVariableType().getCount() * variable.getVariableType().getSize();
    }
}

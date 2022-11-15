package com.strategy.strategydatastructure.wrapper;

import com.dbmanager.datastructure.variable.PrimitiveType;
import com.dbmanager.datastructure.variable.Variable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariableTypeWrapper {
    private Variable variableType;

    public int getSize() {
        return variableType.getCount() * variableType.getSize();
    }

    public static VariableTypeWrapper getEventVariable() {
        VariableTypeWrapper variableTypeWrapper = new VariableTypeWrapper();
        Variable variable = new Variable();
        variable.setCount(1);
        variable.setSize(4);
        variable.setName("EVENT");
        variable.setType(PrimitiveType.ENUM);
        variableTypeWrapper.setVariableType(variable);
        return variableTypeWrapper;
    }
}

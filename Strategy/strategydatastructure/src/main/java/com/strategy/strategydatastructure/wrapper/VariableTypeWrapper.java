package com.strategy.strategydatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.variable.PrimitiveType;
import com.dbmanager.datastructure.variable.Variable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariableTypeWrapper {
    private Variable variableType;
    private List<String> defaultValueList = new ArrayList<>();

    public String getName() {
        return variableType.getName();
    }

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

package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeVariableWrapper {
    String id;
    VariableTypeWrapper type;
    List<CodeVariableWrapper> childVariableList = new ArrayList<>();
    String defaultValue;

    public static String makeVariableId(String serviceId, String variableId) {
        return serviceId + "_" + variableId;
    }

    public static String makeVariableId(String serviceId, String statementId, String index,
            String subIndex) {
        return makeVariableId(serviceId, statementId, subIndex) + "_" + subIndex;
    }

    public static String makeVariableId(String serviceId, String statementId, String index) {
        return serviceId + "_" + statementId + "_" + index;
    }

    public VariableTypeWrapper getChildVariableType() {
        if (childVariableList.size() == 0) {
            return null;
        }
        return childVariableList.get(0).getType();
    }
}

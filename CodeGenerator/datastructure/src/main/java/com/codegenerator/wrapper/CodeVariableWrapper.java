package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeVariableWrapper {
    private String id;
    private String name;
    private VariableTypeWrapper type;
    private List<CodeVariableWrapper> childVariableList = new ArrayList<>();
    private String defaultValue;
    private boolean realVariable = false;

    public static String makeVariableId(String first, String second) {
        return first + "_" + second;
    }

    public static String makeVariableId(String first, int index) {
        return makeVariableId(first, String.valueOf(index));
    }

    public static String makeVariableId(String prefix, int statementId, int index) {
        return makeVariableId(makeVariableId(prefix, String.valueOf(statementId)),
                String.valueOf(index));
    }

    public static String makeVariableId(String prefix, int statementId, int index, int subIndex) {
        return makeVariableId(makeVariableId(prefix, statementId, index), String.valueOf(subIndex));
    }

    public static String makeVariableId(String prefix, String middle, int index) {
        return makeVariableId(makeVariableId(prefix, middle), String.valueOf(index));
    }

    public static String makeVariableId(String prefix, String middle, String last, int index) {
        return makeVariableId(makeVariableId(prefix, middle), last, index);
    }

    public VariableTypeWrapper getChildVariableType() {
        if (childVariableList.size() == 0) {
            return null;
        }
        return childVariableList.get(0).getType();
    }
}

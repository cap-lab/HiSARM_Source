package com.strategy.strategydatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.action.Action;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionTypeWrapper {
    private Action action;
    private int actionId;
    private List<VariableTypeWrapper> variableInputList = new ArrayList<>();
    private List<VariableTypeWrapper> variableOutputList = new ArrayList<>();
    private List<VariableTypeWrapper> variableSharedList = new ArrayList<>();

    public boolean isGroupAction() {
        return action.getGroupAction() != null;
    }
}

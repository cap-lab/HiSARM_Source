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
    private boolean groupAction;
    private List<VariableTypeWrapper> variableInputList = new ArrayList<>();
    private List<VariableTypeWrapper> variableOutputList = new ArrayList<>();
    private List<VariableTypeWrapper> variableSharedList = new ArrayList<>();
}

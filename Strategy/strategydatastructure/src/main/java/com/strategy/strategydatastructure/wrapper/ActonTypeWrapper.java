package com.strategy.strategydatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.action.GroupAction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActonTypeWrapper {
    private GroupAction action;
    private List<VariableTypeWrapper> variableList = new ArrayList<>();
}

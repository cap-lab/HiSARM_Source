package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.scriptparser.parserdatastructure.entity.statement.Statement;
import com.scriptparser.parserdatastructure.enumeration.TransitionCondition;
import com.scriptparser.parserdatastructure.util.KeyValueList;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StatementWrapper {
    private Statement statement;
    private ServiceWrapper service;
    private List<VariableWrapper> variableList = new ArrayList<>();
    private KeyValueList<TransitionCondition, StatementWrapper> connectedStatements =
            new KeyValueList<>();
    private List<StatementWrapper> counterStatements = new ArrayList<>();
}

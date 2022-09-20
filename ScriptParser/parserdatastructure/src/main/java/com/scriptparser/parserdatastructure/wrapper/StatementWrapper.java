package com.scriptparser.parserdatastructure.wrapper;

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
    private KeyValueList<TransitionCondition, StatementWrapper> connectedStatements =
            new KeyValueList<>();
}

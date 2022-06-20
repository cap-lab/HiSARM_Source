package com.scriptparser.parserdatastructure.wrapper;

import com.scriptparser.parserdatastructure.entity.statement.ConditionalStatement;
import com.scriptparser.parserdatastructure.entity.statement.DummyStatement;
import java.util.HashMap;
import java.util.Map;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.Statement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.enumeration.TransitionCondition;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StatementWrapper {
    private Statement statement;
    private ServiceWrapper service;
    private Map<TransitionCondition, StatementWrapper> nextStatements = new HashMap<>();

    public <T> T getStatement(Class<T> objectClass) {
        return objectClass.cast(statement);
    }

    static public Class<?> getStatementClass(StatementType statementType) {
        Class<?> cl = null;
        switch (statementType) {
            case ACTION:
                cl = ActionStatement.class;
                break;
            case RECEIVE:
            case SUBSCRIBE:
            case SEND:
            case PUBLISH:
                cl = CommunicationalStatement.class;
                break;
            case IF:
            case REPEAT:
            case LOOP:
                cl = ConditionalStatement.class;
                break;
            case THROW:
                cl = ThrowStatement.class;
                break;
            default:
                cl = DummyStatement.class;
        }

        return cl;
    }

}

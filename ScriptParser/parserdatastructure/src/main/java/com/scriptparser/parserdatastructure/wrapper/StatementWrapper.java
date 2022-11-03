package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ConditionalStatement;
import com.scriptparser.parserdatastructure.entity.statement.Statement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.enumeration.TransitionCondition;
import com.scriptparser.parserdatastructure.util.KeyValueList;
import com.scriptparser.parserdatastructure.util.StatementVisitor;
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

    public void visitStatement(StatementVisitor visitor, int statementIndex) {
        switch (statement.getStatementType()) {
            case ACTION:
                visitor.visitActionStatement(this, (ActionStatement) statement, statementIndex);
                break;
            case IF:
            case LOOP:
            case REPEAT:
                visitor.visitConditionalStatement(this, (ConditionalStatement) statement,
                        statementIndex);
                break;
            case SEND:
            case RECEIVE:
            case PUBLISH:
            case SUBSCRIBE:
                visitor.visitCommunicationalStatement(this, (CommunicationalStatement) statement,
                        statementIndex);
                break;
            case THROW:
                visitor.visitThrowStatement(this, (ThrowStatement) statement, statementIndex);
                break;
            default:
                break;
        }
    }
}

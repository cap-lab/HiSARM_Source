package com.scriptparser.parserdatastructure.util;

import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ConditionalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;

public interface StatementVisitor {
    public void visitActionStatement(StatementWrapper wrapper, ActionStatement statement,
            int statementIndex);

    public void visitCommunicationalStatement(StatementWrapper wrapper,
            CommunicationalStatement statement, int statementIndex);

    public void visitThrowStatement(StatementWrapper wrapper, ThrowStatement statement,
            int statementIndex);

    public void visitConditionalStatement(StatementWrapper wrapper, ConditionalStatement statement,
            int statementIndex);
}

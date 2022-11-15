package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.scriptparser.parserdatastructure.enumeration.TransitionCondition;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeStatementWrapper {
    private StatementWrapper statement;
    private String statementId;
    private List<CodeVariableWrapper> variableList = new ArrayList<CodeVariableWrapper>();
    private List<CodeActionWrapper> actionList = new ArrayList<>();
    private CodeCommunicationWrapper comm = null;
    private CodeConditionalWrapper condition = null;
    private CodeThrowWrapper th = null;

    public static String makeStatementId(String serviceId, int statementIndex) {
        return serviceId + "_" + String.valueOf(statementIndex);
    }

    public CodeStatementWrapper getNextStatement(List<CodeStatementWrapper> statementList,
            String condition) {
        StatementWrapper nextStatement = statement.getConnectedStatements()
                .findFirst(TransitionCondition.valueOf(condition), StatementWrapper.class);
        for (CodeStatementWrapper st : statementList) {
            if (st.getStatement().equals(nextStatement)) {
                return st;
            }
        }
        return null;
    }
}

package com.scriptparser.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import com.scriptparser.grammar.StatementFactory;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.enumeration.TransitionCondition;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;

public class StatementConnector {

    private static boolean isBranchStatement(StatementWrapper statement) {
        StatementType type = statement.getStatement().getStatementType();
        if (type == StatementType.IF || type == StatementType.LOOP
                || type == StatementType.REPEAT) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isComplete(StatementWrapper statement) {
        if (isBranchStatement(statement)) {
            if (statement.getConnectedStatements().size() == 2) {
                return true;
            } else {
                return false;
            }
        } else {
            if (statement.getConnectedStatements().size() == 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static Stack<StatementWrapper> dealWithElseType(
            Stack<StatementWrapper> expressionStack) {
        Stack<StatementWrapper> elseStack = new Stack<StatementWrapper>();
        for (int index = expressionStack.size(); index > 0; index--) {
            if (isBranchStatement(expressionStack.elementAt(index - 1))) {
                break;
            } else {
                elseStack.add(expressionStack.pop());
            }
        }
        return elseStack;
    }

    private static void dealWithCommonType(StatementWrapper statement, int startPoint, int pointer,
            Stack<StatementWrapper> expressionStack) {
        for (int index = startPoint; index > pointer; index--) {
            StatementWrapper previous = expressionStack.elementAt(index - 1);
            if (isBranchStatement(previous)) {
                if (previous.getConnectedStatements().containKey(TransitionCondition.TRUE)) {
                    previous.getConnectedStatements().add(TransitionCondition.FALSE, statement);
                } else {
                    previous.getConnectedStatements().add(TransitionCondition.TRUE, statement);
                }
            } else {
                if (!previous.getConnectedStatements().containKey(TransitionCondition.TRUE)) {
                    previous.getConnectedStatements().add(TransitionCondition.TRUE, statement);
                }
            }
            if (isComplete(previous) && index - 1 != 0) {
                expressionStack.removeElementAt(index - 1);
            }
        }
        expressionStack.push(statement);
    }

    private static void connectStatementTransition(ServiceWrapper service) {
        Stack<StatementWrapper> expressionStack = new Stack<>();
        Stack<Object> compoundStack = new Stack<>();
        Stack<Object> elseStack = new Stack<>();
        Stack<Object> deeperElseStack = new Stack<>();
        int elseflag = 0;
        List<StatementWrapper> removeList = new ArrayList<>();

        for (StatementWrapper statement : service.getStatementList()) {
            if (statement.getStatement().getStatementType() == StatementType.COMPOUND_IN) {
                compoundStack.add(expressionStack);
                expressionStack = new Stack<>();
                if (!elseStack.empty()) {
                    deeperElseStack.add(elseStack.pop());
                    elseflag = 0;
                } else {
                    deeperElseStack.add(null);
                }
                removeList.add(statement);
            } else if (statement.getStatement().getStatementType() == StatementType.COMPOUND_OUT) {
                Stack<StatementWrapper> tmpStack = expressionStack;
                expressionStack = (Stack<StatementWrapper>) compoundStack.pop();
                StatementWrapper first = tmpStack.firstElement();
                tmpStack.remove(first);
                int pointer = elseflag == 2 ? expressionStack.size() - 1 : 0;
                dealWithCommonType(first, expressionStack.size(), pointer, expressionStack);
                expressionStack.addAll(tmpStack);
                tmpStack = (Stack<StatementWrapper>) deeperElseStack.pop();
                if (tmpStack != null) {
                    elseStack.add(tmpStack);
                    elseflag = 1;
                }
                removeList.add(statement);
            } else if (statement.getStatement().getStatementType() == StatementType.ELSE) {
                elseStack.add(dealWithElseType(expressionStack));
                removeList.add(statement);
                elseflag = 3;
            } else {
                int pointer = elseflag == 2 ? expressionStack.size() - 1 : 0;
                dealWithCommonType(statement, expressionStack.size(), pointer, expressionStack);
            }
            if (elseflag == 1) {
                expressionStack.addAll((Stack<StatementWrapper>) elseStack.pop());
            }
            elseflag = Math.max(0, elseflag - 1);
        }

        for (StatementWrapper statement : removeList) {
            service.getStatementList().remove(statement);
        }

        StatementWrapper statement = null;
        if (service.getStatementList().get(service.getStatementList().size() - 1).getStatement()
                .getStatementType() == StatementType.REPEAT) {
            statement = service.getStatementList().get(0);
        } else {
            statement = StatementFactory.makeStatement(StatementType.FINISH, null);
            service.getStatementList().add(statement);
        }
        dealWithCommonType(statement, expressionStack.size(), 0, expressionStack);

    }

    public static void connectStatement(MissionWrapper mission) {
        for (ServiceWrapper service : mission.getServiceList()) {
            connectStatementTransition(service);
        }
    }
}

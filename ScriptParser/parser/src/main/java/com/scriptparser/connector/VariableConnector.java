package com.scriptparser.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.scriptparser.parserdatastructure.entity.common.Condition;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ConditionalStatement;
import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.util.KeyValue;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.scriptparser.parserdatastructure.wrapper.VariableWrapper;

public class VariableConnector {
    private static Map<String, VariableWrapper> publishedVariable = new HashMap<>();
    private static Map<String, VariableWrapper> sendedVariable = new HashMap<>();

    public static void connectVariables(MissionWrapper mission) {
        for (ServiceWrapper service : mission.getServiceList()) {
            StatementWrapper firstStatement = service.getStatementList().get(0);
            List<StatementWrapper> visitedStatements = new ArrayList<>();
            Set<VariableWrapper> figuredVariables = new HashSet<>();
            traverseStatement(service, firstStatement, figuredVariables, visitedStatements);
        }
    }

    private static List<StatementWrapper> traverseStatement(ServiceWrapper service,
            StatementWrapper statement, Set<VariableWrapper> figuredVariables,
            List<StatementWrapper> visitedStatements) {
        if (visitedStatements.contains(statement)) {
            return visitedStatements;
        } else {
            visitedStatements.add(statement);
        }

        switch (statement.getStatement().getStatementType()) {
            case ACTION:
                extractVariableFromAcionStatement(service, statement, figuredVariables);
                break;
            case RECEIVE:
            case SUBSCRIBE:
                extractVariableFromReceiveStatement(service, statement, figuredVariables);
                break;
            case PUBLISH:
            case SEND:
                extractVariableFromSendStatement(service, statement, figuredVariables);
                break;
            case IF:
            case LOOP:
            case REPEAT:
                extractVariableFromConditionalStatement(service, statement, figuredVariables);
                break;
            default:
                break;
        }

        for (StatementWrapper nextStatement : statement.getConnectedStatements()
                .values(StatementWrapper.class)) {
            traverseStatement(service, nextStatement, figuredVariables, visitedStatements);
        }

        return visitedStatements;
    }

    private static void extractVariableFromAcionStatement(ServiceWrapper service,
            StatementWrapper statement, Set<VariableWrapper> figuredVariables) {
        ActionStatement action = (ActionStatement) statement.getStatement();
        for (Identifier output : action.getOutputList()) {
            VariableWrapper variable = new VariableWrapper();
            variable.setName(output.getId());
            variable.setCreator(new KeyValue<ServiceWrapper, VariableWrapper>(service, variable));
            figuredVariables.add(variable);
            statement.getVariableList().add(variable);
        }

        for (IdentifierSet inputSet : action.getInputList()) {
            for (Identifier input : inputSet.getIdentifierSet()) {
                if (input.getType().equals(IdentifierType.VARIABLE)) {
                    VariableWrapper variable = new VariableWrapper();
                    variable.setName(input.getId());
                    for (VariableWrapper v : figuredVariables) {
                        if (v.getName().equals(variable.getName())) {
                            variable.setCreator(v.getCreator());
                            break;
                        }
                    }
                    statement.getVariableList().add(variable);

                }
            }
        }
    }

    private static void extractVariableFromSendStatement(ServiceWrapper service,
            StatementWrapper statement, Set<VariableWrapper> figuredVariables) {
        CommunicationalStatement cStatement = (CommunicationalStatement) statement.getStatement();
        VariableWrapper variable = new VariableWrapper();
        variable.setName(cStatement.getMessage().getId());
        for (VariableWrapper v : figuredVariables) {
            if (v.getName().equals(variable.getName())) {
                variable.setCreator(v.getCreator());
                break;
            }
        }
        statement.getVariableList().add(variable);
        if (cStatement.getStatementType().equals(StatementType.SEND)) {
            sendedVariable.put(cStatement.getCounterTeam(), variable);
        } else {
            publishedVariable.put(cStatement.getCounterTeam(), variable);
        }
    }

    private static void extractVariableFromConditionalStatement(ServiceWrapper service,
            StatementWrapper statement, Set<VariableWrapper> figuredVariables) {
        ConditionalStatement cStatement = (ConditionalStatement) statement.getStatement();
        if (cStatement.getCondition() != null) {
            traverseCondition(statement, cStatement.getCondition(), figuredVariables);
        }
    }

    private static void dealWithOperand(StatementWrapper statement,
            Set<VariableWrapper> figuredVariables, Identifier operand) {
        if (operand.getType().equals(IdentifierType.VARIABLE)) {
            VariableWrapper variable = new VariableWrapper();
            variable.setName(operand.getId());

            for (VariableWrapper v : figuredVariables) {
                if (v.getName().equals(variable.getName())) {
                    variable.setCreator(v.getCreator());
                    break;
                }
            }

            statement.getVariableList().add(variable);
        }
    }

    private static void traverseCondition(StatementWrapper statement, Condition condition,
            Set<VariableWrapper> figuredVariables) {
        if (condition.isLeaf()) {
            dealWithOperand(statement, figuredVariables, condition.getLeftOperand());
            dealWithOperand(statement, figuredVariables, condition.getRightOperand());
        } else {
            traverseCondition(statement, condition.getLeft(), figuredVariables);
            traverseCondition(statement, condition.getRight(), figuredVariables);
        }
    }

    private static void extractVariableFromReceiveStatement(ServiceWrapper service,
            StatementWrapper statement, Set<VariableWrapper> figuredVariables) {
        CommunicationalStatement cStatement = (CommunicationalStatement) statement.getStatement();
        VariableWrapper variable = new VariableWrapper();
        variable.setName(cStatement.getOutput().getId());
        figuredVariables.add(variable);
        statement.getVariableList().add(variable);
        VariableWrapper message = new VariableWrapper();
        message.setName(cStatement.getMessage().getId());
        figuredVariables.add(message);
        statement.getVariableList().add(message);
        variable.setCreator(new KeyValue<ServiceWrapper, VariableWrapper>(service, message));
        message.setCreator(new KeyValue<ServiceWrapper, VariableWrapper>(service, variable));
    }
}

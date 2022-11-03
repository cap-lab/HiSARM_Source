package com.codegenerator.mapper;

import java.util.ArrayList;
import java.util.List;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.codegenerator.wrapper.CodeStatementWrapper;
import com.codegenerator.wrapper.CodeVariableWrapper;
import com.scriptparser.parserdatastructure.entity.common.Condition;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ConditionalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import com.scriptparser.parserdatastructure.util.ConditionVisitor;
import com.scriptparser.parserdatastructure.util.KeyValue;
import com.scriptparser.parserdatastructure.util.StatementVisitor;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;

public class ServiceStatementMapper {
    private CodeRobotWrapper robot;

    private class VariableListMaker implements StatementVisitor {
        List<CodeVariableWrapper> variableList = new ArrayList<CodeVariableWrapper>();
        CodeServiceWrapper service;

        public VariableListMaker(CodeServiceWrapper service) {
            this.service = service;
        }

        private CodeVariableWrapper findVariable(String id) {
            for (CodeVariableWrapper variable : variableList) {
                if (variable.getId().equals(id)) {
                    return variable;
                }
            }
            return null;
        }

        private void makeVariableListFromActionInput(ActionStatement action, int statementId,
                ActionTypeWrapper actionType, List<IdentifierSet> inputList) {
            for (int inputIndex = 0; inputIndex < inputList.size(); inputIndex++) {
                IdentifierSet input = action.getInputList().get(inputIndex);
                VariableTypeWrapper inputType = actionType.getVariableInputList().get(inputIndex);
                VariableTypeWrapper elementType = null;
                List<CodeVariableWrapper> childVariableList = new ArrayList<CodeVariableWrapper>();
                for (int elementIndex = 0; elementIndex < input.getIdentifierSet()
                        .size(); elementIndex++) {
                    Identifier element = input.getIdentifierSet().get(elementIndex);
                    CodeVariableWrapper childVariable = null;
                    String variableId = null;
                    String defaultValue = null;
                    if (element.getType().equals(IdentifierType.VARIABLE)) {
                        variableId = CodeVariableWrapper.makeVariableId(service.getServiceId(),
                                element.getId());
                        childVariable = findVariable(variableId);
                    } else {
                        variableId = CodeVariableWrapper.makeVariableId(service.getServiceId(),
                                String.valueOf(statementId), String.valueOf(inputIndex),
                                String.valueOf(elementIndex));
                        defaultValue = element.getId();
                    }
                    if (childVariable == null) {
                        childVariable = new CodeVariableWrapper();
                        childVariable.setId(variableId);
                        childVariable.setType(elementType);
                        childVariable.setDefaultValue(defaultValue);
                        variableList.add(childVariable);
                    }
                    childVariableList.add(childVariable);
                }
                CodeVariableWrapper variable = null;
                if (childVariableList.size() > 1) {
                    variable = new CodeVariableWrapper();
                    variable.setId(CodeVariableWrapper.makeVariableId(service.getServiceId(),
                            String.valueOf(statementId), String.valueOf(inputIndex)));
                    variable.setType(inputType);
                    variableList.add(variable);
                } else {
                    variable = childVariableList.get(0);
                }
                if (variable.getChildVariableList().size() == 0) {
                    variable.setChildVariableList(childVariableList);
                }
            }
        }

        private void makeVariableListFromActionOutput(ActionStatement action, int statementId,
                ActionTypeWrapper actionType, List<Identifier> outputList) {
            for (int outputIndex = 0; outputIndex < outputList.size(); outputIndex++) {
                Identifier output = outputList.get(outputIndex);
                CodeVariableWrapper variable = findVariable(
                        CodeVariableWrapper.makeVariableId(service.getServiceId(), output.getId()));
                if (variable == null) {
                    variable = new CodeVariableWrapper();
                    variable.setId(CodeVariableWrapper.makeVariableId(service.getServiceId(),
                            output.getId()));
                    variable.setType(actionType.getVariableOutputList().get(outputIndex));
                    variable.getChildVariableList().add(variable);
                    variableList.add(variable);
                }
            }
        }

        @Override
        public void visitActionStatement(StatementWrapper statement, ActionStatement action,
                int statementId) {
            ActionTypeWrapper actionType;
            try {
                actionType = robot.getRobot().getRobotTask().getRobot()
                        .getActionType(action.getActionName());
                makeVariableListFromActionInput(action, statementId, actionType,
                        action.getInputList());
                makeVariableListFromActionOutput(action, statementId, actionType,
                        action.getOutputList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void visitCommunicationalStatement(StatementWrapper statement,
                CommunicationalStatement comm, int statementId) {
            try {
                Identifier output = comm.getOutput();
                CodeVariableWrapper variable = findVariable(
                        CodeVariableWrapper.makeVariableId(service.getServiceId(), output.getId()));
                if (variable == null) {
                    variable = new CodeVariableWrapper();
                    variable.setId(CodeVariableWrapper.makeVariableId(service.getServiceId(),
                            output.getId()));
                    variable.setType(robot.getRobot().getRobotTask().getRobot().getVariableMap()
                            .get(new KeyValue<ServiceWrapper, String>(service.getService(),
                                    output.getId())));
                    variable.getChildVariableList().add(variable);
                    variableList.add(variable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private class ConditionVariableListMaker implements ConditionVisitor {
            int statementId;

            public ConditionVariableListMaker(int statementId) {
                this.statementId = statementId;
            }

            private CodeVariableWrapper makeVariable(Identifier identifier, int conditionId) {
                CodeVariableWrapper variable = null;
                if (identifier.getType().equals(IdentifierType.VARIABLE)) {
                    variable = findVariable(CodeVariableWrapper
                            .makeVariableId(service.getServiceId(), identifier.getId()));
                    if (variable == null) {
                        variable = new CodeVariableWrapper();
                        variable.setId(CodeVariableWrapper.makeVariableId(service.getServiceId(),
                                identifier.getId()));
                        variable.setType(robot.getRobot().getRobotTask().getRobot().getVariableMap()
                                .get(new KeyValue<ServiceWrapper, String>(service.getService(),
                                        identifier.getId())));
                        variable.getChildVariableList().add(variable);
                        variableList.add(variable);
                    }
                } else {
                    variable = new CodeVariableWrapper();
                    variable.setId(CodeVariableWrapper.makeVariableId(service.getServiceId(),
                            String.valueOf(statementId), String.valueOf(conditionId)));
                    variable.setDefaultValue(identifier.getId());
                    variable.getChildVariableList().add(variable);
                    variableList.add(variable);
                }
                return variable;
            }

            @Override
            public void preConditionFunction(Condition condition, int conditionId) {
                if (condition.isLeaf()) {
                    CodeVariableWrapper left =
                            makeVariable(condition.getLeftOperand(), conditionId);
                    CodeVariableWrapper right =
                            makeVariable(condition.getLeftOperand(), conditionId);
                    if (left.getType() == null) {
                        left.setType(right.getType());
                    } else if (right.getType() == null) {
                        right.setType(left.getType());
                    }
                }
            }

            @Override
            public void postConditionFunction(Condition condition, int conditionId) {}
        }

        @Override
        public void visitConditionalStatement(StatementWrapper statement, ConditionalStatement cond,
                int statementId) {
            try {
                ConditionVariableListMaker maker = new ConditionVariableListMaker(statementId);
                cond.exploreCondition(maker);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void visitThrowStatement(StatementWrapper statement, ThrowStatement th,
                int statementId) {}

    }

    public ServiceStatementMapper(CodeRobotWrapper robot) {
        this.robot = robot;
    }

    public List<CodeStatementWrapper> mapServiceStatement(CodeServiceWrapper service) {
        List<CodeStatementWrapper> statementList = new ArrayList<>();
        for (StatementWrapper statement : service.getService().getStatementList()) {
            CodeStatementWrapper codeStatement = new CodeStatementWrapper();
            codeStatement.setStatement(statement);
            statementList.add(codeStatement);
        }
        VariableListMaker variableListMaker = new VariableListMaker(service);
        service.getService().traverseService(variableListMaker);
        service.setVariableList(variableListMaker.variableList);
        return statementList;
    }
}

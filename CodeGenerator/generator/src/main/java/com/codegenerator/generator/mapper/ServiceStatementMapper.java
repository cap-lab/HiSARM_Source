package com.codegenerator.generator.mapper;

import java.util.ArrayList;
import java.util.List;
import com.codegenerator.wrapper.CodeActionWrapper;
import com.codegenerator.wrapper.CodeCommunicationWrapper;
import com.codegenerator.wrapper.CodeConditionalWrapper;
import com.codegenerator.wrapper.CodePortWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.codegenerator.wrapper.CodeStatementWrapper;
import com.codegenerator.wrapper.CodeThrowWrapper;
import com.codegenerator.wrapper.CodeVariableWrapper;
import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.task.UEMActionTask;
import com.metadata.algorithm.task.UEMControlTask;
import com.scriptparser.parserdatastructure.entity.common.Condition;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ConditionalStatement;
import com.scriptparser.parserdatastructure.entity.statement.Statement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.util.ConditionVisitor;
import com.scriptparser.parserdatastructure.util.KeyValue;
import com.scriptparser.parserdatastructure.util.StatementVisitor;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;

public class ServiceStatementMapper {
    private CodeRobotWrapper robot;

    private class StatementListmaker implements StatementVisitor {
        List<CodeVariableWrapper> variableList = new ArrayList<CodeVariableWrapper>();
        List<CodeStatementWrapper> statementList = new ArrayList<>();
        CodeServiceWrapper service;
        String groupId;

        public StatementListmaker(CodeServiceWrapper service, String groupId) {
            this.service = service;
            this.groupId = groupId;
        }

        private CodeVariableWrapper findVariable(String id) {
            for (CodeVariableWrapper variable : variableList) {
                if (variable.getId().equals(id)) {
                    return variable;
                }
            }
            return null;
        }

        private List<CodeVariableWrapper> makeChildVariableList(IdentifierSet input,
                int statementId, int inputIndex, VariableTypeWrapper elementType) {
            List<CodeVariableWrapper> childVariableList = new ArrayList<>();
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
            return childVariableList;
        }

        private List<CodeVariableWrapper> makeInputListFromActionInput(ActionStatement action,
                int statementId, ActionTypeWrapper actionType) {
            List<IdentifierSet> inputList = action.getInputList();
            List<CodeVariableWrapper> inputVariableList = new ArrayList<>();
            for (int inputIndex = 0; inputIndex < inputList.size(); inputIndex++) {
                IdentifierSet input = action.getInputList().get(inputIndex);
                VariableTypeWrapper inputType = actionType.getVariableInputList().get(inputIndex);
                VariableTypeWrapper elementType = robot.getRobot().getRobotTask().getRobot()
                        .getPrimitiveVariableMap().get(inputType.getVariableType().getType());
                List<CodeVariableWrapper> childVariableList =
                        makeChildVariableList(input, statementId, inputIndex, elementType);
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
                inputVariableList.add(variable);
            }
            return inputVariableList;
        }

        private List<CodeVariableWrapper> makeVariableListFromActionOutput(ActionStatement action,
                int statementId, ActionTypeWrapper actionType) {
            List<CodeVariableWrapper> outputVariableList = new ArrayList<>();
            List<Identifier> outputList = action.getOutputList();
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
                outputVariableList.add(variable);
            }
            return outputVariableList;
        }

        private List<CodeActionWrapper> makeActionImplList(List<UEMActionTask> actionTaskList,
                List<CodeVariableWrapper> inputVariableList,
                List<CodeVariableWrapper> outputVariableList) {
            List<CodeActionWrapper> actionImplList = new ArrayList<>();
            for (UEMActionTask actionTask : actionTaskList) {
                CodeActionWrapper actionImpl = new CodeActionWrapper();
                UEMControlTask controlTask = robot.getRobot().getRobotTask().getControlTask();
                actionImpl.setActionTask(actionTask);
                actionImplList.add(actionImpl);
                for (int inputIndex = 0; inputIndex < inputVariableList.size(); inputIndex++) {
                    CodeVariableWrapper inputVariable = inputVariableList.get(inputIndex);
                    UEMChannelPort inputPort =
                            controlTask.getInputPortList(actionTask).get(inputIndex);
                    CodePortWrapper codePort = new CodePortWrapper();
                    codePort.setPort(inputPort);
                    codePort.setVariable(inputVariable);
                    actionImpl.getInputList().add(codePort);
                }
                for (int outputIndex = 0; outputIndex < outputVariableList.size(); outputIndex++) {
                    CodeVariableWrapper outputVariable = outputVariableList.get(outputIndex);
                    UEMChannelPort outputPort =
                            controlTask.getOutputPortList(actionTask).get(outputIndex);
                    CodePortWrapper codePort = new CodePortWrapper();
                    codePort.setPort(outputPort);
                    codePort.setVariable(outputVariable);
                    actionImpl.getOutputList().add(codePort);
                }
                if (actionTask.getActionImpl().getActionType().isGroupAction()) {
                    CodePortWrapper codePort = new CodePortWrapper();
                    UEMChannelPort group = controlTask.getGroupPort(actionTask);
                    codePort.setPort(group);
                    actionImpl.setGroup(codePort);
                }
            }
            return actionImplList;
        }

        @Override
        public void visitActionStatement(StatementWrapper statement, ActionStatement action,
                int statementId) {
            try {
                ActionTypeWrapper actionType = robot.getRobot().getRobotTask().getRobot()
                        .getActionType(action.getActionName());
                List<CodeVariableWrapper> inputVariableList =
                        makeInputListFromActionInput(action, statementId, actionType);
                List<CodeVariableWrapper> outputVariableList =
                        makeVariableListFromActionOutput(action, statementId, actionType);
                List<UEMActionTask> actionTaskList = robot.getRobot().getRobotTask()
                        .getActionTaskList(service.getServiceId(), action);
                CodeStatementWrapper codeStatement = new CodeStatementWrapper();
                codeStatement.setStatement(statement);
                codeStatement.setStatementId(
                        CodeStatementWrapper.makeStatementId(service.getServiceId(), statementId));
                codeStatement.getVariableList().addAll(inputVariableList);
                codeStatement.getVariableList().addAll(outputVariableList);
                codeStatement.setActionList(
                        makeActionImplList(actionTaskList, inputVariableList, outputVariableList));
                statementList.add(codeStatement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void visitCommunicationalStatement(StatementWrapper statement,
                CommunicationalStatement comm, int statementId) {
            try {
                Identifier variable =
                        comm.getOutput() == null ? comm.getMessage() : comm.getOutput();
                CodeVariableWrapper codeVariable = findVariable(CodeVariableWrapper
                        .makeVariableId(service.getServiceId(), variable.getId()));
                if (codeVariable == null) {
                    codeVariable = new CodeVariableWrapper();
                    codeVariable.setId(CodeVariableWrapper.makeVariableId(service.getServiceId(),
                            variable.getId()));
                    codeVariable.setType(robot.getRobot().getRobotTask().getRobot().getVariableMap()
                            .get(new KeyValue<ServiceWrapper, String>(service.getService(),
                                    variable.getId())));
                    codeVariable.getChildVariableList().add(codeVariable);
                    variableList.add(codeVariable);
                }
                UEMCommPort port = null;
                if (comm.getStatementType().equals(StatementType.RECEIVE)
                        || comm.getStatementType().equals(StatementType.SUBSCRIBE)) {
                    for (UEMChannelPort p : robot.getRobot().getRobotTask().getControlTask()
                            .getInputPortList(robot.getRobot().getRobotTask().getListenTask())) {
                        if (((UEMCommPort) p).getMessage().equals(comm.getMessage())
                                && ((UEMCommPort) p).getCounterTeam()
                                        .equals(comm.getCounterTeam())) {
                            port = (UEMCommPort) p;
                        }
                    }
                } else {
                    for (UEMChannelPort p : robot.getRobot().getRobotTask().getControlTask()
                            .getOutputPortList(robot.getRobot().getRobotTask().getReportTask())) {
                        if (((UEMCommPort) p).getMessage().equals(comm.getMessage())
                                && ((UEMCommPort) p).getCounterTeam()
                                        .equals(comm.getCounterTeam())) {
                            port = (UEMCommPort) p;
                        }
                    }
                }
                CodeStatementWrapper codeStatement = new CodeStatementWrapper();
                codeStatement.getVariableList().add(codeVariable);
                codeStatement.setStatement(statement);
                codeStatement.setStatementId(
                        CodeStatementWrapper.makeStatementId(service.getServiceId(), statementId));
                codeStatement.setComm(new CodeCommunicationWrapper());
                codeStatement.getComm().setPort(new CodePortWrapper());
                codeStatement.getComm().getPort().setVariable(codeVariable);
                codeStatement.getComm().getPort().setPort(port);
                statementList.add(codeStatement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private class ConditionVariableListMaker implements ConditionVisitor {
            CodeConditionalWrapper codeCondition;
            List<CodeVariableWrapper> conditionVariableList = new ArrayList<>();
            int statementId;

            public ConditionVariableListMaker(CodeConditionalWrapper codeCondition,
                    int statementId) {
                this.codeCondition = codeCondition;
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
                    conditionVariableList.add(left);
                    conditionVariableList.add(right);
                    codeCondition.setLeftVariable(left);
                    codeCondition.setRightVariable(right);
                }
            }

            @Override
            public void postConditionFunction(Condition condition, int conditionId) {}
        }

        @Override
        public void visitConditionalStatement(StatementWrapper statement, ConditionalStatement cond,
                int statementId) {
            try {
                CodeConditionalWrapper codeCondition = new CodeConditionalWrapper();
                ConditionVariableListMaker maker =
                        new ConditionVariableListMaker(codeCondition, statementId);
                cond.exploreCondition(maker);
                CodeStatementWrapper codeStatement = new CodeStatementWrapper();
                codeStatement.setStatement(statement);
                codeStatement.setVariableList(maker.conditionVariableList);
                codeStatement.setCondition(codeCondition);
                statementList.add(codeStatement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void visitThrowStatement(StatementWrapper statement, ThrowStatement th,
                int statementId) {
            CodeThrowWrapper codeThrow = new CodeThrowWrapper();
            for (UEMChannelPort p : robot.getRobot().getRobotTask().getControlTask()
                    .getInputPortList(robot.getRobot().getRobotTask().getListenTask())) {
                if (((UEMCommPort) p).getMessage().equals(th.getEvent().getName())
                        && ((UEMCommPort) p).getCounterTeam().equals(groupId)) {
                    CodePortWrapper port = new CodePortWrapper();
                    port.setPort(p);
                    codeThrow.setInPort(port);
                }
            }
            for (UEMChannelPort p : robot.getRobot().getRobotTask().getControlTask()
                    .getOutputPortList(robot.getRobot().getRobotTask().getReportTask())) {
                if (((UEMCommPort) p).getMessage().equals(th.getEvent().getName())
                        && ((UEMCommPort) p).getCounterTeam().equals(groupId)) {
                    CodePortWrapper port = new CodePortWrapper();
                    port.setPort(p);
                    codeThrow.setOutPort(port);
                }
            }
            CodeStatementWrapper codeStatement = new CodeStatementWrapper();
            codeStatement.setStatement(statement);
            codeStatement.setStatementId(
                    CodeStatementWrapper.makeStatementId(service.getServiceId(), statementId));
            codeStatement.setTh(codeThrow);
            statementList.add(codeStatement);
        }

        @Override
        public void visitOtherStatement(StatementWrapper wrapper, Statement statement, int index) {
            CodeStatementWrapper codeStatement = new CodeStatementWrapper();
            codeStatement.setStatement(wrapper);
            codeStatement.setStatementId(
                    CodeStatementWrapper.makeStatementId(service.getServiceId(), index));
            statementList.add(codeStatement);
        }

    }

    public ServiceStatementMapper(CodeRobotWrapper robot) {
        this.robot = robot;
    }

    public void mapServiceStatement(CodeServiceWrapper service) {
        StatementListmaker variableListMaker =
                new StatementListmaker(service, service.getGroupId());
        service.getService().traverseService(variableListMaker);
        service.setVariableList(variableListMaker.variableList);
        service.setStatementList(variableListMaker.statementList);
    }
}

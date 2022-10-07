package com.metadata.generator.algorithm;

import com.metadata.generator.constant.AlgorithmConstant;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.xml.PortDirectionType;

public class UEMReportTask extends UEMCommTask {

    public UEMReportTask(String robotId, String name) {
        super(robotId, name);
    }

    public void addSend(StatementWrapper send, UEMRobotTask robot) {
        CommunicationalStatement statement = (CommunicationalStatement) send.getStatement();
        String portName = makePortName(statement.getCounterTeam(), statement.getMessage().getId());
        if (!existChannelPort(portName)) {
            VariableTypeWrapper variable =
                    robot.getRobot().getVariableList().get(statement.getOutput().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            UEMChannelPort inPort = new UEMChannelPort();
            inPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName, PortDirectionType.INPUT,
                    portSize);
            inPort.setExport(false);
            getPort().add(inPort);
            UEMChannelPort outPort = new UEMChannelPort();
            outPort.makePortInfo(portName, PortDirectionType.OUTPUT, portSize);
            outPort.setExport(true);
            getPort().add(outPort);
        }
    }

    public void addPublish(StatementWrapper subscribe, UEMRobotTask robot) {
        CommunicationalStatement statement = (CommunicationalStatement) subscribe.getStatement();
        String portName = makePortName(statement.getCounterTeam(), statement.getMessage().getId());
        if (!existMulticastPort(portName)) {
            VariableTypeWrapper variable =
                    robot.getRobot().getVariableList().get(statement.getOutput().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            UEMChannelPort inPort = new UEMChannelPort();
            inPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName, PortDirectionType.INPUT,
                    portSize);
            inPort.setExport(false);
            getPort().add(inPort);
            UEMMulticastPort outPort = new UEMMulticastPort();
            outPort.setName(portName);
            outPort.setGroup(robot.getRobot().getGroupList().get(0));
            outPort.setDirection(PortDirectionType.OUTPUT);
            getMulticastPort().add(outPort);
        }
    }

    public void addThrow(StatementWrapper throwEvent, String group) {
        ThrowStatement statement = (ThrowStatement) throwEvent.getStatement();
        String portName = makePortName(group, statement.getEvent().getName());
        if (statement.broadcast) {
            UEMChannelPort inPort = new UEMChannelPort();
            inPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName, PortDirectionType.INPUT,
                    4);
            inPort.setExport(false);
            getPort().add(inPort);
            UEMMulticastPort outPort = new UEMMulticastPort();
            outPort.setName(portName);
            outPort.setGroup(group);
            outPort.setDirection(PortDirectionType.OUTPUT);
            getMulticastPort().add(outPort);
        }
    }
}

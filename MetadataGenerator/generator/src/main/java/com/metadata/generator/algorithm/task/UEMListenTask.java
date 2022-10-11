package com.metadata.generator.algorithm.task;

import com.metadata.generator.algorithm.UEMChannelPort;
import com.metadata.generator.algorithm.UEMCommPort;
import com.metadata.generator.algorithm.UEMLibraryPort;
import com.metadata.generator.algorithm.UEMMulticastPort;
import com.metadata.generator.constant.AlgorithmConstant;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.xml.PortDirectionType;

public class UEMListenTask extends UEMCommTask {

    public UEMListenTask(String robotId, String name) {
        super(robotId, name);
    }

    public void addReceive(StatementWrapper receive, UEMRobotTask robot) {
        CommunicationalStatement statement = (CommunicationalStatement) receive.getStatement();
        String portName = makePortName(statement.getCounterTeam(), statement.getMessage().getId());
        if (!existChannelPort(portName)) {
            VariableTypeWrapper variable =
                    robot.getRobot().getVariableList().get(statement.getOutput().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            UEMCommPort inPort = new UEMCommPort();
            inPort.makePortInfo(portName, PortDirectionType.INPUT, portSize);
            inPort.setExport(true);
            inPort.setCounterTeam(statement.getCounterTeam());
            inPort.setVariableName(statement.getMessage().getId());
            getPort().add(inPort);
            getExportPortList().add(inPort);
            UEMChannelPort outPort = new UEMChannelPort();
            outPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName,
                    PortDirectionType.OUTPUT, portSize);
            outPort.setExport(false);
            getPort().add(outPort);
            getControlPortList().add(outPort);
        }
    }

    public void addSubscribe(StatementWrapper subscribe, UEMRobotTask robot) throws Exception {
        CommunicationalStatement statement = (CommunicationalStatement) subscribe.getStatement();
        String portName = makePortName(statement.getCounterTeam(), statement.getMessage().getId());
        if (!existMulticastPort(portName)) {
            VariableTypeWrapper variable =
                    robot.getRobot().getVariableList().get(statement.getOutput().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            UEMMulticastPort inPort = new UEMMulticastPort();
            inPort.setName(portName);
            inPort.setGroup(robot.getRobot().getGroupList().get(0));
            inPort.setDirection(PortDirectionType.INPUT);
            getMulticastPort().add(inPort);
            UEMChannelPort outPort = new UEMChannelPort();
            outPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName,
                    PortDirectionType.OUTPUT, portSize);
            outPort.setExport(false);
            getPort().add(outPort);
            getControlPortList().add(outPort);
        }
    }

    public void addThrow(StatementWrapper throwEvent, String group) {
        ThrowStatement statement = (ThrowStatement) throwEvent.getStatement();
        String portName = makePortName(group, statement.getEvent().getName());
        if (statement.broadcast && !existMulticastPort(portName)) {
            UEMMulticastPort port = new UEMMulticastPort();
            port.setName(portName);
            port.setGroup(group);
            port.setDirection(PortDirectionType.INPUT);
            getMulticastPort().add(port);
            UEMChannelPort outPort = new UEMChannelPort();
            outPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName,
                    PortDirectionType.OUTPUT, 4);
            outPort.setExport(false);
            getPort().add(outPort);
            getControlPortList().add(outPort);
        }
    }

    public void addSharedData(String dataId) {
        String portName = dataId;
        if (!existMulticastPort(portName)) {
            UEMMulticastPort inPort = new UEMMulticastPort();
            inPort.setName(portName);
            inPort.setGroup(portName);
            inPort.setDirection(PortDirectionType.INPUT);
            getMulticastPort().add(inPort);
            UEMLibraryPort outPort = new UEMLibraryPort();
            outPort.setName(portName);
            outPort.setType(portName);
            getLibraryMasterPort().add(outPort);
        }
    }
}

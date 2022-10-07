package com.metadata.generator.algorithm;

import com.metadata.generator.constant.AlgorithmConstant;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import com.strategy.strategymaker.constant.StrategyConstant;
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
            UEMChannelPort inPort = new UEMChannelPort();
            inPort.makePortInfo(portName, PortDirectionType.INPUT, portSize);
            inPort.setExport(true);
            getPort().add(inPort);
            UEMChannelPort outPort = new UEMChannelPort();
            outPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName,
                    PortDirectionType.OUTPUT, portSize);
            outPort.setExport(false);
            getPort().add(outPort);
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
        }
    }

    public void addSharedData(String dataId) {
        String portName = dataId;
        if (!existMulticastPort(portName)) {
            UEMMulticastPort port = new UEMMulticastPort();
            port.setName(portName);
            port.setGroup(portName);
            port.setDirection(PortDirectionType.INPUT);
            getMulticastPort().add(port);
            UEMLibraryPort outPort = new UEMLibraryPort();
            outPort.setName(portName);
            outPort.setType(portName);
            getLibraryMasterPort().add(outPort);
        }
    }
}

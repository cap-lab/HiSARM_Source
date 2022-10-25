package com.metadata.algorithm.task;

import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.UEMLibrary;
import com.metadata.algorithm.UEMLibraryPort;
import com.metadata.algorithm.UEMMulticastPort;
import com.metadata.constant.AlgorithmConstant;
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
                    robot.getRobot().getVariableMap().get(statement.getOutput().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            UEMCommPort inPort = new UEMCommPort();
            inPort.makePortInfo(portName, PortDirectionType.INPUT, portSize);
            inPort.setExport(true);
            inPort.setCounterTeam(statement.getCounterTeam());
            inPort.setMessage(statement.getMessage().getId());
            inPort.setVariable(variable);
            getPort().add(inPort);
            getExportPortList().add(inPort);
            UEMChannelPort outPort = new UEMChannelPort();
            outPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName,
                    PortDirectionType.OUTPUT, portSize);
            outPort.setExport(false);
            getPort().add(outPort);
            getControlPortList().add(outPort);
            getChannelPortMap().put(inPort, outPort);
        }
    }

    public void addSubscribe(StatementWrapper subscribe, UEMRobotTask robot) throws Exception {
        CommunicationalStatement statement = (CommunicationalStatement) subscribe.getStatement();
        String portName = makePortName(statement.getCounterTeam(), statement.getMessage().getId());
        if (!existMulticastPort(portName)) {
            VariableTypeWrapper variable =
                    robot.getRobot().getVariableMap().get(statement.getOutput().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            UEMMulticastPort inPort = new UEMMulticastPort();
            inPort.setName(portName);
            inPort.setGroup(robot.getRobot().getTeam());
            inPort.setDirection(PortDirectionType.INPUT);
            inPort.setMessage(statement.getMessage().getId());
            inPort.setVariable(variable);
            getMulticastPort().add(inPort);
            UEMChannelPort outPort = new UEMChannelPort();
            outPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName,
                    PortDirectionType.OUTPUT, portSize);
            outPort.setExport(false);
            getPort().add(outPort);
            getControlPortList().add(outPort);
            getMulticastPortMap().put(inPort, outPort);
        }
    }

    public void addThrow(StatementWrapper throwEvent, String group, UEMRobotTask robot) {
        ThrowStatement statement = (ThrowStatement) throwEvent.getStatement();
        String portName = makePortName(group, statement.getEvent().getName());
        if (statement.broadcast && !existMulticastPort(portName)) {
            UEMMulticastPort inPort = new UEMMulticastPort();
            inPort.setName(portName);
            inPort.setGroup(group);
            inPort.setDirection(PortDirectionType.INPUT);
            inPort.setMessage(statement.getEvent().getName());
            inPort.setVariable(robot.getRobot().getVariableMap().get("EVENT"));
            getMulticastPort().add(inPort);
            UEMChannelPort outPort = new UEMChannelPort();
            outPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName,
                    PortDirectionType.OUTPUT, 4);
            outPort.setExport(false);
            getPort().add(outPort);
            getControlPortList().add(outPort);
            getMulticastPortMap().put(inPort, outPort);
        }
    }

    public void addSharedData(UEMLibrary library) {
        String portName = library.getName();
        if (!existMulticastPort(portName)) {
            UEMMulticastPort inPort = new UEMMulticastPort();
            inPort.setName(portName);
            inPort.setGroup(portName);
            inPort.setDirection(PortDirectionType.INPUT);
            getMulticastPort().add(inPort);
            UEMLibraryPort outPort = new UEMLibraryPort();
            outPort.setName(portName);
            outPort.setType(portName);
            outPort.setLibrary(library);
            getLibraryMasterPort().add(outPort);
            getSharedDataPortMap().put(outPort, inPort);
        }
    }
}

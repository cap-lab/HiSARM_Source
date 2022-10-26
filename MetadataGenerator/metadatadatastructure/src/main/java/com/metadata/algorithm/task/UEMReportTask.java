package com.metadata.algorithm.task;

import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.UEMMulticastPort;
import com.metadata.algorithm.library.UEMLibrary;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.constant.AlgorithmConstant;
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
                    robot.getRobot().getVariableMap().get(statement.getOutput().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            UEMChannelPort inPort = new UEMChannelPort();
            inPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName, PortDirectionType.INPUT,
                    portSize);
            inPort.setExport(false);
            getPort().add(inPort);
            getControlPortList().add(inPort);
            UEMCommPort outPort = new UEMCommPort();
            outPort.makePortInfo(portName, PortDirectionType.OUTPUT, portSize);
            outPort.setExport(true);
            outPort.setCounterTeam(statement.getCounterTeam());
            outPort.setMessage(statement.getMessage().getId());
            outPort.setVariable(variable);
            getPort().add(outPort);
            getExportPortList().add(outPort);
            getChannelPortMap().put(outPort, inPort);
        }
    }

    public void addPublish(StatementWrapper subscribe, UEMRobotTask robot) {
        CommunicationalStatement statement = (CommunicationalStatement) subscribe.getStatement();
        String portName = makePortName(statement.getCounterTeam(), statement.getMessage().getId());
        if (!existMulticastPort(portName)) {
            VariableTypeWrapper variable =
                    robot.getRobot().getVariableMap().get(statement.getOutput().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            UEMChannelPort inPort = new UEMChannelPort();
            inPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName, PortDirectionType.INPUT,
                    portSize);
            inPort.setExport(false);
            getPort().add(inPort);
            getControlPortList().add(inPort);
            UEMMulticastPort outPort = new UEMMulticastPort();
            outPort.setName(portName);
            outPort.setGroup(robot.getRobot().getGroupList().get(0));
            outPort.setDirection(PortDirectionType.OUTPUT);
            outPort.setMessage(statement.getMessage().getId());
            outPort.setVariableType(variable);
            getMulticastPort().add(outPort);
            getMulticastPortMap().put(outPort, inPort);
        }
    }

    public void addThrow(StatementWrapper throwEvent, String group, UEMRobotTask robot) {
        ThrowStatement statement = (ThrowStatement) throwEvent.getStatement();
        String portName = makePortName(group, statement.getEvent().getName());
        if (statement.broadcast) {
            UEMChannelPort inPort = new UEMChannelPort();
            inPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName, PortDirectionType.INPUT,
                    4);
            inPort.setExport(false);
            getPort().add(inPort);
            getControlPortList().add(inPort);
            UEMMulticastPort outPort = new UEMMulticastPort();
            outPort.setName(portName);
            outPort.setGroup(group);
            outPort.setDirection(PortDirectionType.OUTPUT);
            outPort.setMessage(statement.getEvent().getName());
            outPort.setVariableType(robot.getRobot().getVariableMap().get("EVENT"));
            getMulticastPort().add(outPort);
            getMulticastPortMap().put(outPort, inPort);
        }
    }

    public void addSharedData(UEMLibrary library) {
        String portName = library.getName();
        if (!existMulticastPort(portName)) {
            UEMLibraryPort inPort = new UEMLibraryPort();
            inPort.setName(portName);
            inPort.setType(portName);
            inPort.setLibrary(library);
            getLibraryMasterPort().add(inPort);
            UEMMulticastPort outPort = new UEMMulticastPort();
            outPort.setName(portName);
            outPort.setGroup(portName);
            outPort.setDirection(PortDirectionType.OUTPUT);
            getMulticastPort().add(outPort);
            getSharedDataPortMap().put(inPort, outPort);
        }
    }
}

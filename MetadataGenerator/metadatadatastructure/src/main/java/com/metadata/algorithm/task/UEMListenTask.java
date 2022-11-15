package com.metadata.algorithm.task;

import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.UEMMulticastPort;
import com.metadata.algorithm.library.UEMLeaderLibrary;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.algorithm.library.UEMSharedData;
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
            inPort.setVariableType(variable);
            getPort().add(inPort);
            getExportPortList().add(inPort);
            UEMCommPort outPort = new UEMCommPort();
            outPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName,
                    PortDirectionType.OUTPUT, portSize);
            outPort.setExport(false);
            outPort.setCounterTeam(statement.getCounterTeam());
            outPort.setMessage(statement.getMessage().getId());
            outPort.setVariableType(variable);
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
            inPort.setVariableType(variable);
            getMulticastPort().add(inPort);
            UEMCommPort outPort = new UEMCommPort();
            outPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName,
                    PortDirectionType.OUTPUT, portSize);
            outPort.setExport(false);
            outPort.setCounterTeam(robot.getRobot().getTeam());
            outPort.setMessage(statement.getMessage().getId());
            outPort.setVariableType(variable);
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
            inPort.setVariableType(VariableTypeWrapper.getEventVariable());
            getMulticastPort().add(inPort);
            UEMCommPort outPort = new UEMCommPort();
            outPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName,
                    PortDirectionType.OUTPUT, 4);
            outPort.setExport(false);
            outPort.setCounterTeam(group);
            outPort.setMessage(statement.getEvent().getName());
            outPort.setVariableType(VariableTypeWrapper.getEventVariable());
            getPort().add(outPort);
            getControlPortList().add(outPort);
            getMulticastPortMap().put(inPort, outPort);
        }
    }

    public void addSharedData(UEMSharedData library) {
        String portName = library.getGroup();
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
            outPort.setVariableType(library.getVariableType());
            getLibraryMasterPort().add(outPort);
            getSharedDataPortMap().put(outPort, inPort);
        }
    }

    public void addLeaderPort(UEMLeaderLibrary leaderLib) {
        this.leaderPort = new UEMLibraryPort();
        this.leaderPort.setName(AlgorithmConstant.LEADER);
        this.leaderPort.setType(AlgorithmConstant.LEADER);
        this.leaderPort.setLibrary(leaderLib);
        getLibraryMasterPort().add(this.leaderPort);
        leaderLib.getGroupList().forEach(group -> {
            String robotIdPortName = makePortName(group,
                    makePortName(AlgorithmConstant.LEADER, AlgorithmConstant.ROBOT_ID));
            if (!existMulticastPort(robotIdPortName)) {
                UEMMulticastPort robotIdPort = new UEMMulticastPort();
                robotIdPort.setName(robotIdPortName);
                robotIdPort.setGroup(robotIdPortName);
                robotIdPort.setDirection(PortDirectionType.INPUT);
                robotIdPort.setMessage(group);
                getMulticastPort().add(robotIdPort);
                String heartbeatPortName = makePortName(group,
                        makePortName(AlgorithmConstant.LEADER, AlgorithmConstant.HEARTBEAT));
                UEMMulticastPort heartbeatPort = new UEMMulticastPort();
                heartbeatPort.setName(heartbeatPortName);
                heartbeatPort.setGroup(heartbeatPortName);
                heartbeatPort.setDirection(PortDirectionType.INPUT);
                heartbeatPort.setMessage(group);
                getMulticastPort().add(heartbeatPort);
                getLeaderPortMap().put(robotIdPort, heartbeatPort);
            }
        });
    }
}

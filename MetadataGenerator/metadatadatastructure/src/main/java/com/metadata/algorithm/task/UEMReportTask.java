package com.metadata.algorithm.task;

import java.util.Map;
import com.dbmanager.datastructure.variable.PrimitiveType;
import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.UEMMulticastPort;
import com.metadata.algorithm.library.UEMGroupingLibrary;
import com.metadata.algorithm.library.UEMLeaderLibrary;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.algorithm.library.UEMSharedData;
import com.metadata.constant.AlgorithmConstant;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.xml.PortDirectionType;

public class UEMReportTask extends UEMCommTask {

    public UEMReportTask(String robotId, String name) {
        super(robotId, name);
        setMode(50);
    }

    public void addSend(StatementWrapper send, UEMRobotTask robot, ServiceWrapper service,
            Map<String, String> argumentMap) {
        CommunicationalStatement statement = (CommunicationalStatement) send.getStatement();
        String counterTeamName = argumentMap.containsKey(statement.getCounterTeam())
                ? argumentMap.get(statement.getCounterTeam())
                : statement.getCounterTeam();
        String portName = makePortName(counterTeamName, statement.getMessage().getId());
        if (!existChannelPort(portName)) {
            VariableTypeWrapper variable =
                    robot.getRobot().getVariableType(service, statement.getMessage().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            String inPortName = AlgorithmConstant.CONTROL_TASK + portName;
            UEMCommPort inPort = new UEMCommPort();
            inPort.makePortInfo(inPortName, PortDirectionType.INPUT, portSize);
            inPort.setExport(false);
            inPort.setCounterTeam(counterTeamName);
            inPort.setCounterTeamVariable(statement.getCounterTeam());
            inPort.setMessage(statement.getMessage().getId());
            inPort.setVariableType(variable);
            inPort.setCounterTeamVariable(statement.getCounterTeam());
            getPort().add(inPort);
            getControlPortList().add(inPort);
            UEMCommPort outPort = new UEMCommPort();
            outPort.makePortInfo(portName, PortDirectionType.OUTPUT, portSize);
            outPort.setExport(true);
            outPort.setCounterTeam(counterTeamName);
            outPort.setCounterTeamVariable(statement.getCounterTeam());
            outPort.setMessage(statement.getMessage().getId());
            outPort.setVariableType(variable);
            getPort().add(outPort);
            getExportPortList().add(outPort);
            getChannelPortMap().put(inPort, outPort);
        }
    }

    public void addPublish(StatementWrapper publish, UEMRobotTask robot, ServiceWrapper service,
            Map<String, String> argumentMap) throws Exception {
        CommunicationalStatement statement = (CommunicationalStatement) publish.getStatement();
        String counterTeamName = argumentMap.containsKey(statement.getCounterTeam())
                ? argumentMap.get(statement.getCounterTeam())
                : statement.getCounterTeam();
        String portName = makePortName(counterTeamName, statement.getMessage().getId());
        if (!existMulticastPort(portName)) {
            VariableTypeWrapper variable =
                    robot.getRobot().getVariableType(service, statement.getMessage().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            String inPortName = AlgorithmConstant.CONTROL_TASK + portName;
            UEMCommPort inPort = new UEMCommPort();
            inPort.makePortInfo(inPortName, PortDirectionType.INPUT, portSize);
            inPort.setExport(false);
            inPort.setCounterTeam(counterTeamName);
            inPort.setMessage(statement.getMessage().getId());
            inPort.setVariableType(variable);
            inPort.setCounterTeamVariable(statement.getCounterTeam());
            getPort().add(inPort);
            getControlPortList().add(inPort);
            UEMMulticastPort outPort = new UEMMulticastPort();
            outPort.setName(portName);
            outPort.setGroup(robot.getRobot().getTeam() + "_" + counterTeamName + "_"
                    + statement.getMessage().getId());
            outPort.setDirection(PortDirectionType.OUTPUT);
            outPort.setMessage(statement.getMessage().getId());
            outPort.setVariableType(variable);
            getMulticastPort().add(outPort);
            getMulticastPortMap().put(inPort, outPort);
        }
    }

    public void addThrow(StatementWrapper throwEvent, String group, UEMRobotTask robot) {
        ThrowStatement statement = (ThrowStatement) throwEvent.getStatement();
        String portName = makePortName(group, statement.getEvent().getName());
        if (statement.broadcast && !existMulticastPort(portName)) {
            UEMCommPort inPort = new UEMCommPort();
            inPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName, PortDirectionType.INPUT,
                    4);
            inPort.setExport(false);
            inPort.setCounterTeam(group);
            inPort.setMessage(statement.getEvent().getName());
            inPort.setVariableType(
                    robot.getRobot().getPrimitiveVariableMap().get(PrimitiveType.INT32));
            getPort().add(inPort);
            getControlPortList().add(inPort);
            UEMMulticastPort outPort = new UEMMulticastPort();
            outPort.setName(portName);
            outPort.setGroup(group + "_" + AlgorithmConstant.EVENT);
            outPort.setDirection(PortDirectionType.OUTPUT);
            outPort.setMessage(statement.getEvent().getName());
            outPort.setVariableType(
                    robot.getRobot().getPrimitiveVariableMap().get(PrimitiveType.INT32));
            getMulticastPort().add(outPort);
            getMulticastPortMap().put(inPort, outPort);
        }
    }

    public void addSharedData(UEMSharedData library) {
        String portName = library.getGroup();
        if (!existMulticastPort(portName)) {
            UEMLibraryPort inPort = new UEMLibraryPort();
            inPort.setName(portName);
            inPort.setType(portName);
            inPort.setLibrary(library);
            inPort.setVariableType(library.getVariableType());
            getLibraryMasterPort().add(inPort);
            UEMMulticastPort outPort = new UEMMulticastPort();
            outPort.setName(portName);
            outPort.setGroup(portName);
            outPort.setDirection(PortDirectionType.OUTPUT);
            getMulticastPort().add(outPort);
            getSharedDataPortMap().put(inPort, outPort);
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
                robotIdPort.setGroup(
                        group + "_" + AlgorithmConstant.LEADER + "_" + AlgorithmConstant.ROBOT_ID);
                robotIdPort.setDirection(PortDirectionType.OUTPUT);
                robotIdPort.setMessage(group);
                getMulticastPort().add(robotIdPort);
                String heartbeatPortName = makePortName(group,
                        makePortName(AlgorithmConstant.LEADER, AlgorithmConstant.HEARTBEAT));
                UEMMulticastPort heartbeatPort = new UEMMulticastPort();
                heartbeatPort.setName(heartbeatPortName);
                heartbeatPort.setGroup(
                        group + "_" + AlgorithmConstant.LEADER + "_" + AlgorithmConstant.HEARTBEAT);
                heartbeatPort.setDirection(PortDirectionType.OUTPUT);
                heartbeatPort.setMessage(group);
                getMulticastPort().add(heartbeatPort);
                getLeaderPortMap().put(robotIdPort, heartbeatPort);
            }
        });
    }

    public void addGroupActionPort(UEMActionTask action) {
        if (groupActionPort == null) {
            groupActionPort = new UEMLibraryPort();
            groupActionPort.setName(AlgorithmConstant.GROUP_ACTION);
            groupActionPort.setIndex(getLibraryMasterPort().size());
            groupActionPort.setType(AlgorithmConstant.GROUP_ACTION);
            getLibraryMasterPort().add(groupActionPort);
        }
        if (!existMulticastPort(action.getActionName())) {
            UEMMulticastPort groupActionPort = new UEMMulticastPort();
            groupActionPort.setGroup(action.getActionName());
            groupActionPort.setName(action.getActionName());
            groupActionPort.setMessage(String.valueOf(action.getGroupActionIndex()));
            groupActionPort.setDirection(PortDirectionType.OUTPUT);
            getGroupActionPortList().add(groupActionPort);
            getMulticastPort().add(groupActionPort);
        }
    }

    public void addGroupingPort(UEMGroupingLibrary groupingLibrary) {
        this.groupingPort = new UEMLibraryPort();
        this.groupingPort.setName(AlgorithmConstant.GROUPING);
        this.groupingPort.setType(AlgorithmConstant.GROUPING);
        this.groupingPort.setLibrary(groupingLibrary);
        getLibraryMasterPort().add(this.leaderPort);
        groupingLibrary.getGroupMap().keySet().forEach(modeName -> {
            String sharedDataPortName = makePortName(modeName, AlgorithmConstant.GROUPING);
            if (!existMulticastPort(sharedDataPortName)) {
                UEMMulticastPort sharedDataPort = new UEMMulticastPort();
                sharedDataPort.setName(sharedDataPortName);
                sharedDataPort.setGroup(sharedDataPortName);
                sharedDataPort.setDirection(PortDirectionType.OUTPUT);
                getMulticastPort().add(sharedDataPort);
                getGroupingPortList().add(sharedDataPort);
            }
        });
    }
}

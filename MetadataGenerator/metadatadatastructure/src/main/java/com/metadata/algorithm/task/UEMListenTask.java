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

public class UEMListenTask extends UEMCommTask {

    public UEMListenTask(String robotId, String name) {
        super(robotId, name);
        setMode(200);
        setFile(AlgorithmConstant.SEMO + AlgorithmConstant.LISTEN_TASK_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.LISTEN_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.LISTEN_SOURCE_SUFFIX);
    }

    public void addReceive(StatementWrapper receive, UEMRobotTask robot, ServiceWrapper service,
            Map<String, String> argumentMap) {
        CommunicationalStatement statement = (CommunicationalStatement) receive.getStatement();
        String counterTeamName = argumentMap.containsKey(statement.getCounterTeam())
                ? argumentMap.get(statement.getCounterTeam())
                : statement.getCounterTeam();
        String portName = makePortName(counterTeamName, statement.getMessage().getId());
        if (!existChannelPort(portName)) {
            VariableTypeWrapper variable =
                    robot.getRobot().getVariableType(service, statement.getOutput().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            UEMCommPort inPort = new UEMCommPort();
            inPort.makePortInfo(portName, PortDirectionType.INPUT, portSize);
            inPort.setExport(true);
            inPort.setCounterTeam(counterTeamName);
            inPort.setCounterTeamVariable(statement.getCounterTeam());
            inPort.setMessage(statement.getMessage().getId());
            inPort.setVariableType(variable);
            getPort().add(inPort);
            getExportPortList().add(inPort);
            String outPortName = AlgorithmConstant.CONTROL_TASK + portName;
            UEMCommPort outPort = new UEMCommPort();
            outPort.makePortInfo(outPortName, PortDirectionType.OUTPUT, portSize);
            outPort.setExport(false);
            outPort.setCounterTeam(counterTeamName);
            outPort.setMessage(statement.getMessage().getId());
            outPort.setVariableType(variable);
            outPort.setCounterTeamVariable(statement.getCounterTeam());
            getPort().add(outPort);
            getControlPortList().add(outPort);
            getChannelPortMap().put(inPort, outPort);
        }
    }

    public void addSubscribe(StatementWrapper subscribe, UEMRobotTask robot, ServiceWrapper service,
            Map<String, String> argumentMap) throws Exception {
        CommunicationalStatement statement = (CommunicationalStatement) subscribe.getStatement();
        String counterTeamName = argumentMap.containsKey(statement.getCounterTeam())
                ? argumentMap.get(statement.getCounterTeam())
                : statement.getCounterTeam();
        String portName = makePortName(counterTeamName, statement.getMessage().getId());
        if (!existMulticastPort(portName)) {
            VariableTypeWrapper variable =
                    robot.getRobot().getVariableType(service, statement.getOutput().getId());
            int portSize =
                    variable.getVariableType().getSize() * variable.getVariableType().getCount();
            UEMMulticastPort inPort = new UEMMulticastPort();
            inPort.setName(portName);
            inPort.setGroup(counterTeamName + "_" + robot.getRobot().getTeam() + "_"
                    + statement.getMessage().getId());
            inPort.setDirection(PortDirectionType.INPUT);
            inPort.setMessage(statement.getMessage().getId());
            inPort.setVariableType(variable);
            getMulticastPort().add(inPort);
            UEMMulticastPort outMulticastPort = new UEMMulticastPort();
            outMulticastPort.setName(portName + "_out");
            outMulticastPort.setGroup(counterTeamName + "_" + robot.getRobot().getTeam() + "_"
                    + statement.getMessage().getId());
            outMulticastPort.setDirection(PortDirectionType.OUTPUT);
            outMulticastPort.setMessage(statement.getMessage().getId());
            outMulticastPort.setVariableType(variable);
            getMulticastPort().add(outMulticastPort);
            String outPortName = AlgorithmConstant.CONTROL_TASK + portName;
            UEMCommPort outPort = new UEMCommPort();;
            outPort.makePortInfo(outPortName, PortDirectionType.OUTPUT, portSize);
            outPort.setExport(false);
            outPort.setCounterTeam(counterTeamName);
            outPort.setMessage(statement.getMessage().getId());
            outPort.setVariableType(variable);
            outPort.setCounterTeamVariable(statement.getCounterTeam());
            getPort().add(outPort);
            getControlPortList().add(outPort);
            getMulticastPortMap().put(outPort, inPort);
        }
    }

    public void addThrow(StatementWrapper throwEvent, String group, UEMRobotTask robot) {
        ThrowStatement statement = (ThrowStatement) throwEvent.getStatement();
        String portName = makePortName(group, statement.getEvent().getName());
        if (statement.broadcast && !existMulticastPort(portName)) {
            UEMMulticastPort inPort = new UEMMulticastPort();
            inPort.setName(portName);
            inPort.setGroup(group + "_" + AlgorithmConstant.EVENT);
            inPort.setDirection(PortDirectionType.INPUT);
            inPort.setMessage(statement.getEvent().getName());
            inPort.setVariableType(
                    robot.getRobot().getPrimitiveVariableMap().get(PrimitiveType.INT32));
            getMulticastPort().add(inPort);

            UEMMulticastPort outMulticastPort = new UEMMulticastPort();
            outMulticastPort.setName(portName + "_out");
            outMulticastPort.setGroup(group + "_" + AlgorithmConstant.EVENT);
            outMulticastPort.setDirection(PortDirectionType.OUTPUT);
            outMulticastPort.setMessage(statement.getEvent().getName());
            outMulticastPort.setVariableType(
                    robot.getRobot().getPrimitiveVariableMap().get(PrimitiveType.INT32));
            getMulticastPort().add(outMulticastPort);

            UEMCommPort outPort = new UEMCommPort();
            outPort.makePortInfo(AlgorithmConstant.CONTROL_TASK + portName,
                    PortDirectionType.OUTPUT, 4);
            outPort.setExport(false);
            outPort.setCounterTeam(group);
            outPort.setMessage(statement.getEvent().getName());
            outPort.setVariableType(
                    robot.getRobot().getPrimitiveVariableMap().get(PrimitiveType.INT32));
            getPort().add(outPort);
            getControlPortList().add(outPort);
            getMulticastPortMap().put(outPort, inPort);
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

            UEMMulticastPort outMulticastPort = new UEMMulticastPort();
            outMulticastPort.setName(portName + "_out");
            outMulticastPort.setGroup(portName);
            outMulticastPort.setDirection(PortDirectionType.OUTPUT);
            getMulticastPort().add(outMulticastPort);

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
            String selectionInfoPortName = makePortName(group,
                    makePortName(AlgorithmConstant.LEADER, AlgorithmConstant.SELECTION_INFO));
            if (!existMulticastPort(selectionInfoPortName)) {
                UEMMulticastPort selectionInfoPort = new UEMMulticastPort();
                selectionInfoPort.setName(selectionInfoPortName);
                selectionInfoPort.setGroup(group + "_" + AlgorithmConstant.LEADER + "_"
                        + AlgorithmConstant.SELECTION_INFO);
                selectionInfoPort.setDirection(PortDirectionType.INPUT);
                selectionInfoPort.setMessage(group);
                getMulticastPort().add(selectionInfoPort);
                UEMMulticastPort selectionInfoOutPort = new UEMMulticastPort();
                selectionInfoOutPort.setName(selectionInfoPortName + "_out");
                selectionInfoOutPort.setGroup(group + "_" + AlgorithmConstant.LEADER + "_"
                        + AlgorithmConstant.SELECTION_INFO);
                selectionInfoOutPort.setDirection(PortDirectionType.OUTPUT);
                selectionInfoOutPort.setMessage(group);
                getMulticastPort().add(selectionInfoOutPort);
                String heartbeatPortName = makePortName(group,
                        makePortName(AlgorithmConstant.LEADER, AlgorithmConstant.HEARTBEAT));
                UEMMulticastPort heartbeatPort = new UEMMulticastPort();
                heartbeatPort.setName(heartbeatPortName);
                heartbeatPort.setGroup(
                        group + "_" + AlgorithmConstant.LEADER + "_" + AlgorithmConstant.HEARTBEAT);
                heartbeatPort.setDirection(PortDirectionType.INPUT);
                heartbeatPort.setMessage(group);
                getMulticastPort().add(heartbeatPort);
                UEMMulticastPort heartbeatOutPort = new UEMMulticastPort();
                heartbeatOutPort.setName(heartbeatPortName + "_out");
                heartbeatOutPort.setGroup(
                        group + "_" + AlgorithmConstant.LEADER + "_" + AlgorithmConstant.HEARTBEAT);
                heartbeatOutPort.setDirection(PortDirectionType.OUTPUT);
                heartbeatOutPort.setMessage(group);
                getMulticastPort().add(heartbeatOutPort);
                getLeaderPortMap().put(selectionInfoPort, heartbeatPort);
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
            groupActionPort.setDirection(PortDirectionType.INPUT);
            getGroupActionPortList().add(groupActionPort);
            getMulticastPort().add(groupActionPort);
            UEMMulticastPort groupActionOutPort = new UEMMulticastPort();
            groupActionOutPort.setGroup(action.getActionName());
            groupActionOutPort.setName(action.getActionName() + "_out");
            groupActionOutPort.setMessage(String.valueOf(action.getGroupActionIndex()));
            groupActionOutPort.setDirection(PortDirectionType.OUTPUT);
            getMulticastPort().add(groupActionOutPort);
        }
    }

    public void addGroupingPort(UEMGroupingLibrary groupingLibrary) {
        this.groupingPort = new UEMLibraryPort();
        this.groupingPort.setName(AlgorithmConstant.GROUPING);
        this.groupingPort.setType(AlgorithmConstant.GROUPING);
        this.groupingPort.setLibrary(groupingLibrary);
        getLibraryMasterPort().add(this.groupingPort);
        groupingLibrary.getModeSet().forEach(modeName -> {
            String sharedDataPortName = makePortName(modeName, AlgorithmConstant.GROUPING);
            if (!existMulticastPort(sharedDataPortName)) {
                UEMMulticastPort sharedDataPort = new UEMMulticastPort();
                sharedDataPort.setName(sharedDataPortName);
                sharedDataPort.setGroup(sharedDataPortName);
                sharedDataPort.setDirection(PortDirectionType.INPUT);
                getMulticastPort().add(sharedDataPort);
                getGroupingPortList().add(sharedDataPort);
                UEMMulticastPort sharedDataOutPort = new UEMMulticastPort();
                sharedDataOutPort.setName(sharedDataPortName + "_out");
                sharedDataOutPort.setGroup(sharedDataPortName);
                sharedDataOutPort.setDirection(PortDirectionType.OUTPUT);
                getMulticastPort().add(sharedDataOutPort);
            }
        });
    }
}

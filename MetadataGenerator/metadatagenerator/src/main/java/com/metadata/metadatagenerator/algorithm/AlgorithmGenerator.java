package com.metadata.metadatagenerator.algorithm;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import com.dbmanager.datastructure.task.PortDirection;
import com.dbmanager.datastructure.task.PortMap;
import com.dbmanager.datastructure.variable.PrimitiveType;
import com.metadata.algorithm.UEMAlgorithm;
import com.metadata.algorithm.UEMChannel;
import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.UEMMulticastPortMap;
import com.metadata.algorithm.UEMPortMap;
import com.metadata.algorithm.UEMTaskGraph;
import com.metadata.algorithm.library.UEMGroupAction;
import com.metadata.algorithm.library.UEMGroupingLibrary;
import com.metadata.algorithm.library.UEMLeaderLibrary;
import com.metadata.algorithm.library.UEMLibrary;
import com.metadata.algorithm.library.UEMLibraryConnection;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.algorithm.library.UEMSharedData;
import com.metadata.algorithm.task.UEMActionTask;
import com.metadata.algorithm.task.UEMControlTask;
import com.metadata.algorithm.task.UEMGroupingTask;
import com.metadata.algorithm.task.UEMLeaderTask;
import com.metadata.algorithm.task.UEMLibraryPortMap;
import com.metadata.algorithm.task.UEMListenTask;
import com.metadata.algorithm.task.UEMReportTask;
import com.metadata.algorithm.task.UEMResourceTask;
import com.metadata.algorithm.task.UEMRobotTask;
import com.metadata.algorithm.task.UEMTask;
import com.metadata.constant.AlgorithmConstant;
import com.metadata.metadatagenerator.constant.MetadataConstant;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ConditionalStatement;
import com.scriptparser.parserdatastructure.entity.statement.Statement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.util.ModeTransitionVisitor;
import com.scriptparser.parserdatastructure.util.StatementVisitor;
import com.scriptparser.parserdatastructure.util.VariableVisitor;
import com.scriptparser.parserdatastructure.wrapper.GroupModeTransitionWrapper;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import com.strategy.strategydatastructure.wrapper.ActionImplWrapper;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import com.strategy.strategydatastructure.wrapper.ControlStrategyWrapper;
import com.strategy.strategydatastructure.wrapper.GroupingAlgorithmWrapper;
import com.strategy.strategydatastructure.wrapper.ResourceWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.wrapper.StrategyWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.exception.CICXMLException;
import hopes.cic.xml.ChannelPortType;
import hopes.cic.xml.PortDirectionType;
import hopes.cic.xml.TaskPortType;
import hopes.cic.xml.handler.CICAlgorithmXMLHandler;

public class AlgorithmGenerator {
    private UEMAlgorithm algorithm;
    private CICAlgorithmXMLHandler handler = new CICAlgorithmXMLHandler();
    private List<String> groupActionList = new ArrayList<>();

    public AlgorithmGenerator() {
        algorithm = new UEMAlgorithm(handler.getAlgorithm());
    }

    public UEMAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void generate(MissionWrapper mission, StrategyWrapper strategy,
            AdditionalInfo additionalInfo, Path targetDir) {
        try {
            for (RobotImplWrapper robot : strategy.getRobotList()) {
                UEMRobotTask robotTask = new UEMRobotTask(robot.getRobot().getRobotId(), robot);
                makeRobotInerGraph(mission, robotTask, additionalInfo, targetDir);
                robotTask.setPort();
                algorithm.addTask(robotTask);
                algorithm.getRobotTaskList().add(robotTask);
            }
            makeRobotInterGraph();
            recursiveCommConvert(algorithm.getUEMPortMapList(), algorithm.getUEMChannelList(),
                    algorithm.getUEMLibraryConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class RobotInerGraphMaker implements ModeTransitionVisitor, VariableVisitor {
        private UEMRobotTask robot;
        private List<ControlStrategyWrapper> controlStrategyList;
        private Path taskServerPrefix;
        private boolean isActionTask;
        private Map<String, Map<String, String>> transitionArgumentMap = new HashMap<>();
        private Map<String, Map<String, String>> modeArgumentMap = new HashMap<>();
        private Stack<List<String>> variableStack = new Stack<>();


        private class TaskMakerForStatement implements StatementVisitor {
            private String groupId;
            private String modeId;
            private Map<String, String> argumentMap;
            private ServiceWrapper service;

            public TaskMakerForStatement(String modeId, String groupId,
                    Map<String, String> argumentMap, ServiceWrapper service) {
                this.groupId = groupId;
                this.modeId = modeId;
                this.argumentMap = argumentMap;
                this.service = service;
            }

            private void convertPortMap(UEMActionTask actionTask, UEMPortMap afterMap,
                    PortMap beforeMap) {
                afterMap.setTask(actionTask.getName());
                afterMap.setNotFlattenedTask(actionTask.getActionImpl().getTask().getTaskId());
                afterMap.setPort(beforeMap.getOutsidePort());
                afterMap.setChildTask(
                        UEMTask.makeName(afterMap.getTask(), beforeMap.getInsideTask()));
                afterMap.setChildNotFlattenedTask(beforeMap.getInsideTask());
                afterMap.setChildTaskPort(beforeMap.getInsidePort());
            }

            private UEMPortMap convertChanelPortMap(UEMActionTask actionTask, PortMap beforeMap) {
                UEMPortMap afterMap = new UEMPortMap();
                convertPortMap(actionTask, afterMap, beforeMap);
                afterMap.setDirection(
                        beforeMap.getDirection().equals(PortDirection.IN) ? PortDirectionType.INPUT
                                : PortDirectionType.OUTPUT);
                return afterMap;
            }

            private UEMLibraryPortMap convertLibraryPortMap(UEMActionTask actionTask,
                    PortMap beforeMap) {
                UEMLibraryPortMap afterMap = new UEMLibraryPortMap();
                convertPortMap(actionTask, afterMap, beforeMap);
                return afterMap;
            }

            private UEMMulticastPortMap convertResourcePortMap(UEMActionTask actionTask,
                    PortMap beforeMap) {
                UEMMulticastPortMap afterMap = new UEMMulticastPortMap();
                convertPortMap(actionTask, afterMap, beforeMap);
                return afterMap;
            }

            private void makeActionTaskPortMap(UEMActionTask actionTask) {
                for (PortMap beforeMap : actionTask.getActionImpl().getTask().getPortMapSet()) {
                    if (beforeMap.getDirection().equals(PortDirection.LIBRARY)) {
                        actionTask.getLibPortMapList()
                                .add(convertLibraryPortMap(actionTask, beforeMap));
                    } else if (beforeMap.getDirection().equals(PortDirection.RESOURCE)) {
                        actionTask.getMulticastPortMapList()
                                .add(convertResourcePortMap(actionTask, beforeMap));
                    } else {
                        actionTask.getPortMapList()
                                .add(convertChanelPortMap(actionTask, beforeMap));
                    }
                }
            }

            private List<UEMTaskGraph> exploreSubGraph(UEMActionTask actionTask,
                    Path taskServerPrefix) {
                List<UEMTaskGraph> taskGraphList =
                        recursiveExplore(actionTask, robot, taskServerPrefix);
                makeActionTaskPortMap(actionTask);
                return taskGraphList;
            }

            private void makeSubGraphOfAction(UEMActionTask actionTask, Path taskServerPrefix) {
                actionTask.setSubTaskGraphs(exploreSubGraph(actionTask, taskServerPrefix));
                for (UEMTaskGraph taskGraph : actionTask.getSubTaskGraphs()) {
                    algorithm.addAllTasks(taskGraph.getTaskList());
                    algorithm.addAllLibraries(taskGraph.getLibraryList());
                    algorithm.getAlgorithm().getChannels().getChannel()
                            .addAll(taskGraph.getChannelList());
                    algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                            .addAll(taskGraph.getLibraryConnectionList());
                    algorithm.addAllPortMaps(actionTask.getPortMapList());
                }
            }

            @Override
            public void visitActionStatement(StatementWrapper statement, ActionStatement action,
                    int statementIndex) {
                if (isActionTask == false) {
                    return;
                }
                for (ControlStrategyWrapper controlStrategy : controlStrategyList) {
                    if (controlStrategy.getControlStrategy().getActionName()
                            .equals(action.getActionName())) {
                        for (ActionImplWrapper actionImpl : controlStrategy.getActionList()) {
                            UEMActionTask actionTask = new UEMActionTask(robot.getName(), modeId,
                                    statement.getService().getService().getName(), actionImpl,
                                    action);
                            if (actionImpl.getTask().isHasSubGraph()) {
                                makeSubGraphOfAction(actionTask, taskServerPrefix);
                            }
                            if (!groupActionList.contains(actionTask.getActionName())) {
                                groupActionList.add(actionTask.getActionName());
                            }
                            actionTask.setGroupActionIndex(
                                    groupActionList.indexOf(actionTask.getActionName()));
                            robot.getActionTaskList().add(actionTask);
                            algorithm.addTask(actionTask);
                        }
                        break;
                    }
                }
            }

            @Override
            public void visitCommunicationalStatement(StatementWrapper statement,
                    CommunicationalStatement comm, int statementIndex) {
                if (isActionTask == true) {
                    return;
                }
                try {
                    if (comm.getStatementType().equals(StatementType.RECEIVE)) {
                        robot.getListenTask().addReceive(statement, robot, service, argumentMap);
                    } else if (statement.getStatement().getStatementType()
                            .equals(StatementType.SUBSCRIBE)) {
                        String counterTeamName = argumentMap.containsKey(comm.getCounterTeam())
                                ? argumentMap.get(comm.getCounterTeam())
                                : comm.getCounterTeam();
                        robot.getListenTask().addSubscribe(statement, robot, service, argumentMap);
                        if (algorithm.getMulticastGroup(counterTeamName) == null) {
                            VariableTypeWrapper variable = robot.getRobot().getVariableType(service,
                                    comm.getOutput().getId());
                            algorithm
                                    .addMulticastGroup(
                                            counterTeamName + "_" + robot.getRobot().getTeam() + "_"
                                                    + comm.getMessage().getId(),
                                            variable.getSize());
                        }
                    } else if (statement.getStatement().getStatementType()
                            .equals(StatementType.SEND)) {
                        robot.getReportTask().addSend(statement, robot, service, argumentMap);
                    } else if (statement.getStatement().getStatementType()
                            .equals(StatementType.PUBLISH)) {
                        robot.getReportTask().addPublish(statement, robot, service, argumentMap);
                        String counterTeamName = argumentMap.containsKey(comm.getCounterTeam())
                                ? argumentMap.get(comm.getCounterTeam())
                                : comm.getCounterTeam();
                        if (algorithm.getMulticastGroup(counterTeamName) == null) {
                            VariableTypeWrapper variable = robot.getRobot().getVariableType(service,
                                    comm.getMessage().getId());
                            algorithm
                                    .addMulticastGroup(
                                            robot.getRobot().getTeam() + "_" + counterTeamName + "_"
                                                    + comm.getMessage().getId(),
                                            variable.getSize());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void visitConditionalStatement(StatementWrapper arg0, ConditionalStatement arg1,
                    int arg2) {}

            @Override
            public void visitThrowStatement(StatementWrapper statement, ThrowStatement th,
                    int statementIndex) {
                if (isActionTask == true) {
                    return;
                }
                robot.getListenTask().addThrow(statement, groupId, robot);
                robot.getReportTask().addThrow(statement, groupId, robot);
                algorithm.addMulticastGroup(groupId + "_" + AlgorithmConstant.EVENT, 4);
            }

            @Override
            public void visitOtherStatement(StatementWrapper wrapper, Statement statement,
                    int index) {}

        }

        public RobotInerGraphMaker(UEMRobotTask robot,
                List<ControlStrategyWrapper> controlStrategyList, Path taskServerPrefix,
                boolean isActionTask) throws Exception {
            transitionArgumentMap.put(robot.getRobot().getTeam(), new HashMap<>());
            variableStack.add(new ArrayList<>());
            this.robot = robot;
            this.controlStrategyList = controlStrategyList;
            this.taskServerPrefix = taskServerPrefix;
            this.isActionTask = isActionTask;
        }

        @Override
        public void visitModeToService(ModeWrapper mode, String modeId,
                ParallelServiceWrapper service, String groupId) {
            TaskMakerForStatement taskMaker = new TaskMakerForStatement(modeId, groupId,
                    service.getService()
                            .makeArgumentMap(service.makeArgumentList(modeArgumentMap.get(modeId))),
                    service.getService());
            service.getService().traverseService(taskMaker);
        }

        @Override
        public void visitModeToTransition(ModeWrapper mode, String modeId,
                GroupModeTransitionWrapper transition, String groupId) {
            variableStack.add(transition.makeArgumentList(modeArgumentMap.get(modeId)));
        }

        @Override
        public void visitTransitionToMode(TransitionWrapper transition, String transitionId,
                ModeWrapper previousMode, String event, TransitionModeWrapper mode,
                String groupId) {
            variableStack.add(mode.makeArgumentList(transitionArgumentMap.get(transitionId)));
        }

        @Override
        public void visitMode(ModeWrapper mode, String modeId, String groupId) {
            modeArgumentMap.put(modeId, mode.makeArgumentMap(variableStack.pop()));
        }

        @Override
        public void visitTransition(TransitionWrapper transition, String transitionId,
                String groupId) {
            transitionArgumentMap.put(transitionId,
                    transition.makeArgumentMap(variableStack.pop()));
        }

    }

    private void makeRobotInterGraph() throws Exception {
        for (UEMRobotTask robotTask : algorithm.getRobotTaskList()) {
            String senderTeam = robotTask.getRobot().getTeam();
            for (TaskPortType p : robotTask.getPort()) {
                UEMCommPort port = (UEMCommPort) p;
                if (port.getDirection().equals(PortDirectionType.OUTPUT)) {
                    for (UEMRobotTask counterRobotTask : algorithm.getRobotTaskList()) {
                        if (counterRobotTask.getRobot().getTeam().equals(port.getCounterTeam())) {
                            for (TaskPortType cp : counterRobotTask.getPort()) {
                                UEMCommPort counterPort = (UEMCommPort) cp;
                                if (counterPort.getDirection().equals(PortDirectionType.INPUT)
                                        && counterPort.getCounterTeam().equals(senderTeam)
                                        && counterPort.getMessage().equals(port.getMessage())) {
                                    UEMChannel channel = UEMChannel.makeChannel(robotTask, port,
                                            counterRobotTask, counterPort);
                                    algorithm.getAlgorithm().getChannels().getChannel()
                                            .add(channel);
                                    algorithm.putRobotConnection(robotTask, counterRobotTask);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void makeRobotInerGraph(MissionWrapper mission, UEMRobotTask robot,
            AdditionalInfo additionalInfo, Path targetDir) throws Exception {
        makeActionTask(mission, robot, robot.getRobot().getControlStrategyList(),
                Paths.get(additionalInfo.getTaskServerPrefix()));
        makeResourceTask(robot, Paths.get(additionalInfo.getTaskServerPrefix()));
        makeGroupingTask(mission, robot, Paths.get(additionalInfo.getTaskServerPrefix()));
        makeLibraryTask(robot);
        makeGroupActionTask(robot);
        makeLeaderTask(robot);
        makeCommunicationTask(mission, robot);
        makeControlTask(mission, robot);
    }

    private boolean convertChannelPortToDirect(ChannelPortType port, List<UEMPortMap> portMapList) {
        String taskName = port.getTask();
        String portName = port.getPort();
        for (UEMPortMap portMap : portMapList) {
            if (portMap.getTask().equals(taskName) && portMap.getPort().equals(portName)) {
                port.setTask(portMap.getChildTask());
                port.setPort(portMap.getChildTaskPort());
                return true;
            }
        }
        return false;
    }

    private boolean convertChannelToDirect(List<UEMPortMap> portMapList,
            List<UEMChannel> channelList) {
        boolean flag = false;
        for (UEMChannel channel : channelList) {
            flag ^= convertChannelPortToDirect(channel.getSrc().get(0), portMapList);
            flag ^= convertChannelPortToDirect(channel.getDst().get(0), portMapList);
        }
        return flag;
    }

    private boolean convertLibraryPortToDirect(UEMLibraryConnection connection,
            List<UEMPortMap> portMapList) {
        String taskName = connection.getMasterTask();
        String portName = connection.getMasterPort();
        for (UEMPortMap portMap : portMapList) {
            if (portMap.getTask().equals(taskName) && portMap.getPort().equals(portName)) {
                connection.setMasterTask(portMap.getChildTask());
                connection.setMasterPort(portMap.getChildTaskPort());
                return true;
            }
        }
        return false;
    }

    private boolean convertLibConnectionToDirect(List<UEMPortMap> portMapList,
            List<UEMLibraryConnection> libConnectionList) {
        boolean flag = false;
        for (UEMLibraryConnection libConnection : libConnectionList) {
            convertLibraryPortToDirect(libConnection, portMapList);
        }
        return flag;

    }

    private void recursiveCommConvert(List<UEMPortMap> portMapList, List<UEMChannel> channelList,
            List<UEMLibraryConnection> libConnectionList) {
        boolean flag = false;
        do {
            flag = false;
            flag ^= convertChannelToDirect(portMapList, channelList);
            flag ^= convertLibConnectionToDirect(portMapList, libConnectionList);
        } while (flag);
    }

    private List<UEMTaskGraph> recursiveExplore(UEMTask task, UEMRobotTask robot,
            Path taskServerPrefix) {
        TaskXMLtoAlgorithm convertor = new TaskXMLtoAlgorithm(robot);
        List<UEMTaskGraph> taskGraphList = new ArrayList<>();
        int exploreIndex = 0;
        taskGraphList.add(convertor.convertTaskXMLtoAlgorithm(2, task.getName(),
                Paths.get(taskServerPrefix.toString(), task.getFile())));
        while (exploreIndex < taskGraphList.size()) {
            UEMTaskGraph taskGraph = taskGraphList.get(exploreIndex);
            for (UEMTask subTask : taskGraph.getTaskList()) {
                if (subTask.getHasSubGraph().equals(AlgorithmConstant.YES)) {
                    taskGraphList.add(convertor.convertTaskXMLtoAlgorithm(taskGraph.getLevel() + 1,
                            subTask.getName(),
                            Paths.get(taskServerPrefix.toString(), subTask.getFile())));
                }
            }
            exploreIndex = exploreIndex + 1;
        }
        return taskGraphList;
    }

    private void makeActionTask(MissionWrapper mission, UEMRobotTask robot,
            List<ControlStrategyWrapper> controlStrategyList, Path taskServerPrefix)
            throws Exception {
        String team = robot.getRobot().getTeam();
        TransitionWrapper transition = mission.getTransition(team);
        RobotInerGraphMaker maker =
                new RobotInerGraphMaker(robot, controlStrategyList, taskServerPrefix, true);
        transition.traverseTransition(new String(), team, new ArrayList<String>(),
                new ArrayList<>(robot.getRobot().getGroupMap().keySet()), maker, maker);
    }

    private void makeResourceTask(UEMRobotTask robot, Path taskServerPrefix) {
        for (ResourceWrapper resource : robot.getRobot().getResourceList()) {
            UEMResourceTask resourceTask =
                    new UEMResourceTask(robot.getName(), resource, resource.getTask());
            if (resource.getTask().isHasSubGraph() == true) {
                resourceTask.getSubTaskGraphs()
                        .addAll(recursiveExplore(resourceTask, robot, taskServerPrefix));
                for (UEMTaskGraph taskGraph : resourceTask.getSubTaskGraphs()) {
                    algorithm.addAllTasks(taskGraph.getTaskList());
                    algorithm.addAllLibraries(taskGraph.getLibraryList());
                    algorithm.getAlgorithm().getChannels().getChannel()
                            .addAll(taskGraph.getChannelList());
                    algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                            .addAll(taskGraph.getLibraryConnectionList());
                }
            }
            robot.getResourceTaskList().add(resourceTask);
            algorithm.addTask(resourceTask);
            algorithm.addInnerMulticastGroup(
                    robot.getName() + "_" + resource.getResource().getResourceId(),
                    resource.getResource().getDataSize());
        }
    }

    private class GroupingModeVisitor implements ModeTransitionVisitor {
        private UEMGroupingLibrary groupingLibrary;

        @Override
        public void visitMode(ModeWrapper mode, String modeId, String groupId) {
            if (mode.getGroupList().size() > 0) {
                groupingLibrary.addGroupMap(modeId, groupId, mode.getGroupList());
            }
        }

        @Override
        public void visitTransition(TransitionWrapper arg0, String arg1, String arg2) {}

        public GroupingModeVisitor(UEMGroupingLibrary groupingLibrary) {
            this.groupingLibrary = groupingLibrary;
        }
    }

    private void makeGroupingTask(MissionWrapper mission, UEMRobotTask robot, Path taskServerPrefix)
            throws Exception {
        GroupingAlgorithmWrapper groupingAlgorithm = robot.getRobot().getGroupingAlgorithm();
        UEMGroupingTask groupingTask = new UEMGroupingTask(robot);
        if (groupingAlgorithm.getRunTimeTask().isHasSubGraph() == true) {
            groupingTask.getSubTaskGraphs()
                    .addAll(recursiveExplore(groupingTask, robot, taskServerPrefix));
            for (UEMTaskGraph taskGraph : groupingTask.getSubTaskGraphs()) {
                algorithm.addAllTasks(taskGraph.getTaskList());
                algorithm.addAllLibraries(taskGraph.getLibraryList());
                algorithm.getAlgorithm().getChannels().getChannel()
                        .addAll(taskGraph.getChannelList());
                algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                        .addAll(taskGraph.getLibraryConnectionList());
            }
        }
        algorithm.addTask(groupingTask);
        robot.setGroupingTask(groupingTask);
        UEMGroupingLibrary groupingLibrary =
                new UEMGroupingLibrary(robot.getRobot(), groupingAlgorithm);
        algorithm.addLibrary(groupingLibrary);
        robot.setGroupingLibrary(groupingLibrary);
        String team = robot.getRobot().getTeam();
        TransitionWrapper transition = mission.getTransition(team);
        GroupingModeVisitor visitor = new GroupingModeVisitor(groupingLibrary);
        transition.traverseTransition(new String(), team, new ArrayList<String>(),
                new ArrayList<>(robot.getRobot().getGroupMap().keySet()), visitor, null);
        UEMLibraryConnection connection = new UEMLibraryConnection();
        connection.setConnection(groupingTask.getName(), groupingTask.getGroupPort(),
                groupingLibrary);
        algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection().add(connection);
    }

    private void makeLibraryTask(UEMRobotTask robot) {
        for (UEMActionTask actionTask : robot.getActionTaskList()) {
            if (actionTask.getActionImpl().getActionType().isGroupAction()) {
                ActionTypeWrapper actionType = actionTask.getActionImpl().getActionType();
                for (int i = 0; i < actionType.getVariableSharedList().size(); i++) {
                    VariableTypeWrapper variable = actionType.getVariableSharedList().get(i);
                    UEMLibraryPort libPort = actionTask.getLibraryPort(i);
                    String libName = UEMLibrary.makeName(robot.getName(), actionTask.getScope(),
                            actionType.getAction().getName() + "_" + String.valueOf(i));
                    UEMSharedData library = robot.getSharedDataTask(libName);
                    if (library == null) {
                        library = new UEMSharedData(robot);
                        library.makeGeneratedLibrary(libName);
                        library.setGroup(UEMSharedData.makeGroup(actionTask.getScope(),
                                actionType.getAction().getName(), i));
                        algorithm.addLibrary(library);
                        robot.getSharedDataTaskList().add(library);
                        library.setVariableType(variable);
                    }
                    UEMLibraryConnection connection = new UEMLibraryConnection();
                    connection.setConnection(actionTask, libPort, library);
                    algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                            .add(connection);
                }
            }
        }
    }

    private void makeGroupActionTask(UEMRobotTask robot) {
        UEMGroupAction groupAction = new UEMGroupAction(robot);
        groupAction.setName(robot.getName(), AlgorithmConstant.GROUP_ACTION);
        algorithm.addLibrary(groupAction);
        robot.setGroupActionTask(groupAction);
        for (UEMActionTask actionTask : robot.getActionTaskList()) {
            if (actionTask.getActionImpl().getActionType().isGroupAction()) {
                if (groupAction.existGroupAction(actionTask) == false) {
                    groupAction.getGroupActionList().add(actionTask);
                }
            }
        }
    }

    private void makeLeaderTask(UEMRobotTask robot) throws Exception {
        UEMLeaderTask leader = new UEMLeaderTask(robot);
        UEMLeaderLibrary leaderLibrary = new UEMLeaderLibrary(robot, leader);
        UEMLibraryConnection connection = new UEMLibraryConnection();
        connection.setConnection(leader, leader.getLeaderPort(), leaderLibrary);
        robot.setLeaderTask(leader);
        robot.setLeaderLibraryTask(leaderLibrary);
        algorithm.addTask(leader);
        algorithm.addLibrary(leaderLibrary);
        algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection().add(connection);
        for (UEMActionTask actionTask : robot.getActionTaskList()) {
            if (actionTask.getLeaderPort() != null) {
                UEMLibraryConnection leaderConnection = new UEMLibraryConnection();
                boolean flag = true;
                String taskName = actionTask.getName();
                while (flag) {
                    flag = false;
                    for (UEMLibraryPortMap portMap : actionTask.getLibPortMapList()) {
                        if (portMap.getTask().equals(taskName)) {
                            taskName = portMap.getChildTask();
                            flag = true;
                            break;
                        }
                    }
                }
                leaderConnection.setConnection(taskName, actionTask.getLeaderPort(), leaderLibrary);
                actionTask.getLeaderPort().setLibrary(leaderLibrary);
                algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                        .add(leaderConnection);
            }
        }
    }

    private void makeSharedDataLibraryConnectionForCommunication(UEMRobotTask robot) {
        for (UEMSharedData library : robot.getSharedDataTaskList()) {
            robot.getListenTask().addSharedData(library);
            UEMLibraryConnection listenConnection = new UEMLibraryConnection();
            listenConnection.setSlaveLibrary(library.getName());
            listenConnection.setMasterPort(library.getName());
            listenConnection.setMasterTask(robot.getListenTask().getName());
            algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                    .add(listenConnection);
            robot.getReportTask().addSharedData(library);
            UEMLibraryConnection reportConnection = new UEMLibraryConnection();
            reportConnection.setSlaveLibrary(library.getName());
            reportConnection.setMasterPort(library.getName());
            reportConnection.setMasterTask(robot.getReportTask().getName());
            algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                    .add(reportConnection);
            algorithm.addMulticastGroup(library.getGroup(), library.getVariableType().getSize());
        }
    }

    private void makeLeaderLibraryConnectionForCommunication(UEMRobotTask robot) {
        UEMLeaderLibrary leaderLibrary = robot.getLeaderLibraryTask();
        if (leaderLibrary != null) {
            robot.getListenTask().addLeaderPort(leaderLibrary);
            UEMLibraryConnection listenConnection = new UEMLibraryConnection();
            listenConnection.setSlaveLibrary(leaderLibrary.getName());
            listenConnection.setMasterPort(AlgorithmConstant.LEADER);
            listenConnection.setMasterTask(robot.getListenTask().getName());
            algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                    .add(listenConnection);
            robot.getReportTask().addLeaderPort(leaderLibrary);
            UEMLibraryConnection reportConnection = new UEMLibraryConnection();
            reportConnection.setSlaveLibrary(leaderLibrary.getName());
            reportConnection.setMasterPort(AlgorithmConstant.LEADER);
            reportConnection.setMasterTask(robot.getReportTask().getName());
            algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                    .add(reportConnection);
            robot.getListenTask().getLeaderPortMap().keySet().forEach(port -> {
                algorithm.addMulticastGroup(port.getGroup(), PrimitiveType.INT32.getSize());
            });
            robot.getListenTask().getLeaderPortMap().values().forEach(port -> {
                algorithm.addMulticastGroup(port.getGroup(), PrimitiveType.INT32.getSize());
            });
        }
    }

    private void makeGroupActionPortForCommunication(UEMRobotTask robot) {
        boolean groupActionExist = false;
        for (UEMActionTask actionTask : robot.getActionTaskList()) {
            if (actionTask.getActionImpl().getActionType().isGroupAction()) {
                groupActionExist = true;
                robot.getListenTask().addGroupActionPort(actionTask);
                robot.getReportTask().addGroupActionPort(actionTask);
                algorithm.addMulticastGroup(actionTask.getActionName(),
                        PrimitiveType.INT32.getSize());
            }
        }
        if (groupActionExist == true) {
            UEMLibraryConnection listenConnection = new UEMLibraryConnection();
            listenConnection.setSlaveLibrary(robot.getGroupActionTask().getName());
            listenConnection.setMasterPort(AlgorithmConstant.GROUP_ACTION);
            listenConnection.setMasterTask(robot.getListenTask().getName());
            algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                    .add(listenConnection);
            UEMLibraryConnection reportConnection = new UEMLibraryConnection();
            reportConnection.setSlaveLibrary(robot.getGroupActionTask().getName());
            reportConnection.setMasterPort(AlgorithmConstant.GROUP_ACTION);
            reportConnection.setMasterTask(robot.getReportTask().getName());
            algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                    .add(reportConnection);
        }
    }

    private void makeGroupingPortForCommunication(UEMRobotTask robot) {
        UEMGroupingLibrary groupingLibrary = robot.getGroupingLibrary();
        if (groupingLibrary != null) {
            robot.getListenTask().addGroupingPort(groupingLibrary);
            UEMLibraryConnection listenConnection = new UEMLibraryConnection();
            listenConnection.setSlaveLibrary(groupingLibrary.getName());
            listenConnection.setMasterPort(AlgorithmConstant.GROUPING);
            listenConnection.setMasterTask(robot.getListenTask().getName());
            algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                    .add(listenConnection);
            robot.getReportTask().addGroupingPort(groupingLibrary);
            UEMLibraryConnection reportConnection = new UEMLibraryConnection();
            reportConnection.setSlaveLibrary(groupingLibrary.getName());
            reportConnection.setMasterPort(AlgorithmConstant.GROUPING);
            reportConnection.setMasterTask(robot.getReportTask().getName());
            algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                    .add(reportConnection);
            robot.getListenTask().getGroupingPortList().forEach(port -> {
                algorithm.addMulticastGroup(port.getGroup(), groupingLibrary.getSharedDataSize());
            });
        }
    }

    private void makeCommunicationTask(MissionWrapper mission, UEMRobotTask robot)
            throws Exception {
        algorithm.addTask(robot.getListenTask());
        algorithm.addTask(robot.getReportTask());
        String team = robot.getRobot().getTeam();
        TransitionWrapper transition = mission.getTransition(team);
        RobotInerGraphMaker maker = new RobotInerGraphMaker(robot, null, null, false);
        transition.traverseTransition(new String(), team, null,
                new ArrayList<>(robot.getRobot().getGroupMap().keySet()), maker, maker);
        makeSharedDataLibraryConnectionForCommunication(robot);
        makeLeaderLibraryConnectionForCommunication(robot);
        makeGroupActionPortForCommunication(robot);
        makeGroupingPortForCommunication(robot);
    }

    private void makeControlTask(MissionWrapper mission, UEMRobotTask robot) {
        UEMControlTask controlTask = new UEMControlTask(robot);
        List<UEMChannel> channelList = new ArrayList<>();
        for (UEMActionTask actionTask : robot.getActionTaskList()) {
            channelList.addAll(controlTask.setActionPortInfo(actionTask));
        }

        UEMListenTask listenTask = robot.getListenTask();
        channelList.addAll(controlTask.setCommPortInfo(listenTask));

        UEMReportTask reportTask = robot.getReportTask();
        channelList.addAll(controlTask.setCommPortInfo(reportTask));

        UEMLeaderTask leaderTask = robot.getLeaderTask();
        channelList.add(
                controlTask.setLeaderPortInfo(leaderTask, robot.getRobot().getGroupMap().size()));

        channelList.addAll(controlTask.setGroupingPortInfo(robot.getGroupingTask()));

        UEMGroupAction groupActionTask = robot.getGroupActionTask();
        if (groupActionTask != null) {
            controlTask.setGroupActionPortInfo(groupActionTask);
            UEMLibraryConnection con = new UEMLibraryConnection();
            con.setMasterTask(controlTask.getName());
            con.setMasterPort(AlgorithmConstant.GROUP_ACTION);
            con.setSlaveLibrary(groupActionTask.getName());
            algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection().add(con);
        }

        boolean flag = true;
        while (flag) {
            if (algorithm.getAlgorithm().getPortMaps() == null) {
                flag = false;
            } else {
                flag = convertChannelToDirect(
                        algorithm.getAlgorithm().getPortMaps().getPortMap().stream()
                                .map(pm -> (UEMPortMap) pm).collect(Collectors.toList()),
                        channelList);
            }
        }
        algorithm.getAlgorithm().getChannels().getChannel().addAll(channelList);
        algorithm.addTask(controlTask);
        robot.setControlTask(controlTask);
    }

    public boolean generateAlgorithmXML(Path rootDirectory, String projectName) {
        try {
            Path filePath = Paths.get(rootDirectory.toString(),
                    projectName + MetadataConstant.ALGORITHM_SUFFIX);
            handler.storeXMLString(filePath.toString());
            return true;
        } catch (CICXMLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

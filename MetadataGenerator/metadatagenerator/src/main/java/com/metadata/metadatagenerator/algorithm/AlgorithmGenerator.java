package com.metadata.metadatagenerator.algorithm;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.dbmanager.datastructure.variable.PrimitiveType;
import com.metadata.algorithm.UEMAlgorithm;
import com.metadata.algorithm.UEMChannel;
import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.UEMPortMap;
import com.metadata.algorithm.UEMTaskGraph;
import com.metadata.algorithm.library.UEMLeaderLibrary;
import com.metadata.algorithm.library.UEMLibrary;
import com.metadata.algorithm.library.UEMLibraryConnection;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.algorithm.library.UEMSharedData;
import com.metadata.algorithm.task.UEMActionTask;
import com.metadata.algorithm.task.UEMControlTask;
import com.metadata.algorithm.task.UEMLeaderTask;
import com.metadata.algorithm.task.UEMListenTask;
import com.metadata.algorithm.task.UEMReportTask;
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
import com.scriptparser.parserdatastructure.util.ModeVisitor;
import com.scriptparser.parserdatastructure.util.StatementVisitor;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import com.strategy.strategydatastructure.wrapper.ActionImplWrapper;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import com.strategy.strategydatastructure.wrapper.ControlStrategyWrapper;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class RobotInerGraphMaker implements ModeVisitor {
        private UEMRobotTask robot;
        private List<ControlStrategyWrapper> controlStrategyList;
        private Path taskServerPrefix;
        private boolean isActionTask;

        private class TaskMakerForStatement implements StatementVisitor {
            private String currentGroup;
            private ModeWrapper mode;

            public TaskMakerForStatement(String currentGroup, ModeWrapper mode) {
                this.currentGroup = currentGroup;
                this.mode = mode;
            }

            private List<UEMTaskGraph> recursiveExplore(UEMActionTask actionTask,
                    Path taskServerPrefix) {
                TaskXMLtoAlgorithm convertor = new TaskXMLtoAlgorithm();
                List<UEMTaskGraph> taskGraphList = new ArrayList<>();
                int exploreIndex = 0;
                taskGraphList.add(convertor.convertTaskXMLtoAlgorithm(2, actionTask.getName(),
                        Paths.get(taskServerPrefix.toString(), actionTask.getFile())));
                while (exploreIndex < taskGraphList.size()) {
                    UEMTaskGraph taskGraph = taskGraphList.get(exploreIndex);
                    for (UEMTask subTask : taskGraph.getTaskList()) {
                        if (subTask.getHasSubGraph().equals(AlgorithmConstant.YES)) {
                            taskGraphList.add(convertor.convertTaskXMLtoAlgorithm(
                                    taskGraph.getLevel() + 1, subTask.getName(),
                                    Paths.get(taskServerPrefix.toString(), subTask.getFile())));
                        }
                    }
                    exploreIndex = exploreIndex + 1;
                }
                return taskGraphList;
            }

            private List<UEMTaskGraph> exploreSubGraph(UEMActionTask actionTask,
                    Path taskServerPrefix) {
                List<UEMTaskGraph> taskGraphList = recursiveExplore(actionTask, taskServerPrefix);
                List<UEMPortMap> portMapList = new ArrayList<>();
                List<UEMChannel> channelList = new ArrayList<>();
                List<UEMLibraryConnection> libConnectionList = new ArrayList<>();

                for (UEMTaskGraph taskGraph : taskGraphList) {
                    for (UEMTask task : taskGraph.getTaskList()) {
                        portMapList.addAll(task.getPortMapList());
                    }
                    channelList.addAll(taskGraph.getChannelList());
                    libConnectionList.addAll(taskGraph.getLibraryConnectionList());
                }

                recursiveCommConvert(portMapList, channelList, libConnectionList);

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
                            UEMActionTask actionTask = new UEMActionTask(robot.getName(),
                                    currentGroup + "_" + mode.getMode().getName(),
                                    statement.getService().getService().getName(), actionImpl,
                                    action);
                            if (actionImpl.getTask().isHasSubGraph()) {
                                makeSubGraphOfAction(actionTask, taskServerPrefix);
                            }
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
                        robot.getListenTask().addReceive(statement, robot);
                    } else if (statement.getStatement().getStatementType()
                            .equals(StatementType.SUBSCRIBE)) {
                        robot.getListenTask().addSubscribe(statement, robot);
                        VariableTypeWrapper variable = robot.getRobot().getVariableMap()
                                .get(statement.getVariableList().get(0).getName());
                        algorithm.addMulticastGroup(robot.getRobot().getTeam(),
                                variable.getVariableType().getSize()
                                        * variable.getVariableType().getCount());
                    } else if (statement.getStatement().getStatementType()
                            .equals(StatementType.SEND)) {
                        robot.getReportTask().addSend(statement, robot);
                    } else if (statement.getStatement().getStatementType()
                            .equals(StatementType.PUBLISH)) {
                        robot.getReportTask().addPublish(statement, robot);
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
                robot.getListenTask().addThrow(statement, currentGroup, robot);
                robot.getReportTask().addThrow(statement, currentGroup, robot);
                algorithm.addMulticastGroup(currentGroup, 4);
            }

            @Override
            public void visitOtherStatement(StatementWrapper wrapper, Statement statement,
                    int index) {}

        }

        public RobotInerGraphMaker(UEMRobotTask robot,
                List<ControlStrategyWrapper> controlStrategyList, Path taskServerPrefix,
                boolean isActionTask) {
            this.robot = robot;
            this.controlStrategyList = controlStrategyList;
            this.taskServerPrefix = taskServerPrefix;
            this.isActionTask = isActionTask;
        }

        @Override
        public void visitMode(ModeWrapper mode, String modeId, String groupId) {
            for (ParallelServiceWrapper service : mode.getServiceList()) {
                TaskMakerForStatement taskMaker = new TaskMakerForStatement(groupId, mode);
                service.getService().traverseService(taskMaker);
            }
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
                                if (counterPort.getCounterTeam().equals(senderTeam)
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
        makeLibraryTask(robot);
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


    private void makeActionTask(MissionWrapper mission, UEMRobotTask robot,
            List<ControlStrategyWrapper> controlStrategyList, Path taskServerPrefix)
            throws Exception {
        String team = robot.getRobot().getTeam();
        TransitionWrapper transition = mission.getTransition(team);
        RobotInerGraphMaker maker =
                new RobotInerGraphMaker(robot, controlStrategyList, taskServerPrefix, true);
        transition.traverseTransition(new String(), team, new ArrayList<String>(),
                new ArrayList<>(robot.getRobot().getGroupMap().keySet()), maker);
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
                        library = new UEMSharedData();
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

    private void makeLeaderTask(UEMRobotTask robot) {
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
                leaderConnection.setConnection(actionTask, actionTask.getLeaderPort(),
                        leaderLibrary);
                actionTask.getLeaderPort().setLibrary(leaderLibrary);
                algorithm.getAlgorithm().getLibraryConnections().getTaskLibraryConnection()
                        .add(leaderConnection);
            }
        }
    }

    private void makeCommunicationTask(MissionWrapper mission, UEMRobotTask robot)
            throws Exception {
        algorithm.addTask(robot.getListenTask());
        algorithm.addTask(robot.getReportTask());
        String team = robot.getRobot().getTeam();
        TransitionWrapper transition = mission.getTransition(team);
        RobotInerGraphMaker maker = new RobotInerGraphMaker(robot, null, null, false);
        transition.traverseTransition(new String(), team, new ArrayList<String>(),
                new ArrayList<>(robot.getRobot().getGroupMap().keySet()), maker);
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
            algorithm.addMulticastGroup(library.getGroup(),
                    library.getVariableType().getVariableType().getSize()
                            * library.getVariableType().getVariableType().getCount());
        }
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
            reportConnection.setMasterPort(leaderLibrary.getName());
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

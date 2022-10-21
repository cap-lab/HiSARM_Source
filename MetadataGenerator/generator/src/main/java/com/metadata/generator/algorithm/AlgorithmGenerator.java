package com.metadata.generator.algorithm;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.dbmanager.datastructure.task.Task;
import com.dbmanager.datastructure.task.TaskFile;
import com.metadata.generator.algorithm.task.UEMActionTask;
import com.metadata.generator.algorithm.task.UEMControlTask;
import com.metadata.generator.algorithm.task.UEMListenTask;
import com.metadata.generator.algorithm.task.UEMReportTask;
import com.metadata.generator.algorithm.task.UEMRobotTask;
import com.metadata.generator.algorithm.task.UEMTask;
import com.metadata.generator.constant.AlgorithmConstant;
import com.metadata.generator.constant.MetadataConstant;
import com.metadata.generator.util.LocalFileCopier;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import com.strategy.strategydatastructure.wrapper.ActionImplWrapper;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import com.strategy.strategydatastructure.wrapper.ControlStrategyWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.wrapper.StrategyWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import com.strategy.strategymaker.GroupAllocator;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import hopes.cic.exception.CICXMLException;
import hopes.cic.xml.ChannelPortType;
import hopes.cic.xml.PortDirectionType;
import hopes.cic.xml.TaskPortType;
import hopes.cic.xml.handler.CICAlgorithmXMLHandler;

public class AlgorithmGenerator {
    private UEMAlgorithm algorithm = new UEMAlgorithm();

    public UEMAlgorithm getAlgorithm(){
        return algorithm;
    }

    public void generate(MissionWrapper mission, StrategyWrapper strategy,
            AdditionalInfo additionalInfo, Path targetDir) {
        try {
            for(RobotImplWrapper robot : strategy.getRobotList()) {
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

    private void makeRobotInterGraph() {
        for (UEMRobotTask robotTask : algorithm.getRobotTaskList()) {
            String senderTeam = robotTask.getRobot().getGroupList().get(0);
            for (TaskPortType p : robotTask.getPort()) {
                UEMCommPort port = (UEMCommPort) p;
                if (port.getDirection().equals(PortDirectionType.OUTPUT)) {
                    for (UEMRobotTask counterRobotTask : algorithm.getRobotTaskList()) {
                        if (counterRobotTask.getRobot().getGroupList().get(0)
                                .equals(port.getCounterTeam())) {
                            for (TaskPortType cp : counterRobotTask.getPort()) {
                                UEMCommPort counterPort = (UEMCommPort) cp;
                                if (counterPort.getCounterTeam().equals(senderTeam) && counterPort
                                        .getVariableName().equals(port.getVariableName())) {
                                    UEMChannel channel = UEMChannel.makeChannel(robotTask, port,
                                            counterRobotTask, counterPort);
                                    algorithm.getChannels().getChannel().add(channel);
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
                Paths.get(additionalInfo.getTaskServerPrefix()), targetDir);
        makeLibraryTask(robot);
        makeCommunicationTask(mission, robot);
        makeControlTask(mission, robot);
    }

    private void copyFiles(Task task, Path taskServerPrefix, Path targetDir)
            throws Exception {
        for (TaskFile file : task.getTaskFiles()) {
            if (file.isDirectory()) {
                LocalFileCopier.copyDirectory(
                        Paths.get(taskServerPrefix.toString(), file.getPath()), targetDir);
            } else {
                LocalFileCopier.copyBinaryFile(
                        Paths.get(taskServerPrefix.toString(), file.getPath()),
                        Paths.get(targetDir.toString(), file.getPath().substring(file.getPath().lastIndexOf('/')+1)));
            }
        }
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
                    taskGraphList.add(convertor.convertTaskXMLtoAlgorithm(taskGraph.level + 1,
                            subTask.getName(),
                            Paths.get(taskServerPrefix.toString(), subTask.getFile())));
                }
            }
            exploreIndex = exploreIndex + 1;
        }
        return taskGraphList;
    }

    private boolean convertChannelPortToDirect(ChannelPortType port,
            List<UEMPortMap> portMapList) {
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

    private void recursiveCommConvert(List<UEMPortMap> portMapList,
            List<UEMChannel> channelList, List<UEMLibraryConnection> libConnectionList) {
        boolean flag = false;
        do {
            flag = false;
            flag ^= convertChannelToDirect(portMapList, channelList);
            flag ^= convertLibConnectionToDirect(portMapList, libConnectionList);
        } while (flag);
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

    private void visitModeForAction(ModeWrapper mode, UEMRobotTask robot,
            String currentGroup, List<ControlStrategyWrapper> controlStrategyList,
            Path taskServerPrefix, Path targetDir, Set<String> visitedMode) {
        if (visitedMode.contains(currentGroup + mode.getMode().getName())) {
            return;
        }
        mode.getServiceList().forEach(s -> s.getService().getStatementList().forEach(statement -> {
            if (statement.getStatement().getStatementType().equals(StatementType.ACTION)) {
                ActionStatement actionStatement = (ActionStatement) statement.getStatement();
                for (ControlStrategyWrapper controlStrategy : controlStrategyList) {
                    if (controlStrategy.getControlStrategy().getActionName()
                            .equals(actionStatement.getActionName())) {
                        for (ActionImplWrapper action : controlStrategy.getActionList()) {
                            Task task = action.getTask();
                            UEMActionTask actionTask = new UEMActionTask(robot.getName(),
                                    currentGroup + "_" + mode.getMode().getName(), s.getService().getService().getName(), action);
                            try {
                                copyFiles(task, taskServerPrefix, targetDir);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (task.isHasSubGraph()) {
                                actionTask.setSubTaskGraphs(
                                        exploreSubGraph(actionTask, taskServerPrefix));
                                for (UEMTaskGraph taskGraph : actionTask.getSubTaskGraphs()) {
                                    algorithm.getTasks().getTask().addAll(taskGraph.getTaskList());
                                    algorithm.getChannels().getChannel()
                                            .addAll(taskGraph.getChannelList());
                                    algorithm.getLibraries().getLibrary()
                                            .addAll(taskGraph.getLibraryList());
                                    algorithm.getLibraryConnections().getTaskLibraryConnection()
                                            .addAll(taskGraph.getLibraryConnectionList());
                                }
                            }
                            robot.getActionTaskList().add(actionTask);
                            algorithm.addTask(actionTask);
                        }
                        break;
                    }
                }
            }
        }));
        visitedMode.add(currentGroup + mode.getMode().getName());
        mode.getGroupList()
                .forEach(g -> traverseTransitionForAction(g.getModeTransition().getModeTransition(),
                        robot, GroupAllocator.makeGroupKey(currentGroup, g.getGroup().getName()),
                        controlStrategyList, taskServerPrefix, targetDir, visitedMode));
    }

    private void traverseTransitionForAction(TransitionWrapper transition,
            UEMRobotTask robot, String currentGroup,
            List<ControlStrategyWrapper> controlStrategyList, Path taskServerPrefix, Path targetDir,
            Set<String> visitedMode) {
        Set<ModeWrapper> modeSet = new HashSet<>();
        modeSet.add(transition.getDefaultMode().getMode());
        transition.getTransitionMap().values().forEach(list -> list.forEach(ce -> {
            ModeWrapper m = ce.getMode().getMode();
            if (!m.getMode().getName().equals("FINISH")
                    && !m.getMode().getName().equals("PREVIOUS_MODE")) {
                modeSet.add(m);
            }
        }));
        modeSet.forEach(m -> visitModeForAction(m, robot, currentGroup, controlStrategyList,
                taskServerPrefix, targetDir, visitedMode));
    }

    private void makeActionTask(MissionWrapper mission, UEMRobotTask robot,
            List<ControlStrategyWrapper> controlStrategyList, Path taskServerPrefix, Path targetDir)
            throws Exception {
        String team = robot.getRobot().getTeam();
        TransitionWrapper transition = mission.getTransition(team);
        traverseTransitionForAction(transition, robot, team, controlStrategyList, taskServerPrefix,
                targetDir, new HashSet<>());
    }

    private void makeLibraryTask(UEMRobotTask robot) {
        for (UEMActionTask actionTask : robot.getActionTaskList()) {
            if (actionTask.getAction().getAction().isGroupAction()) {
                ActionTypeWrapper actionType = actionTask.getAction().getAction();
                for (int i = 0; i < actionType.getVariableSharedList().size(); i++) {
                    VariableTypeWrapper variable = actionType.getVariableSharedList().get(i);
                    UEMLibraryPort libPort = actionTask.getLibraryPort(i);
                    String libName = UEMLibrary.makeName(robot.getName(), actionTask.getScope(),
                            actionType.getAction().getName() + "_" + String.valueOf(i));
                    UEMLibrary library = robot.getLibraryTask(libName);
                    if (library == null) {
                        library = new UEMLibrary();
                        library.makeGeneratedLibrary(libName);
                        library.setGroup(UEMLibrary.makeGroup(actionTask.getScope(), actionType.getAction().getName(), i));
                        algorithm.getLibraries().getLibrary().add(library);
                        robot.getLibraryTaskList().add(library);
                        library.setVariableType(variable);
                    }
                    UEMLibraryConnection connection = new UEMLibraryConnection();
                    connection.setMasterPort(libPort.getName());
                    connection.setMasterTask(actionTask.getName());
                    connection.setSlaveLibrary(library.getName());
                    algorithm.getLibraryConnections().getTaskLibraryConnection().add(connection);
                }
            }
        }
    }

    private void visitModeForCommunication(ModeWrapper mode, UEMRobotTask robot,
            String currentGroup) {
        mode.getServiceList().forEach(s -> s.getService().getStatementList().forEach(statement -> {
            try {
                if (statement.getStatement().getStatementType().equals(StatementType.RECEIVE)) {
                    robot.getListenTask().addReceive(statement, robot);
                } else if (statement.getStatement().getStatementType()
                        .equals(StatementType.SUBSCRIBE)) {
                    robot.getListenTask().addSubscribe(statement, robot);
                    VariableTypeWrapper variable =
                        robot.getRobot().getVariableMap().get(statement.getVariableList().get(0).getName());
                    algorithm.addMulticastGroup(robot.getRobot().getTeam(), variable.getVariableType().getSize() * variable.getVariableType().getCount());
                } else if (statement.getStatement().getStatementType().equals(StatementType.SEND)) {
                    robot.getReportTask().addSend(statement, robot);
                } else if (statement.getStatement().getStatementType()
                        .equals(StatementType.PUBLISH)) {
                    robot.getReportTask().addPublish(statement, robot);
                } else if (statement.getStatement().getStatementType()
                        .equals(StatementType.THROW)) {
                    robot.getListenTask().addThrow(statement, currentGroup);
                    robot.getReportTask().addThrow(statement, currentGroup);
                    algorithm.addMulticastGroup(currentGroup, 4);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        mode.getGroupList()
                .forEach(g -> traverseTransitionForCommunication(
                        g.getModeTransition().getModeTransition(), robot,
                        GroupAllocator.makeGroupKey(currentGroup, g.getGroup().getName())));
    }

    private void traverseTransitionForCommunication(TransitionWrapper transition,
            UEMRobotTask robot, String currentGroup) {
        Set<ModeWrapper> modeSet = new HashSet<>();
        modeSet.add(transition.getDefaultMode().getMode());
        transition.getTransitionMap().values().forEach(list -> list.forEach(ce -> {
            ModeWrapper m = ce.getMode().getMode();
            if (!m.getMode().getName().equals("FINISH")
                    && !m.getMode().getName().equals("PREVIOUS_MODE")) {
                modeSet.add(m);
            }
        }));
        modeSet.forEach(m -> visitModeForCommunication(m, robot, currentGroup));
    }

    private void makeCommunicationTask(MissionWrapper mission, UEMRobotTask robot)
            throws Exception {
        algorithm.addTask(robot.getListenTask());
        algorithm.addTask(robot.getReportTask());
        String team = robot.getRobot().getGroupList().get(0);
        TransitionWrapper transition = mission.getTransition(team);
        traverseTransitionForCommunication(transition, robot, team);
        for (UEMLibrary library : robot.getLibraryTaskList()) {
            robot.getListenTask().addSharedData(library.getName());
            UEMLibraryConnection listenConnection = new UEMLibraryConnection();
            listenConnection.setSlaveLibrary(library.getName());
            listenConnection.setMasterPort(library.getName());
            listenConnection.setMasterTask(robot.getListenTask().getName());
            algorithm.getLibraryConnections().getTaskLibraryConnection().add(listenConnection);
            robot.getReportTask().addSharedData(library.getName());
            UEMLibraryConnection reportConnection = new UEMLibraryConnection();
            reportConnection.setSlaveLibrary(library.getName());
            reportConnection.setMasterPort(library.getName());
            reportConnection.setMasterTask(robot.getReportTask().getName());
            algorithm.getLibraryConnections().getTaskLibraryConnection().add(reportConnection);
            algorithm.addMulticastGroup(library.getGroup(), library.getVariableType().getVariableType().getSize() * library.getVariableType().getVariableType().getCount());
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
        boolean flag = true;
        while (flag) {
            flag = convertChannelToDirect(algorithm.getPortMaps().getPortMap().stream()
                    .map(pm -> (UEMPortMap) pm).collect(Collectors.toList()), channelList);
        }
        algorithm.getChannels().getChannel().addAll(channelList);
    }

    public boolean generateAlgorithmXML(Path rootDirectory, String projectName) {
        try {
            Path filePath = Paths.get(rootDirectory.toString(),
                    projectName + MetadataConstant.ALGORITHM_SUFFIX);
            CICAlgorithmXMLHandler handler = new CICAlgorithmXMLHandler();
            handler.setAlgorithm(algorithm);
            handler.storeXMLString(filePath.toString());
            return true;
        } catch (CICXMLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

package com.metadata.generator.algorithm;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.task.Task;
import com.dbmanager.datastructure.task.TaskFile;
import com.metadata.generator.constant.AlgorithmConstant;
import com.metadata.generator.util.LocalFileCopier;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.wrapper.ActionImplWrapper;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import com.strategy.strategydatastructure.wrapper.ControlStrategyWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.wrapper.StrategyWrapper;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import com.strategy.strategymaker.additionalinfo.AdditionalInfo;
import hopes.cic.xml.ChannelPortType;
import hopes.cic.xml.handler.CICAlgorithmXMLHandler;

public class AlgorithmGenerator {
    private static CICAlgorithmXMLHandler handler = new CICAlgorithmXMLHandler();
    private static UEMAlgorithm algorithm = new UEMAlgorithm();

    public static void generateAlgorithmXML(MissionWrapper mission, StrategyWrapper strategy,
            AdditionalInfo additionalInfo, Path targetDir) {
        try {
            for (RobotImplWrapper robot : strategy.getRobotList()) {
                UEMRobotTask robotTask = new UEMRobotTask(algorithm.getTaskNum(),
                        robot.getRobot().getRobotId(), robot);
                algorithm.addTask(robotTask);
                makeRobotInerGraph(mission, robotTask, additionalInfo, targetDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void makeRobotInerGraph(MissionWrapper mission, UEMRobotTask robot,
            AdditionalInfo additionalInfo, Path targetDir) throws Exception {
        makeActionTask(robot, robot.getRobot().getControlStrategyList(),
                Paths.get(additionalInfo.getTaskServerPrefix()), targetDir);
        makeLibraryTask(robot);
        makeCommunicationTask(robot);
        makeControlTask();

    }

    private static void copyFiles(Task task, Path taskServerPrefix, Path targetDir)
            throws Exception {
        for (TaskFile file : task.getTaskFiles()) {
            if (file.isDirectory()) {
                LocalFileCopier.copyDirectory(
                        Paths.get(taskServerPrefix.toString(), file.getPath()), targetDir);
            } else {
                LocalFileCopier.copyBinaryFile(
                        Paths.get(taskServerPrefix.toString(), file.getPath()),
                        Paths.get(targetDir.toString(), file.getPath()));
            }
        }
    }

    private static List<UEMTaskGraph> recursiveExplore(UEMActionTask actionTask,
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

    private static boolean convertChannelPortToDirect(ChannelPortType port,
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

    private static boolean convertChannelToDirect(List<UEMPortMap> portMapList,
            List<UEMChannel> channelList) {
        boolean flag = false;
        for (UEMChannel channel : channelList) {
            flag ^= convertChannelPortToDirect(channel.getSrc().get(0), portMapList);
            flag ^= convertChannelPortToDirect(channel.getDst().get(0), portMapList);
        }
        return flag;
    }

    private static boolean convertLibraryPortToDirect(UEMLibraryConnection connection,
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

    private static boolean convertLibConnectionToDirect(List<UEMPortMap> portMapList,
            List<UEMLibraryConnection> libConnectionList) {
        boolean flag = false;
        for (UEMLibraryConnection libConnection : libConnectionList) {
            convertLibraryPortToDirect(libConnection, portMapList);
        }
        return flag;

    }

    private static void recursiveCommConvert(List<UEMPortMap> portMapList,
            List<UEMChannel> channelList, List<UEMLibraryConnection> libConnectionList) {
        boolean flag = false;
        do {
            flag = false;
            flag ^= convertChannelToDirect(portMapList, channelList);
            flag ^= convertLibConnectionToDirect(portMapList, libConnectionList);
        } while (flag);
    }

    private static List<UEMTaskGraph> exploreSubGraph(UEMActionTask actionTask,
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

    private static void makeActionTask(UEMRobotTask robot,
            List<ControlStrategyWrapper> controlStrategyList, Path taskServerPrefix, Path targetDir)
            throws Exception {
        for (ControlStrategyWrapper controlStrategy : controlStrategyList) {
            for (ActionImplWrapper action : controlStrategy.getActionList()) {
                Task task = action.getTask();
                UEMActionTask actionTask =
                        new UEMActionTask(algorithm.getTaskNum(), robot.getName(), action);
                copyFiles(task, taskServerPrefix, targetDir);
                if (task.isHasSubGraph()) {
                    List<UEMTaskGraph> taskGraphList =
                            exploreSubGraph(actionTask, taskServerPrefix);
                    for (UEMTaskGraph taskGraph : taskGraphList) {
                        algorithm.getTasks().getTask().addAll(taskGraph.getTaskList());
                        algorithm.getChannels().getChannel().addAll(taskGraph.getChannelList());
                        algorithm.getLibraries().getLibrary().addAll(taskGraph.getLibraryList());
                        algorithm.getLibraryConnections().getTaskLibraryConnection()
                                .addAll(taskGraph.getLibraryConnectionList());
                    }
                }
                robot.getActionTaskList().add(actionTask);
                algorithm.addTask(actionTask);
            }
        }
    }

    private static void makeLibraryTask(UEMRobotTask robot) {
        for (UEMActionTask actionTask : robot.getActionTaskList()) {
            if (actionTask.getAction().getAction().isGroupAction()) {
                ActionTypeWrapper actionType = actionTask.getAction().getAction();
                for (int i = 0; i < actionType.getVariableSharedList().size(); i++) {
                    VariableTypeWrapper variable = actionType.getVariableSharedList().get(i);
                    UEMLibraryPort libPort = actionTask.getLibraryPort(i);
                    String libName =
                            UEMLibrary.makeName(robot.getName(), actionType.getAction().getName()
                                    + variable.getVariableType().getName());
                    UEMLibrary library = robot.getLibraryTask(libName);
                    if (library == null) {
                        library = new UEMLibrary();
                        library.makeGeneratedLibrary(libName);
                        algorithm.getLibraries().getLibrary().add(library);
                        robot.getLibraryTaskList().add(library);
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

    private static void makeListenTask(UEMRobotTask robot) {

    }

    private static void makeCommunicationTask(UEMRobotTask robot) {

    }

    private static void makeControlTask() {}
}

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
import com.strategy.strategydatastructure.wrapper.ControlStrategyWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.wrapper.StrategyWrapper;
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
                UEMRobotTask robotTask =
                        new UEMRobotTask(algorithm.getTaskNum(), robot.getRobot().getRobotId());
                algorithm.addTask(robotTask);
                makeRobotInerGraph(mission, robot, additionalInfo, targetDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void makeRobotInerGraph(MissionWrapper mission, RobotImplWrapper robot,
            AdditionalInfo additionalInfo, Path targetDir) throws Exception {
        List<UEMActionTask> actionTaskList =
                makeActionTask(robot.getRobot().getRobotId(), robot.getControlStrategyList(),
                        Paths.get(additionalInfo.getTaskServerPrefix()), targetDir);
        makeLibraryTask(mission, actionTaskList);
        makeCommunicationTask(mission, actionTaskList);
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

    private static List<UEMActionTask> makeActionTask(String robotName,
            List<ControlStrategyWrapper> controlStrategyList, Path taskServerPrefix, Path targetDir)
            throws Exception {
        List<UEMActionTask> actionTaskList = new ArrayList<>();

        for (ControlStrategyWrapper controlStrategy : controlStrategyList) {
            for (ActionImplWrapper action : controlStrategy.getActionList()) {
                Task task = action.getTask();
                UEMActionTask actionTask =
                        new UEMActionTask(algorithm.getTaskNum(), robotName, action);
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
                actionTaskList.add(actionTask);
                algorithm.addTask(actionTask);
            }
        }

        return actionTaskList;
    }

    private static void makeCommunicationTask(MissionWrapper mission,
            List<UEMActionTask> actionTasks) {

    }

    private static void makeLibraryTask(MissionWrapper mission, List<UEMActionTask> actionTasks) {

    }

    private static void makeControlTask() {}
}

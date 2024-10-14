package com.metadata.algorithm.task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.metadata.algorithm.UEMChannel;
import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.UEMModeTask;
import com.metadata.algorithm.library.UEMGroupAction;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.PortTypeType;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TimeMetricType;
import hopes.cic.xml.YesNoType;

public class UEMControlTask extends UEMTask {
    private Map<UEMTask, Map<List<UEMChannelPort>, List<UEMChannelPort>>> channelPortMap =
            new HashMap<>();
    private Map<UEMTask, UEMChannelPort> gorupPortMap = new HashMap<>();

    public UEMControlTask(UEMRobotTask robot) {
        super(robot.getName());
        setName(robot.getName(), AlgorithmConstant.CONTROL);
        setExtraCommonCode(robot.getName());
        setFile(AlgorithmConstant.CONTROL + AlgorithmConstant.TASK_FILE_EXTENSION);
        setHasSubGraph(AlgorithmConstant.NO);
        setLanguage(AlgorithmConstant.LANGUAGE_C);
        setIsHardwareDependent(YesNoType.NO);
        setParentTask(robot.getName());
        setRunCondition(RunConditionType.TIME_DRIVEN);
        setTaskType(AlgorithmConstant.CONTROL_TASK);
        setExtraFile(robot.getName());
        setMode();
    }

    private void setExtraFile(String robotId) {
        getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.ACTION_SOURCE_SUFFIX);
        getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.EVENT_SOURCE_SUFFIX);
        getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.MODE_SOURCE_SUFFIX);
        getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.SERVICE_SOURCE_SUFFIX);
        getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.TIMER_SOURCE_SUFFIX);
        getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.TRANSITION_SOURCE_SUFFIX);

        getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.GROUP_SOURCE_SUFFIX);

        getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.LEADER_SOURCE_SUFFIX);

        getExtraHeader().add(robotId + AlgorithmConstant.COMMON_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.VARIABLE_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.VARIABLE_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.PORT_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.PORT_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.EVENT_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.EVENT_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.RESOURCE_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.RESOURCE_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.ACTION_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.ACTION_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.MODE_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.MODE_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.SERVICE_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.SERVICE_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.TIMER_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.TIMER_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.TRANSITION_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.TRANSITION_HEADER_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.TEAM_HEADER_SUFFIX);

        getExtraHeader().add(robotId + AlgorithmConstant.GROUP_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.GROUP_SOURCE_SUFFIX);

        getExtraHeader().add(robotId + AlgorithmConstant.LEADER_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.LEADER_SOURCE_SUFFIX);

        getExtraHeader().add(robotId + AlgorithmConstant.LEADER_DATA_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.LEADER_DATA_SOURCE_SUFFIX);
    }

    private void setMode() {
        UEMModeTask mode = new UEMModeTask();
        mode.setName(getName());
        mode.setDeadline(100);
        mode.setDeadlineUnit(TimeMetricType.MS.value());
        mode.setPeriod(100);
        mode.setPeriodUnit(TimeMetricType.MS.value());
        setMode(mode);
    }

    public List<UEMChannel> setActionPortInfo(UEMActionTask actionTask) {
        List<UEMChannel> channelList = new ArrayList<>();
        List<UEMChannelPort> inputPortList = new ArrayList<>();
        List<UEMChannelPort> outputPortList = new ArrayList<>();
        channelPortMap.put(actionTask, new HashMap<>());
        channelPortMap.get(actionTask).put(inputPortList, outputPortList);
        for (UEMChannelPort counterPort : actionTask.getInputPortList()) {
            UEMChannelPort port = new UEMChannelPort();
            port.setPortInfo(actionTask.getName(), (UEMChannelPort) counterPort);
            getPort().add(port);
            inputPortList.add(port);
            channelList.add(
                    UEMChannel.makeChannel(this, port, actionTask, (UEMChannelPort) counterPort));
        }
        for (UEMChannelPort counterPort : actionTask.getOutputPortList()) {
            UEMChannelPort port = new UEMChannelPort();
            port.setPortInfo(actionTask.getName(), (UEMChannelPort) counterPort);
            port.setType(PortTypeType.FIFO);
            getPort().add(port);
            outputPortList.add(port);
            channelList.add(
                    UEMChannel.makeChannel(this, port, actionTask, (UEMChannelPort) counterPort));
        }
        if (actionTask.getGroupPort() != null) {
            UEMChannelPort port = new UEMChannelPort();
            port.setPortInfo(actionTask.getName(), actionTask.getGroupPort());
            getPort().add(port);
            channelList
                    .add(UEMChannel.makeChannel(this, port, actionTask, actionTask.getGroupPort()));
            gorupPortMap.put(actionTask, port);
        }
        return channelList;
    }

    public List<UEMChannel> setCommPortInfo(UEMCommTask commTask) {
        List<UEMChannel> channelList = new ArrayList<>();
        List<UEMChannelPort> portList = new ArrayList<>();
        channelPortMap.put(commTask, new HashMap<>());
        channelPortMap.get(commTask).put(portList, portList);
        for (UEMCommPort counterPort : commTask.getControlPortList()) {
            UEMCommPort port = new UEMCommPort();
            port.setPortInfo(commTask.getName(), counterPort);
            port.setMessage(counterPort.getMessage());
            port.setCounterTeam(counterPort.getCounterTeam());
            port.setCounterTeamVariable(counterPort.getCounterTeamVariable());
            port.setVariableType(counterPort.getVariableType());
            getPort().add(port);
            portList.add(port);
            channelList.add(
                    UEMChannel.makeChannel(this, port, commTask, (UEMChannelPort) counterPort));
        }
        return channelList;
    }

    public UEMChannel setLeaderPortInfo(UEMLeaderTask leaderTask, int groupNum) {
        List<UEMChannelPort> portList = new ArrayList<>();
        channelPortMap.put(leaderTask, new HashMap<>());
        channelPortMap.get(leaderTask).put(portList, portList);
        UEMChannelPort port = new UEMChannelPort();
        port.setPortInfo(leaderTask.getName(), leaderTask.getControlTaskPort());
        getPort().add(port);
        portList.add(port);
        UEMChannel channel =
                UEMChannel.makeChannel(this, port, leaderTask, leaderTask.getControlTaskPort());
        channel.setSize(port.getSampleSize().multiply(BigInteger.valueOf(groupNum)));
        return channel;
    }

    public void setGroupActionPortInfo(UEMGroupAction groupActionTask) {
        UEMLibraryPort groupActionPort = new UEMLibraryPort();
        groupActionPort.setName(AlgorithmConstant.GROUP_ACTION);
        groupActionPort.setType(AlgorithmConstant.GROUP_ACTION);
        groupActionPort.setLibrary(groupActionTask);
        getLibraryMasterPort().add(groupActionPort);
    }

    public List<UEMChannel> setGroupingPortInfo(UEMGroupingTask groupingTask) {
        List<UEMChannel> channelList = new ArrayList<>();
        channelPortMap.put(groupingTask, new HashMap<>());
        List<UEMChannelPort> inputPortList = new ArrayList<>();
        List<UEMChannelPort> outputPortList = new ArrayList<>();
        channelPortMap.get(groupingTask).put(inputPortList, outputPortList);

        UEMChannelPort modePort = groupingTask.getModePort();
        UEMChannelPort mPort = new UEMChannelPort();
        mPort.setPortInfo(groupingTask.getName(), modePort);
        getPort().add(mPort);
        inputPortList.add(mPort);
        channelList.add(UEMChannel.makeChannel(this, mPort, groupingTask, modePort));

        UEMChannelPort resultPort = groupingTask.getResultPort();
        UEMChannelPort rPort = new UEMChannelPort();
        rPort.setPortInfo(groupingTask.getName(), resultPort);
        getPort().add(rPort);
        outputPortList.add(rPort);
        channelList.add(UEMChannel.makeChannel(this, rPort, groupingTask, resultPort));

        return channelList;
    }

    public List<UEMChannelPort> getInputPortList(UEMTask task) {
        return (List<UEMChannelPort>) channelPortMap.get(task).keySet().toArray()[0];
    }

    public List<UEMChannelPort> getOutputPortList(UEMTask task) {
        return (List<UEMChannelPort>) channelPortMap.get(task).values().toArray()[0];
    }

    public UEMChannelPort getGroupPort(UEMTask task) {
        return gorupPortMap.get(task);
    }
}

package com.metadata.algorithm.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.metadata.algorithm.UEMChannel;
import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.UEMModeTask;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TimeMetricType;
import hopes.cic.xml.YesNoType;

public class UEMControlTask extends UEMTask {
    private Map<UEMTask, Map<List<UEMChannelPort>, List<UEMChannelPort>>> channelPortMap =
            new HashMap<>();
    private Map<UEMTask, UEMChannelPort> gorupPortMap = new HashMap<>();

    public UEMControlTask(UEMRobotTask robot) {
        super();
        setName(robot.getName(), AlgorithmConstant.CONTROL);
        setCflags("");
        setLdflags("");
        setExtraCommonCode(robot.getName());
        setFile(getName() + AlgorithmConstant.TASK_FILE_EXTENSION);
        setLanguage(AlgorithmConstant.LANGUAGE_C);
        setIsHardwareDependent(YesNoType.NO);
        setParentTask(robot.getName());
        setRunCondition(RunConditionType.TIME_DRIVEN);
        setTaskType(AlgorithmConstant.CONTROL_TASK);
        setExtraFile(robot.getName());
        setMode();
    }

    private void setExtraFile(String robotId) {
        getExtraSource().add(AlgorithmConstant.COMMON_TIMER_SOURCE);
        getExtraSource().add(AlgorithmConstant.COMMON_SERVICE_SOURCE);
        getExtraHeader().add(AlgorithmConstant.COMMON_HEADER);
        getExtraSource().add(robotId + AlgorithmConstant.ROBOT_PORT_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.ROBOT_PORT_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.ROBOT_EVENT_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.ROBOT_EVENT_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.ROBOT_SERVICE_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.ROBOT_SERVICE_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.ROBOT_TIMER_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.ROBOT_TIMER_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.ROBOT_MODE_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.ROBOT_COMMON_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.ROBOT_VARIABLE_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.ROBOT_VARIABLE_HEADER_SUFFIX);
    }

    private void setMode() {
        UEMModeTask mode = new UEMModeTask();
        mode.setName(getName());
        mode.setDeadline(50);
        mode.setDeadlineUnit(TimeMetricType.US.value());
        mode.setPeriod(50);
        mode.setPeriodUnit(TimeMetricType.US.value());
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
            getPort().add(port);
            outputPortList.add(port);
            channelList.add(
                    UEMChannel.makeChannel(this, port, actionTask, (UEMChannelPort) counterPort));
        }
        if (actionTask.getActionImpl().getActionType().isGroupAction()) {
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
            port.setVariableType(counterPort.getVariableType());
            getPort().add(port);
            portList.add(port);
            channelList.add(
                    UEMChannel.makeChannel(this, port, commTask, (UEMChannelPort) counterPort));
        }
        return channelList;
    }

    public UEMChannel setLeaderPortInfo(UEMLeaderTask leaderTask) {
        List<UEMChannelPort> portList = new ArrayList<>();
        channelPortMap.put(leaderTask, new HashMap<>());
        channelPortMap.get(leaderTask).put(portList, portList);
        UEMChannelPort port = new UEMChannelPort();
        port.setPortInfo(leaderTask.getName(), leaderTask.getControlTaskPort());
        getPort().add(port);
        portList.add(port);
        return UEMChannel.makeChannel(this, port, leaderTask, leaderTask.getControlTaskPort());
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

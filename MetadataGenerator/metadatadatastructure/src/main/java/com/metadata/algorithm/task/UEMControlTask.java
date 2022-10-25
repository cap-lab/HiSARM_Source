package com.metadata.algorithm.task;

import java.util.ArrayList;
import java.util.List;
import com.metadata.algorithm.UEMChannel;
import com.metadata.algorithm.UEMChannelPort;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TaskPortType;
import hopes.cic.xml.YesNoType;

public class UEMControlTask extends UEMTask {
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
    }

    private void setExtraFile(String robotId) {
        getExtraSource().add(AlgorithmConstant.COMMON_TIMER_SOURCE);
        getExtraSource().add(AlgorithmConstant.COMMON_PORT_SOURCE);
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


    public List<UEMChannel> setActionPortInfo(UEMActionTask actionTask) {
        List<UEMChannel> channelList = new ArrayList<>();
        for (TaskPortType counterPort : actionTask.getPort()) {
            UEMChannelPort port = new UEMChannelPort();
            port.setPortInfo(actionTask.getName(), (UEMChannelPort) counterPort);
            getPort().add(port);
            channelList.add(
                    UEMChannel.makeChannel(this, port, actionTask, (UEMChannelPort) counterPort));
        }
        return channelList;
    }

    public List<UEMChannel> setCommPortInfo(UEMCommTask commTask) {
        List<UEMChannel> channelList = new ArrayList<>();
        for (UEMChannelPort counterPort : commTask.getControlPortList()) {
            UEMChannelPort port = new UEMChannelPort();
            port.setPortInfo(commTask.getName(), counterPort);
            getPort().add(port);
            channelList.add(
                    UEMChannel.makeChannel(this, port, commTask, (UEMChannelPort) counterPort));
        }
        return channelList;
    }
}

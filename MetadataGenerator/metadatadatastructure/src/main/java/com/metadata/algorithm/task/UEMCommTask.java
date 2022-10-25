package com.metadata.algorithm.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.UEMLibraryPort;
import com.metadata.algorithm.UEMMulticastPort;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.YesNoType;

public class UEMCommTask extends UEMTask {
    private List<UEMChannelPort> controlPortList = new ArrayList<>();
    private List<UEMCommPort> exportPortList = new ArrayList<>();
    private Map<UEMCommPort, UEMChannelPort> channelPortMap = new HashMap<>();
    private Map<UEMMulticastPort, UEMChannelPort> multicastPortMap = new HashMap<>();
    private Map<UEMLibraryPort, UEMMulticastPort> sharedDataPortMap = new HashMap<>();

    public UEMCommTask(String robotId, String name) {
        super();
        setName(robotId, name);
        init(robotId);
    }

    protected String makePortName(String counterTeamName, String message) {
        return counterTeamName + "_" + message;
    }

    private void init(String robotId) {
        setLanguage(AlgorithmConstant.LANGUAGE_C);
        setIsHardwareDependent(YesNoType.NO);
        setParentTask(robotId);
        setTaskType(AlgorithmConstant.COMPUTATION_TASK);
        setRunCondition(RunConditionType.TIME_DRIVEN);
        setFile(getName() + AlgorithmConstant.TASK_FILE_EXTENSION);
        setCommRelatedFile(robotId);
    }

    private void setCommRelatedFile(String robotId) {
        getExtraSource().add(AlgorithmConstant.COMMUNICATION_SOURCE);
        getExtraSource().add(AlgorithmConstant.COMMON_PORT_SOURCE);
        getExtraSource().add(robotId + AlgorithmConstant.ROBOT_PORT_SOURCE_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.ROBOT_VARIABLE_SOURCE_SUFFIX);
        getExtraHeader().add(AlgorithmConstant.COMMUNICATION_HEADER);
        getExtraHeader().add(AlgorithmConstant.COMMON_PORT_HEADER);
        getExtraHeader().add(robotId + AlgorithmConstant.ROBOT_PORT_HEADER_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.ROBOT_VARIABLE_HEADER_SUFFIX);
        setExtraCommonCode(robotId);
    }

    public List<UEMChannelPort> getControlPortList() {
        return controlPortList;
    }

    public List<UEMCommPort> getExportPortList() {
        return exportPortList;
    }

    public Map<UEMCommPort, UEMChannelPort> getChannelPortMap() {
        return channelPortMap;
    }

    public Map<UEMMulticastPort, UEMChannelPort> getMulticastPortMap() {
        return multicastPortMap;
    }

    public Map<UEMLibraryPort, UEMMulticastPort> getSharedDataPortMap() {
        return sharedDataPortMap;
    }

}

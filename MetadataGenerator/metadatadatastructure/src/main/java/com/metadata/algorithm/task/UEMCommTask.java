package com.metadata.algorithm.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.UEMModeTask;
import com.metadata.algorithm.UEMMulticastPort;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TimeMetricType;
import hopes.cic.xml.YesNoType;

public class UEMCommTask extends UEMTask {
    private List<UEMCommPort> controlPortList = new ArrayList<>();
    private List<UEMCommPort> exportPortList = new ArrayList<>();
    private Map<UEMCommPort, UEMCommPort> channelPortMap = new HashMap<>();
    private Map<UEMCommPort, UEMMulticastPort> multicastPortMap = new HashMap<>();
    private Map<UEMLibraryPort, UEMMulticastPort> sharedDataPortMap = new HashMap<>();
    protected UEMLibraryPort leaderPort;
    private Map<UEMMulticastPort, UEMMulticastPort> leaderPortMap = new HashMap<>();
    protected UEMLibraryPort groupActionPort;
    private List<UEMMulticastPort> groupActionPortList = new ArrayList<>();

    public UEMCommTask(String robotId, String name) {
        super(robotId);
        setName(robotId, name);
        init(robotId);
    }

    protected void setMode(int time) {
        UEMModeTask mode = new UEMModeTask();
        mode.setName(getName());
        mode.setDeadline(time);
        mode.setDeadlineUnit(TimeMetricType.MS.value());
        mode.setPeriod(time);
        mode.setPeriodUnit(TimeMetricType.MS.value());
        setMode(mode);
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
        setHasSubGraph(AlgorithmConstant.NO);
    }

    private void setCommRelatedFile(String robotId) {
        getExtraSource().add(AlgorithmConstant.COMMON_COMMUNICATION_SOURCE);
        getExtraSource().add(robotId + AlgorithmConstant.ROBOT_VARIABLE_SOURCE_SUFFIX);
        getExtraHeader().add(AlgorithmConstant.COMMON_COMMUNICATION_HEADER);
        getExtraHeader().add(AlgorithmConstant.COMMON_PORT_HEADER);
        getExtraHeader().add(robotId + AlgorithmConstant.ROBOT_VARIABLE_HEADER_SUFFIX);
        setExtraCommonCode(robotId);
    }

    public List<UEMCommPort> getControlPortList() {
        return controlPortList;
    }

    public UEMCommPort getControlPort(String name) {
        for (UEMCommPort port : controlPortList) {
            if (port.getName().equals(name)) {
                return port;
            }
        }
        return null;
    }

    public List<UEMCommPort> getExportPortList() {
        return exportPortList;
    }

    public Map<UEMCommPort, UEMCommPort> getChannelPortMap() {
        return channelPortMap;
    }

    public Map<UEMCommPort, UEMMulticastPort> getMulticastPortMap() {
        return multicastPortMap;
    }

    public Map<UEMLibraryPort, UEMMulticastPort> getSharedDataPortMap() {
        return sharedDataPortMap;
    }

    public Map<UEMMulticastPort, UEMMulticastPort> getLeaderPortMap() {
        return leaderPortMap;
    }

    public UEMLibraryPort getLeaderPort() {
        return leaderPort;
    }

    public List<UEMMulticastPort> getGroupActionPortList() {
        return groupActionPortList;
    }

}

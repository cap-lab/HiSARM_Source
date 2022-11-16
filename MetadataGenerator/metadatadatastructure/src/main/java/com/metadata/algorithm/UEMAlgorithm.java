package com.metadata.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.metadata.algorithm.task.UEMRobotTask;
import com.metadata.algorithm.task.UEMTask;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.CICAlgorithmType;
import hopes.cic.xml.ChannelListType;
import hopes.cic.xml.LibraryConnectionListType;
import hopes.cic.xml.LibraryListType;
import hopes.cic.xml.ModeListType;
import hopes.cic.xml.ModeType;
import hopes.cic.xml.MulticastGroupListType;
import hopes.cic.xml.MulticastGroupType;
import hopes.cic.xml.PortMapListType;
import hopes.cic.xml.TaskListType;

public class UEMAlgorithm {
    private CICAlgorithmType algorithm;
    private List<UEMRobotTask> robotTaskList = new ArrayList<>();
    private Map<UEMRobotTask, List<UEMRobotTask>> robotConnectionMap = new HashMap<>();

    public UEMAlgorithm(CICAlgorithmType algorithm) {
        this.algorithm = algorithm;
        algorithm.setProperty(AlgorithmConstant.PROCESS_NETWORK);
        algorithm.setLibraries(new LibraryListType());
        algorithm.setLibraryConnections(new LibraryConnectionListType());
        algorithm.setChannels(new ChannelListType());
        algorithm.setMulticastGroups(new MulticastGroupListType());
        algorithm.setPortMaps(new PortMapListType());
        algorithm.setTasks(new TaskListType());
        algorithm.setModes(new ModeListType());
        algorithm.getModes().getMode().add(new ModeType());
        algorithm.getModes().getMode().get(0).setName(AlgorithmConstant.DEFAULT);
    }

    public void addTask(UEMTask task) {
        task.setId(getTaskNum());
        algorithm.getModes().getMode().get(0).getTask().add(task.getUEMMode());
        algorithm.getTasks().getTask().add(task);
    }

    public void addAllTasks(List<UEMTask> taskList) {
        taskList.forEach(t -> addTask(t));
    }

    public UEMMulticastGroup getMulticastGroup(String groupId) {
        for (MulticastGroupType group : algorithm.getMulticastGroups().getMulticastGroup()) {
            if (group.getGroupName().equals(groupId)) {
                return (UEMMulticastGroup) group;
            }
        }
        return null;
    }

    public void addMulticastGroup(String groupId, int size) {
        if (getMulticastGroup(groupId) == null) {
            algorithm.getMulticastGroups().getMulticastGroup()
                    .add(new UEMMulticastGroup(groupId, size));
        }
    }

    public int getTaskNum() {
        return algorithm.getTasks().getTask().size();
    }

    public List<UEMRobotTask> getRobotTaskList() {
        return robotTaskList;
    }

    public void putRobotConnection(UEMRobotTask src, UEMRobotTask dst) {
        List<UEMRobotTask> dstList;
        if (robotConnectionMap.containsKey(src)) {
            dstList = robotConnectionMap.get(src);
        } else {
            dstList = new ArrayList<>();
            robotConnectionMap.put(src, dstList);
        }
        dstList.add(dst);
    }

    public Map<UEMRobotTask, List<UEMRobotTask>> getRobotConnectionMap() {
        return robotConnectionMap;
    }

    public CICAlgorithmType getAlgorithm() {
        return algorithm;
    }
}

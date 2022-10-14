package com.metadata.generator.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.metadata.generator.algorithm.task.UEMRobotTask;
import com.metadata.generator.algorithm.task.UEMTask;
import com.metadata.generator.constant.AlgorithmConstant;
import hopes.cic.xml.CICAlgorithmType;
import hopes.cic.xml.ChannelListType;
import hopes.cic.xml.LibraryListType;
import hopes.cic.xml.MulticastGroupListType;
import hopes.cic.xml.PortMapListType;

public class UEMAlgorithm extends CICAlgorithmType {
    private List<UEMRobotTask> robotTaskList = new ArrayList<>();
    private Map<UEMRobotTask, List<UEMRobotTask>> robotConnectionMap = new HashMap<>();

    public UEMAlgorithm() {
        super();
        setProperty(AlgorithmConstant.PROCESS_NETWORK);
        setLibraries(new LibraryListType());
        setChannels(new ChannelListType());
        setMulticastGroups(new MulticastGroupListType());
        setPortMaps(new PortMapListType());
    }

    public void addTask(UEMTask task) {
        getTasks().getTask().add(task);
    }

    public int getTaskNum() {
        return getTasks().getTask().size();
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
}

package com.metadata.generator.algorithm;

import java.util.ArrayList;
import java.util.List;
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
}

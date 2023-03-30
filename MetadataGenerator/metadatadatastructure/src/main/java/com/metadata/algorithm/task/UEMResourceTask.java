package com.metadata.algorithm.task;

import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.task.PortDirection;
import com.dbmanager.datastructure.task.ResourcePort;
import com.dbmanager.datastructure.task.Task;
import com.metadata.algorithm.UEMModeTask;
import com.metadata.algorithm.UEMMulticastPort;
import com.metadata.algorithm.UEMTaskGraph;
import com.metadata.constant.AlgorithmConstant;
import com.strategy.strategydatastructure.wrapper.ResourceWrapper;
import hopes.cic.xml.PortDirectionType;
import hopes.cic.xml.YesNoType;

public class UEMResourceTask extends UEMTask {
    private List<UEMTaskGraph> subTaskGraphs = new ArrayList<>();
    private ResourceWrapper resource;

    public UEMResourceTask(String robotName, ResourceWrapper resource, Task task) {
        super(robotName);
        this.resource = resource;
        setName(robotName + "_" + resource.getResource().getResourceId());
        setParentTask(robotName);
        setTaskInfo(task, robotName);
    }

    private void setTaskInfo(Task task, String robotName) {
        try {
            setRunCondition(runCondition(task.getRunCondition()));
            setFile(task.getCICFile());
            setCflags(task.getCompileFlags());
            setLdflags(task.getLinkFlags());
            setHasSubGraph(convertYesNoString(task.isHasSubGraph()));
            setSubGraphProperty(AlgorithmConstant.DATAFLOW);
            setIsHardwareDependent(YesNoType.NO);
            setTaskType(AlgorithmConstant.COMPUTATION_TASK);
            setLanguage(task.getLanguage());
            setResourcePorts(task, robotName);
            setMode(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResourcePorts(Task task, String robotName) {
        if (task.isHasSubGraph() == false) {
            for (ResourcePort port : task.getResourcePortSet()) {
                UEMMulticastPort resourcePort = new UEMMulticastPort();
                resourcePort.setDirection(
                        port.getDirection() == PortDirection.IN ? PortDirectionType.INPUT
                                : PortDirectionType.OUTPUT);
                resourcePort.setName(port.getName());
                resourcePort.setGroup(robotName + "_" + port.getName());
                getMulticastPort().add(resourcePort);
            }
        }
    }

    private void setMode(Task task) {
        UEMModeTask uemMode = new UEMModeTask();
        uemMode.setName(getName());
        uemMode.setPriority(1);
        uemMode.setDeadline(task.getDeadline().getTime());
        uemMode.setDeadlineUnit(task.getDeadline().getUnit());
        uemMode.setPeriod(task.getPeriod().getTime());
        uemMode.setPeriodUnit(task.getPeriod().getUnit());
        setMode(uemMode);
    }

    public List<UEMTaskGraph> getSubTaskGraphs() {
        return subTaskGraphs;
    }

    public void setSubTaskGraphs(List<UEMTaskGraph> subTaskGraphs) {
        this.subTaskGraphs = subTaskGraphs;
    }

    public ResourceWrapper getResource() {
        return resource;
    }
}

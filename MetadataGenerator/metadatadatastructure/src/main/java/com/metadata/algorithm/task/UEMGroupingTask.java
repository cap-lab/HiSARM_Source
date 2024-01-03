package com.metadata.algorithm.task;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.task.ExtraSetting;
import com.dbmanager.datastructure.task.PortDirection;
import com.dbmanager.datastructure.task.ResourcePort;
import com.dbmanager.datastructure.task.Task;
import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.UEMModeTask;
import com.metadata.algorithm.UEMMulticastPort;
import com.metadata.algorithm.UEMTaskGraph;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.constant.AlgorithmConstant;
import com.strategy.strategydatastructure.enumeration.AdditionalTaskType;
import hopes.cic.xml.PortDirectionType;
import hopes.cic.xml.YesNoType;

public class UEMGroupingTask extends UEMTask {
    private Task groupingTask;
    private List<UEMTaskGraph> subTaskGraphs = new ArrayList<>();
    private UEMChannelPort modePort;
    private UEMChannelPort resultPort;
    private UEMLibraryPort groupPort;

    public UEMGroupingTask(UEMRobotTask robotTask, Path taskServer) throws Exception {
        super(robotTask.getName(), taskServer);
        groupingTask = robotTask.getRobot().getAdditionalTask(AdditionalTaskType.GROUP_SELECTION)
                .getTask();
        setParentTask(robotTask.getName());
        setName(robotTask.getName(), groupingTask.getTaskId());
        getExtraHeader().add(AlgorithmConstant.SEMO + AlgorithmConstant.GROUP_HEADER_SUFFIX);
        getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.GROUP_SOURCE_SUFFIX);
        setTaskInfo(groupingTask, robotTask.getName());
        addDefaultPort();
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
            setExtraSetting(task);
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

    private void setExtraSetting(Task task) {
        for (ExtraSetting setting : task.getExtraSettings()) {
            switch (setting.getType()) {
                case EXTRA_HEADER:
                    getExtraHeader().add(setting.getName());
                    break;
                case EXTRA_SOURCE:
                    getExtraSource().add(setting.getName());
                    break;
                case EXTRA_CIC:
                    getExtraCIC().add(setting.getName());
                    break;
                case EXTRA_FILE:
                    getExtraFile().add(setting.getName());
                    break;
            }
        }
    }

    private void addDefaultPort() {
        modePort = new UEMChannelPort();
        modePort.makePortInfo("mode", PortDirectionType.INPUT, 4);
        modePort.setDefaultRate();
        getPort().add(modePort);
        resultPort = new UEMChannelPort();
        resultPort.makePortInfo("result", PortDirectionType.OUTPUT, 4);
        resultPort.setDefaultRate();
        getPort().add(resultPort);
        groupPort = new UEMLibraryPort();
        groupPort.setIndex(0);
        groupPort.setName("group");
        groupPort.setType(groupPort.getName());
    }

    public UEMChannelPort getModePort() {
        return modePort;
    }

    public UEMChannelPort getResultPort() {
        return resultPort;
    }

    public UEMLibraryPort getGroupPort() {
        return groupPort;
    }

    public List<UEMTaskGraph> getSubTaskGraphs() {
        return subTaskGraphs;
    }

    public Task getGroupingTask() {
        return groupingTask;
    }
}

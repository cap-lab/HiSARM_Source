package com.metadata.generator.algorithm;

import com.dbmanager.datastructure.task.ChannelPort;
import com.dbmanager.datastructure.task.LibraryPort;
import com.dbmanager.datastructure.task.Task;
import com.metadata.generator.constant.AlgorithmConstant;
import com.strategy.strategydatastructure.wrapper.ActionImplWrapper;
import hopes.cic.xml.LibraryMasterPortType;

public class UEMActionTask extends UEMTask {
    private ActionImplWrapper action;

    public UEMActionTask(int taskIndex, String robotTask, ActionImplWrapper action) {
        super(taskIndex);
        setName(robotTask, action.getActionImpl().getActionImplId());
        this.action = action;
        setParentTask(robotTask);
        setTaskInfo(action.getTask());
    }

    private void setTaskInfo(Task task) {
        try {
            setRunCondition(runCondition(task.getRunCondition()));
            setFile(task.getCICFile());
            setCflags(task.getCompileFlags());
            setLdflags(task.getLinkFlags());
            setHasSubGraph(convertYesNoString(task.isHasSubGraph()));
            setTaskType(AlgorithmConstant.COMPUTATION_TASK);
            setLanguage(task.getLanguage());
            setChannelPorts(task);
            setLibraryPorts(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setChannelPorts(Task task) {
        for (ChannelPort port : task.getChannelPortSet()) {
            UEMChannelPort actionPort = new UEMChannelPort();
            actionPort.setPortInfo(port);
            getPort().add(actionPort);
        }
    }

    private void setLibraryPorts(Task task) {
        for (LibraryPort port : task.getLibraryPortSet()) {
            UEMLibraryPort libraryPort = new UEMLibraryPort();
            libraryPort.setName(port.getName());
            libraryPort.setType(port.getName());
            libraryPort.setIndex(port.getIndex());
            getLibraryMasterPort().add(libraryPort);
        }
    }

    public ActionImplWrapper getAction() {
        return action;
    }

    public void setAction(ActionImplWrapper action) {
        this.action = action;
    }

    public UEMLibraryPort getLibraryPort(int index) {
        for (LibraryMasterPortType libPort : getLibraryMasterPort()) {
            if (((UEMLibraryPort) libPort).getIndex() == index) {
                return (UEMLibraryPort) libPort;
            }
        }
        return null;
    }

}

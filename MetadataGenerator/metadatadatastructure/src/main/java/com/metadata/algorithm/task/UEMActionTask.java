package com.metadata.algorithm.task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.task.ChannelPort;
import com.dbmanager.datastructure.task.LibraryPort;
import com.dbmanager.datastructure.task.Task;
import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.UEMTaskGraph;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.constant.AlgorithmConstant;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.strategy.strategydatastructure.wrapper.ActionImplWrapper;
import hopes.cic.xml.LibraryMasterPortType;
import hopes.cic.xml.PortDirectionType;
import hopes.cic.xml.PortTypeType;
import hopes.cic.xml.TaskRateType;
import hopes.cic.xml.YesNoType;

public class UEMActionTask extends UEMTask {
    private ActionImplWrapper actionImpl;
    private ActionStatement actionStatement;
    private String scope;
    private List<UEMTaskGraph> subTaskGraphs = new ArrayList<>();
    private UEMLibraryPort leaderPort;
    private List<UEMLibraryPort> sharedDataPortList = new ArrayList<>();
    private UEMChannelPort groupPort;

    public UEMActionTask(String robotTask, String groupId, String serviceId,
            ActionImplWrapper actionImpl, ActionStatement actionStatement) {
        super();
        this.scope = groupId + "_" + serviceId;
        setName(robotTask, scope + "_" + actionImpl.getActionImpl().getActionImplId());
        this.actionImpl = actionImpl;
        this.actionStatement = actionStatement;
        setParentTask(robotTask);
        setTaskInfo(actionImpl.getTask());
    }

    private void setTaskInfo(Task task) {
        try {
            setRunCondition(runCondition(task.getRunCondition()));
            setFile(task.getCICFile());
            setCflags(task.getCompileFlags());
            setLdflags(task.getLinkFlags());
            setHasSubGraph(convertYesNoString(task.isHasSubGraph()));
            setIsHardwareDependent(YesNoType.NO);
            setTaskType(AlgorithmConstant.COMPUTATION_TASK);
            setLanguage(task.getLanguage());
            setChannelPorts(task);
            setLibraryPorts(task);
            if (task.getLeaderPort() != null) {
                setLeaderPort();
            }
            if (task.getGroupPort() != null) {
                setGroupPort();
            }
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

    public ActionImplWrapper getActionImpl() {
        return actionImpl;
    }

    public void setActionImpl(ActionImplWrapper actionImpl) {
        this.actionImpl = actionImpl;
    }

    public UEMLibraryPort getLibraryPort(int index) {
        for (LibraryMasterPortType libPort : getLibraryMasterPort()) {
            if (((UEMLibraryPort) libPort).getIndex() == index) {
                return (UEMLibraryPort) libPort;
            }
        }
        return null;
    }

    public List<UEMTaskGraph> getSubTaskGraphs() {
        return subTaskGraphs;
    }

    public void setSubTaskGraphs(List<UEMTaskGraph> subTaskGraphs) {
        this.subTaskGraphs = subTaskGraphs;
    }

    public String getScope() {
        return scope;
    }

    public ActionStatement getActionStatement() {
        return actionStatement;
    }

    public void setActionStatement(ActionStatement actionStatement) {
        this.actionStatement = actionStatement;
    }

    public UEMLibraryPort getLeaderPort() {
        return leaderPort;
    }

    private void setLeaderPort() {
        UEMLibraryPort leaderPort = new UEMLibraryPort();
        leaderPort.setName("leader");
        leaderPort.setType("leader");
        this.leaderPort = leaderPort;
        getLibraryMasterPort().add(leaderPort);
    }

    private void setGroupPort() {
        UEMChannelPort groupPort = new UEMChannelPort();
        groupPort.setName("group");
        groupPort.setDirection(PortDirectionType.INPUT);
        groupPort.setExport(false);
        groupPort.setSampleSize(BigInteger.valueOf(4));
        groupPort.setSampleType("int");
        groupPort.setType(PortTypeType.OVERWRITABLE);
        groupPort.setIsFeedback(false);
        TaskRateType rate = new TaskRateType();
        rate.setMode(AlgorithmConstant.DEFAULT);
        rate.setRate(BigInteger.ONE);
        groupPort.getRate().add(rate);
        this.groupPort = groupPort;
        getPort().add(groupPort);
    }

    public List<UEMLibraryPort> getSharedDataPortList() {
        return sharedDataPortList;
    }

}

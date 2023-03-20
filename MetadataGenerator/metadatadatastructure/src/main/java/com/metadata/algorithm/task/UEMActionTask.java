package com.metadata.algorithm.task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.task.ChannelPort;
import com.dbmanager.datastructure.task.LibraryPort;
import com.dbmanager.datastructure.task.PortDirection;
import com.dbmanager.datastructure.task.Task;
import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.UEMModeTask;
import com.metadata.algorithm.UEMTaskGraph;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.constant.AlgorithmConstant;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.strategy.strategydatastructure.wrapper.ActionImplWrapper;
import hopes.cic.xml.LibraryMasterPortType;
import hopes.cic.xml.PortDirectionType;
import hopes.cic.xml.PortTypeType;
import hopes.cic.xml.YesNoType;

public class UEMActionTask extends UEMTask {
    private ActionImplWrapper actionImpl;
    private ActionStatement actionStatement;
    private String scope;
    private List<UEMTaskGraph> subTaskGraphs = new ArrayList<>();
    private List<UEMChannelPort> inputPortList = new ArrayList<>();
    private List<UEMChannelPort> outputPortList = new ArrayList<>();
    private List<UEMLibraryPort> sharedDataPortList = new ArrayList<>();
    private UEMLibraryPort leaderPort = null;
    private UEMChannelPort groupPort = null;
    private int groupActionIndex = -1;

    public UEMActionTask(String robotTask, String groupId, String serviceId,
            ActionImplWrapper actionImpl, ActionStatement actionStatement) {
        super(robotTask);
        this.scope = makeScope(groupId, serviceId);
        setName(robotTask, scope + "_" + actionImpl.getActionImpl().getActionImplId());
        this.actionImpl = actionImpl;
        this.actionStatement = actionStatement;
        setParentTask(robotTask);
        setTaskInfo(actionImpl.getTask());
    }

    public String getActionName() {
        return scope + "_" + actionImpl.getActionImpl().getActionImplId();
    }

    private void setTaskInfo(Task task) {
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
            setChannelPorts(task);
            setLibraryPorts(task);
            setMode(task);
            if (actionImpl.getTask().getLeaderPort() != null) {
                setLeaderPort();
            }
            if (actionImpl.getTask().getGroupPort() != null) {
                setGroupPort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String makeScope(String groupId, String serviceId) {
        return groupId + "_" + serviceId;
    }

    private void setChannelPorts(Task task) {
        for (ChannelPort port : task.getChannelPortSet()) {
            UEMChannelPort actionPort = new UEMChannelPort();
            actionPort.setPortInfo(port);
            if (port.getDirection().equals(PortDirection.IN)) {
                inputPortList.add(actionPort);
            } else {
                outputPortList.add(actionPort);
            }
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

    private void setLeaderPort() {
        leaderPort = new UEMLibraryPort();
        leaderPort.setName(AlgorithmConstant.LEADER);
        leaderPort.setType(AlgorithmConstant.LEADER);
        getLibraryMasterPort().add(leaderPort);
    }

    private void setGroupPort() {
        groupPort = new UEMChannelPort();
        groupPort.setName(AlgorithmConstant.GROUP);
        groupPort.setDirection(PortDirectionType.INPUT);
        groupPort.setExport(false);
        groupPort.setSampleSize(BigInteger.valueOf(4));
        groupPort.setSampleType("int");
        groupPort.setType(PortTypeType.OVERWRITABLE);
        groupPort.setIsFeedback(false);
        groupPort.setDefaultRate();
        getPort().add(groupPort);
    }

    public List<UEMChannelPort> getInputPortList() {
        return inputPortList;
    }

    public List<UEMChannelPort> getOutputPortList() {
        return outputPortList;
    }

    public List<UEMLibraryPort> getSharedDataPortList() {
        return sharedDataPortList;
    }

    public UEMLibraryPort getLeaderPort() {
        return leaderPort;
    }

    public UEMChannelPort getGroupPort() {
        return groupPort;
    }

    public int getGroupActionIndex() {
        return groupActionIndex;
    }

    public void setGroupActionIndex(int groupActionIndex) {
        this.groupActionIndex = groupActionIndex;
    }

}

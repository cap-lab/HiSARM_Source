package com.metadata.algorithm.task;

import java.util.ArrayList;
import java.util.List;
import com.metadata.algorithm.UEMCommPort;
import com.metadata.algorithm.UEMModeTask;
import com.metadata.algorithm.UEMPortMap;
import com.metadata.algorithm.library.UEMLeaderLibrary;
import com.metadata.algorithm.library.UEMSharedData;
import com.metadata.constant.AlgorithmConstant;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TimeMetricType;
import hopes.cic.xml.YesNoType;

public class UEMRobotTask extends UEMTask {
    private RobotImplWrapper robot;
    private List<UEMActionTask> actionTaskList = new ArrayList<>();
    private List<UEMResourceTask> resourceTaskList = new ArrayList<>();
    private List<UEMSharedData> sharedDataTaskList = new ArrayList<>();
    private UEMControlTask controlTask;
    private UEMListenTask listenTask;
    private UEMReportTask reportTask;
    private UEMLeaderLibrary leaderLibraryTask;
    private UEMLeaderTask leaderTask;
    private UEMGroupActionTask groupActionTask;

    public UEMRobotTask(String name, RobotImplWrapper robot) {
        super(name);
        setName(name);
        setRunCondition(RunConditionType.TIME_DRIVEN);
        setTaskType(AlgorithmConstant.COMPUTATION_TASK);
        setHasSubGraph(AlgorithmConstant.YES);
        setParentTask(name);
        setIsHardwareDependent(YesNoType.NO);
        setSubGraphProperty(AlgorithmConstant.PROCESS_NETWORK);
        setMode();
        this.robot = robot;
        listenTask = new UEMListenTask(getName(), AlgorithmConstant.LISTEN);
        reportTask = new UEMReportTask(getName(), AlgorithmConstant.REPORT);
    }

    private void setMode() {
        UEMModeTask mode = new UEMModeTask();
        mode.setName(getName());
        mode.setDeadline(1);
        mode.setDeadlineUnit(TimeMetricType.US.value());
        mode.setPeriod(1);
        mode.setPeriodUnit(TimeMetricType.US.value());
        setMode(mode);
    }

    public int getRobotIndex() {
        return getRobot().getRobotIndex();
    }

    public RobotImplWrapper getRobot() {
        return robot;
    }

    public List<UEMActionTask> getActionTaskList() {
        return actionTaskList;
    }

    public List<UEMActionTask> getActionTaskList(String scope, ActionStatement statement) {
        List<UEMActionTask> targetActionTaskList = new ArrayList<>();
        for (UEMActionTask actionTask : actionTaskList) {
            if (actionTask.getScope().equals(scope) && actionTask.getActionStatement()
                    .getActionName().equals(statement.getActionName())) {
                targetActionTaskList.add(actionTask);
            }
        }
        return targetActionTaskList;
    }

    public void setActionTaskList(List<UEMActionTask> actionTaskList) {
        this.actionTaskList = actionTaskList;
    }

    public List<UEMSharedData> getSharedDataTaskList() {
        return sharedDataTaskList;
    }

    public UEMSharedData getSharedDataTask(String name) {
        for (UEMSharedData library : sharedDataTaskList) {
            if (library.getName().equals(name)) {
                return library;
            }
        }
        return null;
    }

    public UEMControlTask getControlTask() {
        return controlTask;
    }

    public void setControlTask(UEMControlTask controlTask) {
        this.controlTask = controlTask;
    }

    public UEMListenTask getListenTask() {
        return listenTask;
    }

    public void setListenTask(UEMListenTask listenTask) {
        this.listenTask = listenTask;
    }

    public UEMReportTask getReportTask() {
        return reportTask;
    }

    public void setReportTask(UEMReportTask reportTask) {
        this.reportTask = reportTask;
    }

    public void setPort() {
        for (UEMCommPort childPort : listenTask.getExportPortList()) {
            UEMCommPort port = new UEMCommPort();
            port.setExport(false);
            port.setPortInfo(childPort);
            port.setName(listenTask.getName() + "_" + childPort.getName());
            port.setCounterTeam(childPort.getCounterTeam());
            port.setCounterTeamVariable(childPort.getCounterTeamVariable());
            port.setDirection(childPort.getDirection());
            port.setMessage(childPort.getMessage());
            port.setSampleSize(childPort.getSampleSize());
            port.setSampleType(childPort.getSampleType());
            port.setType(childPort.getType());
            port.setVariableType(childPort.getVariableType());
            setPortMap(port, listenTask, childPort);
            getPort().add(port);
        }
        for (UEMCommPort childPort : reportTask.getExportPortList()) {
            UEMCommPort port = new UEMCommPort();
            port.setExport(false);
            port.setPortInfo(childPort);
            port.setName(reportTask.getName() + "_" + childPort.getName());
            port.setCounterTeam(childPort.getCounterTeam());
            port.setCounterTeamVariable(childPort.getCounterTeamVariable());
            port.setDirection(childPort.getDirection());
            port.setMessage(childPort.getMessage());
            port.setSampleSize(childPort.getSampleSize());
            port.setSampleType(childPort.getSampleType());
            port.setType(childPort.getType());
            port.setVariableType(childPort.getVariableType());

            setPortMap(port, reportTask, childPort);
            getPort().add(port);
        }
    }

    private void setPortMap(UEMCommPort port, UEMCommTask childTask, UEMCommPort childPort) {
        UEMPortMap portMap = new UEMPortMap();
        portMap.setChildTask(childTask.getName());
        portMap.setChildTaskPort(childPort.getName());
        portMap.setTask(getName());
        portMap.setPort(port.getName());
        portMap.setDirection(childPort.getDirection());
        getPortMapList().add(portMap);
    }

    public UEMLeaderLibrary getLeaderLibraryTask() {
        return leaderLibraryTask;
    }

    public void setLeaderLibraryTask(UEMLeaderLibrary leaderLibraryTask) {
        this.leaderLibraryTask = leaderLibraryTask;
    }

    public UEMLeaderTask getLeaderTask() {
        return leaderTask;
    }

    public void setLeaderTask(UEMLeaderTask leaderTask) {
        this.leaderTask = leaderTask;
    }

    public UEMGroupActionTask getGroupActionTask() {
        return groupActionTask;
    }

    public void setGroupActionTask(UEMGroupActionTask groupActionTask) {
        this.groupActionTask = groupActionTask;
    }

    public List<UEMResourceTask> getResourceTaskList() {
        return resourceTaskList;
    }
}

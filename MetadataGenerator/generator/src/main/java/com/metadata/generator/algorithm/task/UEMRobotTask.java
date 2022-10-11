package com.metadata.generator.algorithm.task;

import java.util.ArrayList;
import java.util.List;
import com.metadata.generator.algorithm.UEMChannelPort;
import com.metadata.generator.algorithm.UEMCommPort;
import com.metadata.generator.algorithm.UEMLibrary;
import com.metadata.generator.algorithm.UEMPortMap;
import com.metadata.generator.constant.AlgorithmConstant;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import hopes.cic.xml.PortDirectionType;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TaskPortType;
import hopes.cic.xml.YesNoType;

public class UEMRobotTask extends UEMTask {
    private RobotImplWrapper robot;
    private List<UEMActionTask> actionTaskList = new ArrayList<>();
    private List<UEMLibrary> libraryTaskList = new ArrayList<>();
    private UEMListenTask listenTask;
    private UEMReportTask reportTask;

    public UEMRobotTask(String name, RobotImplWrapper robot) {
        super();
        setName(name);
        setRunCondition(RunConditionType.TIME_DRIVEN);
        setTaskType(AlgorithmConstant.COMPUTATION_TASK);
        setHasSubGraph(AlgorithmConstant.YES);
        setParentTask(name);
        setIsHardwareDependent(YesNoType.NO);
        setSubGraphProperty(AlgorithmConstant.PROCESS_NETWORK);
        this.robot = robot;
    }

    public RobotImplWrapper getRobot() {
        return robot;
    }

    public List<UEMActionTask> getActionTaskList() {
        return actionTaskList;
    }

    public void setActionTaskList(List<UEMActionTask> actionTaskList) {
        this.actionTaskList = actionTaskList;
    }

    public List<UEMLibrary> getLibraryTaskList() {
        return libraryTaskList;
    }

    public void setLibraryTaskList(List<UEMLibrary> libraryTaskList) {
        this.libraryTaskList = libraryTaskList;
    }

    public UEMLibrary getLibraryTask(String name) {
        for (UEMLibrary library : libraryTaskList) {
            if (library.getName().equals(name)) {
                return library;
            }
        }
        return null;
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
            setPortMap(port, listenTask, childPort);
        }
        for (UEMCommPort childPort : reportTask.getExportPortList()) {
            UEMCommPort port = new UEMCommPort();
            port.setExport(false);
            port.setPortInfo(childPort);
            setPortMap(port, reportTask, childPort);
        }
    }

    private void setPortMap(UEMCommPort port, UEMCommTask childTask, UEMCommPort childPort) {
        UEMPortMap portMap = new UEMPortMap();
        portMap.setChildTask(childTask.getName());
        portMap.setChildTaskPort(childPort.getName());
        portMap.setTask(getName());
        portMap.setPort(port.getName());
        portMap.setDirection(PortDirectionType.INPUT);
        portMap.setPort(cflags);
        getPortMapList().add(portMap);
    }
}

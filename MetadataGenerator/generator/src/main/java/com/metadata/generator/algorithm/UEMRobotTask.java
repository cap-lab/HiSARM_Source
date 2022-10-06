package com.metadata.generator.algorithm;

import java.util.ArrayList;
import java.util.List;
import com.metadata.generator.constant.AlgorithmConstant;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.YesNoType;

public class UEMRobotTask extends UEMTask {
    private RobotImplWrapper robot;
    private List<UEMActionTask> actionTaskList = new ArrayList<>();
    private List<UEMLibrary> libraryTaskList = new ArrayList<>();

    public UEMRobotTask(int taskIndex, String name, RobotImplWrapper robot) {
        super(taskIndex);
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

}

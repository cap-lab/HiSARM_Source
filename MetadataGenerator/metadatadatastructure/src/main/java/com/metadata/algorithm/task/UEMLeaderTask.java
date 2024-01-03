package com.metadata.algorithm.task;

import java.math.BigInteger;
import java.nio.file.Path;
import com.dbmanager.datastructure.task.Task;
import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.UEMModeTask;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.constant.AlgorithmConstant;
import com.strategy.strategydatastructure.enumeration.AdditionalTaskType;
import hopes.cic.xml.PortDirectionType;
import hopes.cic.xml.PortTypeType;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TimeMetricType;
import hopes.cic.xml.YesNoType;

public class UEMLeaderTask extends UEMTask {
    private Task leaderTask;
    private UEMLibraryPort leaderPort;
    private UEMChannelPort controlTaskPort;

    public UEMLeaderTask(UEMRobotTask robotTask, Path taskServer) throws Exception {
        super(robotTask.getName(), taskServer);
        leaderTask = robotTask.getRobot().getAdditionalTask(AdditionalTaskType.LEADER_SELECTION)
                .getTask();
        setFile(leaderTask.getCICFile());
        setName(robotTask.getName(), AlgorithmConstant.LEADER);
        setParentTask(robotTask.getName());
        setRunCondition(RunConditionType.TIME_DRIVEN);
        setHasSubGraph(AlgorithmConstant.NO);
        setLanguage(leaderTask.getLanguage());
        setIsHardwareDependent(YesNoType.NO);
        setTaskType(AlgorithmConstant.COMPUTATION_TASK);
        getExtraHeader().add(AlgorithmConstant.SEMO + AlgorithmConstant.LEADER_HEADER_SUFFIX);
        getExtraHeader().add(AlgorithmConstant.SEMO + AlgorithmConstant.GROUP_HEADER_SUFFIX);
        setPort();
        setLibraryPort();
        setMode();
    }

    private void setPort() {
        controlTaskPort = new UEMChannelPort();
        controlTaskPort.setDirection(PortDirectionType.INPUT);
        controlTaskPort.setExport(false);
        controlTaskPort.setIndex(0);
        controlTaskPort.setName("group_state");
        controlTaskPort.setSampleSize(BigInteger.valueOf(8));
        controlTaskPort.setType(PortTypeType.FIFO);
        controlTaskPort.setDefaultRate();
        getPort().add(controlTaskPort);
    }

    private void setLibraryPort() {
        this.leaderPort = new UEMLibraryPort();
        leaderPort.setName(AlgorithmConstant.LEADER);
        leaderPort.setType(AlgorithmConstant.LEADER);
        getLibraryMasterPort().add(leaderPort);
    }

    private void setMode() {
        UEMModeTask mode = new UEMModeTask();
        mode.setName(getName());
        mode.setDeadline(2);
        mode.setDeadlineUnit(TimeMetricType.S.value());
        mode.setPeriod(2);
        mode.setPeriodUnit(TimeMetricType.S.value());
        setMode(mode);
    }

    public UEMLibraryPort getLeaderPort() {
        return leaderPort;
    }

    public UEMChannelPort getControlTaskPort() {
        return controlTaskPort;
    }

    public Task getLeaderTask() {
        return leaderTask;
    }
}

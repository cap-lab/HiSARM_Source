package com.metadata.algorithm.task;

import java.math.BigInteger;
import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.library.UEMLibraryPort;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.PortDirectionType;
import hopes.cic.xml.PortTypeType;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TaskRateType;
import hopes.cic.xml.YesNoType;

public class UEMLeaderTask extends UEMTask {
    private UEMLibraryPort leaderPort;

    public UEMLeaderTask(UEMRobotTask robotTask) {
        super();
        setFile(AlgorithmConstant.COMMON_LEADER_SELECTION_SOURCE);
        setName(robotTask.getName(), AlgorithmConstant.LEADER);
        setParentTask(robotTask.getName());
        setRunCondition(RunConditionType.TIME_DRIVEN);
        setHasSubGraph(AlgorithmConstant.NO);
        setLanguage(AlgorithmConstant.LANGUAGE_C);
        setIsHardwareDependent(YesNoType.NO);
        setTaskType(AlgorithmConstant.COMPUTATION_TASK);
        getExtraHeader().add(AlgorithmConstant.COMMON_LEADER_SELECTION_HEADER);
        setPort();
        setLibraryPort();
    }

    private void setPort() {
        UEMChannelPort port = new UEMChannelPort();
        port.setDirection(PortDirectionType.INPUT);
        port.setExport(false);
        port.setIndex(0);
        port.setName("group_state");
        port.setSampleSize(BigInteger.valueOf(8));
        TaskRateType rate = new TaskRateType();
        rate.setMode(AlgorithmConstant.DEFAULT);
        rate.setRate(BigInteger.ONE);
        port.getRate().add(rate);
        port.setType(PortTypeType.FIFO);
        getPort().add(port);
    }

    private void setLibraryPort() {
        this.leaderPort = new UEMLibraryPort();
        leaderPort.setName("leader_lib");
        leaderPort.setType("leader_lib");
        getLibraryMasterPort().add(leaderPort);
    }

    public UEMLibraryPort getLeaderPort() {
        return leaderPort;
    }

}

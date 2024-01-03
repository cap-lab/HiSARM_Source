package com.metadata.algorithm.library;

import java.util.ArrayList;
import java.util.List;
import com.metadata.algorithm.task.UEMRobotTask;
import com.metadata.constant.AlgorithmConstant;
import com.strategy.strategydatastructure.enumeration.AdditionalTaskType;
import com.strategy.strategydatastructure.wrapper.LeaderSelectionAlgorithmWrapper;

public class UEMLeaderLibrary extends UEMLibrary {
    private List<String> groupList = new ArrayList<String>();
    private UEMLibraryPort leaderPort;
    private int sharedDataSize;

    public UEMLeaderLibrary(UEMRobotTask robot) {
        super(robot.getName());
        try {
            setFile(robot.getName() + AlgorithmConstant.LEADER_LIBRARY_SOURCE_SUFFIX);
            setHeader(robot.getName() + AlgorithmConstant.LEADER_LIBRARY_HEADER_SUFFIX);
            getExtraHeader().add(AlgorithmConstant.SEMO + AlgorithmConstant.LEADER_HEADER_SUFFIX);
            getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.LEADER_SOURCE_SUFFIX);
            getExtraHeader().add(robot.getName() + AlgorithmConstant.LEADER_DATA_HEADER_SUFFIX);
            getExtraSource().add(robot.getName() + AlgorithmConstant.LEADER_DATA_SOURCE_SUFFIX);
            getExtraHeader().add(AlgorithmConstant.MUTEX_HEADER);
            setName(robot.getName(), AlgorithmConstant.LEADER_LIB);
            setPort();

            LeaderSelectionAlgorithmWrapper leaderSelectionAlgorithmWrapper =
                    (LeaderSelectionAlgorithmWrapper) robot.getRobot()
                            .getAdditionalTask(AdditionalTaskType.LEADER_SELECTION);
            sharedDataSize = leaderSelectionAlgorithmWrapper.getLeaderSelectionAlgorithm()
                    .getSharedDataSize();

            this.groupList = new ArrayList<>(robot.getRobot().getGroupMap().keySet());
            getFunction()
                    .add(UEMLeaderLibraryFunction.makeSetFuncForLeaderFromListenOfSelectionInfo());
            getFunction().add(UEMLeaderLibraryFunction.makeSetFuncForLeaderFromListenOfHeartBeat());
            getFunction()
                    .add(UEMLeaderLibraryFunction.makeSetFuncForLeaderFromLeaderOfSelectionInfo());
            getFunction().add(
                    UEMLeaderLibraryFunction.makeAvailFuncForLeaderFromReportOfSelectionInfo());
            getFunction()
                    .add(UEMLeaderLibraryFunction.makeAvailFuncForLeaderFromReportOfHeartBeat());
            getFunction()
                    .add(UEMLeaderLibraryFunction.makeGetFuncForLeaderFromLeaderOfSelectionInfo());
            getFunction()
                    .add(UEMLeaderLibraryFunction.makeGetFuncForLeaderFromReportOfSelectionInfo());
            getFunction().add(UEMLeaderLibraryFunction.makeGetFuncForLeaderFromReportOfHeartBeat());
            getFunction().add(UEMLeaderLibraryFunction.makeSetLeaderSelectionState());
            getFunction().add(UEMLeaderLibraryFunction.makeGetLeaderSelectionState());
            getFunction().add(UEMLeaderLibraryFunction.makeGetLeader());
            getFunction().add(UEMLeaderLibraryFunction.makeSetLeader());
            getFunction().add(UEMLeaderLibraryFunction.makeRemoveMalfunctionedRobotFunc());
            getFunction().add(UEMLeaderLibraryFunction.makeGetNewRobotAddedFuncFromLeader());
            getFunction().add(UEMLeaderLibraryFunction.makeGetDuplicatedFuncFromLeader());
            getFunction().add(UEMLeaderLibraryFunction.makeGetGroupNumFromLeader());
            getFunction().add(UEMLeaderLibraryFunction.makeGetGroupIdFromLeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPort() {
        this.leaderPort = new UEMLibraryPort();
        leaderPort.setName(AlgorithmConstant.LEADER);
        leaderPort.setType(AlgorithmConstant.LEADER);
        getLibraryMasterPort().add(leaderPort);
    }

    public List<String> getGroupList() {
        return groupList;
    }

    public UEMLibraryPort getLeaderPort() {
        return leaderPort;
    }

    public int getSharedDataSize() {
        return sharedDataSize;
    }

    public int getSelectionInfoSize() {
        return AlgorithmConstant.MULTICAST_PACKET_HEADER_SIZE + sharedDataSize;
    }

    public int getHeartbeatSize() {
        return AlgorithmConstant.MULTICAST_PACKET_HEADER_SIZE
                + AlgorithmConstant.MULTICAST_ROBOT_ID_SIZE;
    }
}

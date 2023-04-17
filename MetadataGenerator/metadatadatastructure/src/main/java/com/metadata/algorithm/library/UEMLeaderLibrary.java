package com.metadata.algorithm.library;

import java.util.ArrayList;
import java.util.List;
import com.metadata.algorithm.task.UEMLeaderTask;
import com.metadata.algorithm.task.UEMRobotTask;
import com.metadata.constant.AlgorithmConstant;

public class UEMLeaderLibrary extends UEMLibrary {
    private List<String> groupList = new ArrayList<String>();
    private UEMLibraryPort leaderPort;

    public UEMLeaderLibrary(UEMRobotTask robot, UEMLeaderTask leaderTask) {
        super(robot.getName());
        setFile(robot.getName() + AlgorithmConstant.ROBOT_LEADER_LIBRARY_SOURCE_SUFFIX);
        setHeader(robot.getName() + AlgorithmConstant.ROBOT_LEADER_LIBRARY_HEADER_SUFFIX);
        getExtraHeader().add(robot.getName() + AlgorithmConstant.ROBOT_GROUP_HEADER_SUFFIX);
        getExtraSource().add(robot.getName() + AlgorithmConstant.ROBOT_GROUP_SOURCE_SUFFIX);
        getExtraHeader().add(AlgorithmConstant.COMMON_LEADER_HEADER);
        getExtraHeader().add(AlgorithmConstant.MUTEX_HEADER);
        setName(robot.getName(), AlgorithmConstant.LEADER);
        setPort();
        this.groupList = new ArrayList<>(robot.getRobot().getGroupMap().keySet());
        getFunction().add(UEMLeaderLibraryFunction.makeSetFuncForLeaderFromListenOfRobotId());
        getFunction().add(UEMLeaderLibraryFunction.makeSetFuncForLeaderFromListenOfHeartBeat());
        getFunction().add(UEMLeaderLibraryFunction.makeSetFuncForLeaderFromLeaderOfRobotId());
        getFunction().add(UEMLeaderLibraryFunction.makeSetFuncForLeaderFromLeaderOfHeartBeat());
        getFunction().add(UEMLeaderLibraryFunction.makeAvailFuncForLeaderFromLeaderOfRobotId());
        getFunction().add(UEMLeaderLibraryFunction.makeAvailFuncForLeaderFromLeaderOfHeartBeat());
        getFunction().add(UEMLeaderLibraryFunction.makeAvailFuncForLeaderFromReportOfRobotId());
        getFunction().add(UEMLeaderLibraryFunction.makeAvailFuncForLeaderFromReportOfHeartBeat());
        getFunction().add(UEMLeaderLibraryFunction.makeGetFuncForLeaderFromLeaderOfRobotId());
        getFunction().add(UEMLeaderLibraryFunction.makeGetFuncForLeaderFromLeaderOfHeartBeat());
        getFunction().add(UEMLeaderLibraryFunction.makeGetFuncForLeaderFromReportOfRobotId());
        getFunction().add(UEMLeaderLibraryFunction.makeGetFuncForLeaderFromReportOfHeartBeat());
        getFunction().add(UEMLeaderLibraryFunction.makeSetLeaderSelectionState());
        getFunction().add(UEMLeaderLibraryFunction.makeGetLeaderSelectionState());
        getFunction().add(UEMLeaderLibraryFunction.makeSetLastTime());
        getFunction().add(UEMLeaderLibraryFunction.makeGetLastTime());
        getFunction().add(UEMLeaderLibraryFunction.makeGetLeader());
        getFunction().add(UEMLeaderLibraryFunction.makeSetLeader());
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

}

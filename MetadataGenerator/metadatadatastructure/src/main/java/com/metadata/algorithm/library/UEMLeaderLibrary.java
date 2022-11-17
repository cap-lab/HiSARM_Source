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
        getFunction().add(UEMLibraryFunction.makeSetFuncForLeaderFromListenOfRobotId());
        getFunction().add(UEMLibraryFunction.makeSetFuncForLeaderFromListenOfHeartBeat());
        getFunction().add(UEMLibraryFunction.makeSetFuncForLeaderFromLeaderOfRobotId());
        getFunction().add(UEMLibraryFunction.makeSetFuncForLeaderFromLeaderOfHeartBeat());
        getFunction().add(UEMLibraryFunction.makeAvailFuncForLeaderFromLeaderOfRobotId());
        getFunction().add(UEMLibraryFunction.makeAvailFuncForLeaderFromLeaderOfHeartBeat());
        getFunction().add(UEMLibraryFunction.makeAvailFuncForLeaderFromReportOfRobotId());
        getFunction().add(UEMLibraryFunction.makeAvailFuncForLeaderFromReportOfHeartBeat());
        getFunction().add(UEMLibraryFunction.makeGetFuncForLeaderFromLeaderOfRobotId());
        getFunction().add(UEMLibraryFunction.makeGetFuncForLeaderFromLeaderOfHeartBeat());
        getFunction().add(UEMLibraryFunction.makeGetFuncForLeaderFromReportOfRobotId());
        getFunction().add(UEMLibraryFunction.makeGetFuncForLeaderFromReportOfHeartBeat());
        getFunction().add(UEMLibraryFunction.makeSetLeaderSelectionState());
        getFunction().add(UEMLibraryFunction.makeGetLeaderSelectionState());
        getFunction().add(UEMLibraryFunction.makeSetLastTime());
        getFunction().add(UEMLibraryFunction.makeGetLastTime());
        getFunction().add(UEMLibraryFunction.makeGetLeader());
        getFunction().add(UEMLibraryFunction.makeSetLeader());
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

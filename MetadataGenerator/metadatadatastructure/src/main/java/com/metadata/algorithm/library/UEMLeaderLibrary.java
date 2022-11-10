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
        super();
        setFile(robot.getName() + AlgorithmConstant.ROBOT_LEADER_LIBRARY_SOURCE_SUFFIX);
        setHeader(robot.getName() + AlgorithmConstant.ROBOT_LEADER_LIBRARY_HEADER_SUFFIX);
        setName(robot.getName(), AlgorithmConstant.LEADER);
        setPort();
        this.groupList = robot.getRobot().getGroupList();
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

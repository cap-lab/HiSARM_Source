package com.metadata.algorithm.library;

import java.math.BigInteger;
import com.dbmanager.datastructure.variable.PrimitiveType;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.YesNoType;

public class UEMLeaderLibraryFunction extends UEMLibraryFunction {
    private static UEMLibraryFunction makeAvailFuncForLeader(String caller, String id) {
        UEMLibraryFunction libraryFunction = makeAvailFunction(id, caller);
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeAvailFuncForLeaderFromReportOfSelectionInfo() {
        return makeAvailFuncForLeader(AlgorithmConstant.REPORT, "selection_info");
    }

    public static UEMLibraryFunction makeAvailFuncForLeaderFromReportOfHeartBeat() {
        return makeAvailFuncForLeader(AlgorithmConstant.REPORT, "heartbeat");
    }

    public static UEMLibraryFunction makeGetFuncForLeaderFromLeaderOfSelectionInfo() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("selection_info", AlgorithmConstant.LEADER, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("semo_int32*", "robot_num", YesNoType.YES));
        libraryFunction.getArgument().add(makeArgument("semo_int32*", "robot_list", YesNoType.YES));
        libraryFunction.getArgument().add(makeArgument("semo_int8*", "shared_data", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncForLeaderFromReportOfSelectionInfo() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("selection_info", AlgorithmConstant.REPORT, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("semo_int8*", "shared_data", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncForLeaderFromReportOfHeartBeat() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("heartbeat", AlgorithmConstant.REPORT, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("semo_int32*", "leader_id", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncForLeaderFromLeaderOfSelectionInfo() {
        UEMLibraryFunction libraryFunction =
                makeSetFunction("selection_info", AlgorithmConstant.LEADER, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument("semo_int8*", "shared_data", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncForLeaderFromListenOfSelectionInfo() {
        UEMLibraryFunction libraryFunction =
                makeSetFunction("selection_info", AlgorithmConstant.LISTEN, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("semo_int32", "robot_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument("semo_int64", "updated_time", YesNoType.YES));
        libraryFunction.getArgument().add(makeArgument("semo_int8*", "shared_data", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncForLeaderFromListenOfHeartBeat() {
        UEMLibraryFunction libraryFunction =
                makeSetFunction("heartbeat", AlgorithmConstant.LISTEN, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("semo_int32", "robot_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument("semo_int64", "heartbeat_time", YesNoType.YES));
        libraryFunction.getArgument().add(makeArgument("semo_int32", "leader_id", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetLeaderSelectionState() {
        UEMLibraryFunction libraryFunction =
                makeSetFunction("leader_selection_state", null, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument("LEADER_SELECTION_STATE", "state", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetLeaderSelectionState() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("leader_selection_state", null, "LEADER_SELECTION_STATE");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetLeader() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("leader", null, PrimitiveType.INT32.getValue());
        libraryFunction.setReturnSize(BigInteger.valueOf(PrimitiveType.INT32.getSize()));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetLeader() {
        UEMLibraryFunction libraryFunction = makeSetFunction("leader", null, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "robot_id", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetAvailRobot() {
        UEMLibraryFunction libraryFunction = makeGetFunction("avail_robot", null, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("semo_int32*", "robot_num", YesNoType.YES));
        libraryFunction.getArgument().add(makeArgument("semo_int32*", "robot_list", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetNewRobotAddedFuncFromLeader() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("new_robot_added", AlgorithmConstant.LEADER, "semo_int8");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetDuplicatedFuncFromLeader() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("duplicated", AlgorithmConstant.LEADER, "semo_int8");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeRemoveMalfunctionedRobotFunc() {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("remove_malfunctioned_robot");
        libraryFunction.setReturnType("semo_int8");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetGroupNumFromLeader() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("group_num", AlgorithmConstant.LEADER, "semo_int32");
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetGroupIdFromLeader() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("group_id", AlgorithmConstant.LEADER, "semo_int32");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "index", YesNoType.NO));
        return libraryFunction;
    }
}

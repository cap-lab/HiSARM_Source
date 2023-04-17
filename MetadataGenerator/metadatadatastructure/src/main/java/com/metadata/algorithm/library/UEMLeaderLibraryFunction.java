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

    public static UEMLibraryFunction makeAvailFuncForLeaderFromLeaderOfRobotId() {
        return makeAvailFuncForLeader(AlgorithmConstant.LEADER, "robot_id");
    }

    public static UEMLibraryFunction makeAvailFuncForLeaderFromLeaderOfHeartBeat() {
        return makeAvailFuncForLeader(AlgorithmConstant.LEADER, "heartbeat");
    }

    public static UEMLibraryFunction makeAvailFuncForLeaderFromReportOfRobotId() {
        return makeAvailFuncForLeader(AlgorithmConstant.REPORT, "robot_id");
    }

    public static UEMLibraryFunction makeAvailFuncForLeaderFromReportOfHeartBeat() {
        return makeAvailFuncForLeader(AlgorithmConstant.REPORT, "heartbeat");
    }

    private static UEMLibraryFunction makeGetFuncForLeader(String caller, String variable) {
        UEMLibraryFunction libraryFunction =
                makeGetFunction(variable, caller, PrimitiveType.INT32.getValue());
        libraryFunction.setReturnSize(BigInteger.valueOf(PrimitiveType.INT32.getSize()));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncForLeaderFromLeaderOfRobotId() {
        return makeGetFuncForLeader(AlgorithmConstant.LEADER, "robot_id");
    }

    public static UEMLibraryFunction makeGetFuncForLeaderFromLeaderOfHeartBeat() {
        return makeGetFuncForLeader(AlgorithmConstant.LEADER, "heartbeat");
    }

    public static UEMLibraryFunction makeGetFuncForLeaderFromReportOfRobotId() {
        return makeGetFuncForLeader(AlgorithmConstant.REPORT, "robot_id");
    }

    public static UEMLibraryFunction makeGetFuncForLeaderFromReportOfHeartBeat() {
        return makeGetFuncForLeader(AlgorithmConstant.REPORT, "heartbeat");
    }

    private static UEMLibraryFunction makeSetFuncForLeader(String caller, String variable) {
        UEMLibraryFunction libraryFunction = makeSetFunction(variable, caller, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "robot_id", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncForLeaderFromLeaderOfRobotId() {
        return makeSetFuncForLeader(AlgorithmConstant.LEADER, "robot_id");
    }

    public static UEMLibraryFunction makeSetFuncForLeaderFromLeaderOfHeartBeat() {
        return makeSetFuncForLeader(AlgorithmConstant.LEADER, "heartbeat");
    }

    public static UEMLibraryFunction makeSetFuncForLeaderFromListenOfRobotId() {
        return makeSetFuncForLeader(AlgorithmConstant.LISTEN, "robot_id");
    }

    public static UEMLibraryFunction makeSetFuncForLeaderFromListenOfHeartBeat() {
        return makeSetFuncForLeader(AlgorithmConstant.LISTEN, "heartbeat");
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

    public static UEMLibraryFunction makeGetLastTime() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("last_time", null, PrimitiveType.INT64.getValue());
        libraryFunction.setReturnSize(BigInteger.valueOf(PrimitiveType.INT64.getSize()));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetLastTime() {
        UEMLibraryFunction libraryFunction = makeSetFunction("last_time", null, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT64.getValue(), "time", YesNoType.NO));
        return libraryFunction;
    }
}

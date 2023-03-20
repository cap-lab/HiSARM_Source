package com.metadata.algorithm.library;

import java.math.BigInteger;
import com.dbmanager.datastructure.variable.PrimitiveType;
import com.metadata.constant.AlgorithmConstant;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.xml.LibraryFunctionArgumentType;
import hopes.cic.xml.LibraryFunctionType;
import hopes.cic.xml.YesNoType;

public class UEMLibraryFunction extends LibraryFunctionType {
    private static String makeFuncName(String funcId, String id, String caller) {
        String result = funcId;
        if (id != null) {
            result = result + "_" + id;
        }
        if (caller != null) {
            result = result + "_" + caller;
        }
        return result;
    }

    private static LibraryFunctionArgumentType makeArgument(String type, String name,
            YesNoType isPointer) {
        LibraryFunctionArgumentType argument = new LibraryFunctionArgumentType();
        argument.setName(name);
        argument.setType(type);
        argument.setPointer(isPointer);
        argument.setOutput(isPointer);
        return argument;
    }

    private static UEMLibraryFunction makeAvailFunction(String id, String caller) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName(makeFuncName("avail", id, caller));
        libraryFunction.setReturnType(PrimitiveType.INT8.getValue());
        libraryFunction.setReturnSize(BigInteger.valueOf(PrimitiveType.INT8.getSize()));
        return libraryFunction;
    }

    private static UEMLibraryFunction makeGetFunction(String id, String caller, String returType) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName(makeFuncName("get", id, caller));
        libraryFunction.setReturnType(returType);
        return libraryFunction;
    }

    private static UEMLibraryFunction makeSetFunction(String id, String caller, String returnType) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName(makeFuncName("set", id, caller));
        libraryFunction.setReturnType(returnType);
        return libraryFunction;
    }

    public static UEMLibraryFunction makeAvailFuncFromReport(VariableTypeWrapper variableType) {
        return makeAvailFunction(variableType.getVariableType().getName(),
                AlgorithmConstant.REPORT);
    }

    public static UEMLibraryFunction makeAvailFuncFromAction(VariableTypeWrapper variableType) {
        return makeAvailFunction(variableType.getVariableType().getName(),
                AlgorithmConstant.ACTION);
    }

    public static UEMLibraryFunction makeGetFuncFromAction(VariableTypeWrapper variableType) {
        UEMLibraryFunction libraryFunction = makeGetFunction(
                variableType.getVariableType().getName(), AlgorithmConstant.ACTION, "void");
        libraryFunction.getArgument().add(makeArgument("void*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncFromReport(VariableTypeWrapper variableType) {
        UEMLibraryFunction libraryFunction = makeGetFunction(
                variableType.getVariableType().getName(), AlgorithmConstant.REPORT, "void");
        libraryFunction.getArgument().add(makeArgument("void*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncFromAction(VariableTypeWrapper variableType) {
        UEMLibraryFunction libraryFunction = makeSetFunction(
                variableType.getVariableType().getName(), AlgorithmConstant.ACTION, "void");
        libraryFunction.getArgument().add(makeArgument("void*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncFromListen(VariableTypeWrapper variableType) {
        UEMLibraryFunction libraryFunction = makeSetFunction(
                variableType.getVariableType().getName(), AlgorithmConstant.LISTEN, "void");
        libraryFunction.getArgument().add(makeArgument("void*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

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

    public static UEMLibraryFunction makeSetFuncFromListenForGroupAction() {
        UEMLibraryFunction libraryFunction =
                makeSetFunction(AlgorithmConstant.GROUP_ACTION, AlgorithmConstant.LISTEN, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "action_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "robot_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT64.getValue(), "time", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncFromControlForGroupAction() {
        UEMLibraryFunction libraryFunction =
                makeSetFunction(AlgorithmConstant.GROUP_ACTION, AlgorithmConstant.CONTROL, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "action_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT8.getValue(), "sync_state", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT64.getValue(), "time", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetRobotIdFuncFromControlForGroupAction() {
        UEMLibraryFunction libraryFunction =
                makeSetFunction("robot_id", AlgorithmConstant.CONTROL, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "action_id", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncFromControlForGroupAction() {
        UEMLibraryFunction libraryFunction = makeGetFunction(AlgorithmConstant.GROUP_ACTION,
                AlgorithmConstant.CONTROL, PrimitiveType.INT8.getValue());
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "action_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT64.getValue(), "time", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeAvailFuncFromReportForGroupAction() {
        UEMLibraryFunction libraryFunction =
                makeAvailFunction(AlgorithmConstant.GROUP_ACTION, AlgorithmConstant.REPORT);
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "action_id", YesNoType.NO));
        return libraryFunction;
    }
}

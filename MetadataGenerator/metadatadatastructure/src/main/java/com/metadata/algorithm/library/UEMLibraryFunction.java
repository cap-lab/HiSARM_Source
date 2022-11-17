package com.metadata.algorithm.library;

import java.math.BigInteger;
import com.dbmanager.datastructure.variable.PrimitiveType;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.xml.LibraryFunctionArgumentType;
import hopes.cic.xml.LibraryFunctionType;
import hopes.cic.xml.YesNoType;

public class UEMLibraryFunction extends LibraryFunctionType {
    private static UEMLibraryFunction makeAvailFunction(VariableTypeWrapper variableType,
            String caller) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("avail_" + variableType.getVariableType().getName() + "_" + caller);
        libraryFunction.setReturnType(PrimitiveType.INT8.getValue());
        libraryFunction.setReturnSize(BigInteger.valueOf(PrimitiveType.INT8.getSize()));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeAvailFuncFromReport(VariableTypeWrapper variableType) {
        return makeAvailFunction(variableType, "report");
    }

    public static UEMLibraryFunction makeAvailFuncFromAction(VariableTypeWrapper variableType) {
        return makeAvailFunction(variableType, "action");
    }

    private static UEMLibraryFunction makeGetFunction(VariableTypeWrapper variableType,
            String caller) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("get_" + variableType.getVariableType().getName() + "_" + caller);
        libraryFunction.setReturnType("void");
        LibraryFunctionArgumentType argument = new LibraryFunctionArgumentType();
        argument.setName("buffer");
        argument.setType("void*");
        argument.setPointer(YesNoType.YES);
        argument.setOutput(YesNoType.YES);
        libraryFunction.getArgument().add(argument);
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncFromAction(VariableTypeWrapper variableType) {
        return makeGetFunction(variableType, "action");
    }

    public static UEMLibraryFunction makeGetFuncFromReport(VariableTypeWrapper variableType) {
        return makeGetFunction(variableType, "report");
    }

    private static UEMLibraryFunction makeSetFunction(VariableTypeWrapper variableType,
            String caller) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("set_" + variableType.getVariableType().getName() + "_" + caller);
        libraryFunction.setReturnType("void");
        LibraryFunctionArgumentType argument = new LibraryFunctionArgumentType();
        argument.setName("buffer");
        argument.setType("void*");
        argument.setPointer(YesNoType.YES);
        argument.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument);
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncFromAction(VariableTypeWrapper variableType) {
        return makeSetFunction(variableType, "action");
    }

    public static UEMLibraryFunction makeSetFuncFromListen(VariableTypeWrapper variableType) {
        return makeSetFunction(variableType, "listen");
    }

    private static UEMLibraryFunction makeAvailFuncForLeader(String caller, String variable) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("avail_" + variable + "_" + caller);
        libraryFunction.setReturnType(PrimitiveType.INT8.getValue());
        libraryFunction.setReturnSize(BigInteger.valueOf(PrimitiveType.INT8.getSize()));
        LibraryFunctionArgumentType argument = new LibraryFunctionArgumentType();
        argument.setName("group_id");
        argument.setType(PrimitiveType.INT32.getValue());
        argument.setPointer(YesNoType.NO);
        argument.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument);
        return libraryFunction;
    }

    public static UEMLibraryFunction makeAvailFuncForLeaderFromLeaderOfRobotId() {
        return makeAvailFuncForLeader("leader", "robot_id");
    }

    public static UEMLibraryFunction makeAvailFuncForLeaderFromLeaderOfHeartBeat() {
        return makeAvailFuncForLeader("leader", "heartbeat");
    }

    public static UEMLibraryFunction makeAvailFuncForLeaderFromReportOfRobotId() {
        return makeAvailFuncForLeader("report", "robot_id");
    }

    public static UEMLibraryFunction makeAvailFuncForLeaderFromReportOfHeartBeat() {
        return makeAvailFuncForLeader("report", "heartbeat");
    }

    private static UEMLibraryFunction makeGetFuncForLeader(String caller, String variable) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("get_" + variable + "_" + caller);
        libraryFunction.setReturnType(PrimitiveType.INT32.getValue());
        libraryFunction.setReturnSize(BigInteger.valueOf(PrimitiveType.INT32.getSize()));
        LibraryFunctionArgumentType argument = new LibraryFunctionArgumentType();
        argument.setName("group_id");
        argument.setType(PrimitiveType.INT32.getValue());
        argument.setPointer(YesNoType.NO);
        argument.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument);
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncForLeaderFromLeaderOfRobotId() {
        return makeGetFuncForLeader("leader", "robot_id");
    }

    public static UEMLibraryFunction makeGetFuncForLeaderFromLeaderOfHeartBeat() {
        return makeGetFuncForLeader("leader", "heartbeat");
    }

    public static UEMLibraryFunction makeGetFuncForLeaderFromReportOfRobotId() {
        return makeGetFuncForLeader("report", "robot_id");
    }

    public static UEMLibraryFunction makeGetFuncForLeaderFromReportOfHeartBeat() {
        return makeGetFuncForLeader("report", "heartbeat");
    }

    private static UEMLibraryFunction makeSetFuncForLeader(String caller, String variable) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("set_" + variable + "_" + caller);
        libraryFunction.setReturnType("void");
        LibraryFunctionArgumentType argument1 = new LibraryFunctionArgumentType();
        argument1.setName("group_id");
        argument1.setType(PrimitiveType.INT32.getValue());
        argument1.setPointer(YesNoType.NO);
        argument1.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument1);
        LibraryFunctionArgumentType argument2 = new LibraryFunctionArgumentType();
        argument2.setName("robot_id");
        argument2.setSize(BigInteger.valueOf(PrimitiveType.INT32.getSize()));
        argument2.setType(PrimitiveType.INT32.getValue());
        argument2.setPointer(YesNoType.NO);
        argument2.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument2);
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncForLeaderFromLeaderOfRobotId() {
        return makeSetFuncForLeader("leader", "robot_id");
    }

    public static UEMLibraryFunction makeSetFuncForLeaderFromLeaderOfHeartBeat() {
        return makeSetFuncForLeader("leader", "heartbeat");
    }

    public static UEMLibraryFunction makeSetFuncForLeaderFromListenOfRobotId() {
        return makeSetFuncForLeader("listen", "robot_id");
    }

    public static UEMLibraryFunction makeSetFuncForLeaderFromListenOfHeartBeat() {
        return makeSetFuncForLeader("listen", "heartbeat");
    }

    public static UEMLibraryFunction makeSetLeaderSelectionState() {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("set_leader_selection_state");
        libraryFunction.setReturnType("void");
        LibraryFunctionArgumentType argument1 = new LibraryFunctionArgumentType();
        argument1.setName("group_id");
        argument1.setType(PrimitiveType.INT32.getValue());
        argument1.setPointer(YesNoType.NO);
        argument1.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument1);
        LibraryFunctionArgumentType argument2 = new LibraryFunctionArgumentType();
        argument2.setName("state");
        argument2.setType("LEADER_SELECTION_STATE");
        argument2.setPointer(YesNoType.NO);
        argument2.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument2);
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetLeaderSelectionState() {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("get_leader_selection_state");
        libraryFunction.setReturnType("LEADER_SELECTION_STATE");
        LibraryFunctionArgumentType argument1 = new LibraryFunctionArgumentType();
        argument1.setName("group_id");
        argument1.setType(PrimitiveType.INT32.getValue());
        argument1.setPointer(YesNoType.NO);
        argument1.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument1);
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetLastTime() {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("get_last_time");
        libraryFunction.setReturnType(PrimitiveType.INT64.getValue());
        libraryFunction.setReturnSize(BigInteger.valueOf(PrimitiveType.INT64.getSize()));
        LibraryFunctionArgumentType argument = new LibraryFunctionArgumentType();
        argument.setName("group_id");
        argument.setType(PrimitiveType.INT32.getValue());
        argument.setPointer(YesNoType.NO);
        argument.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument);
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetLastTime() {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("set_last_time");
        libraryFunction.setReturnType("void");
        LibraryFunctionArgumentType argument1 = new LibraryFunctionArgumentType();
        argument1.setName("group_id");
        argument1.setType(PrimitiveType.INT32.getValue());
        argument1.setPointer(YesNoType.NO);
        argument1.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument1);
        LibraryFunctionArgumentType argument2 = new LibraryFunctionArgumentType();
        argument2.setName("time");
        argument2.setSize(BigInteger.valueOf(PrimitiveType.INT64.getSize()));
        argument2.setType(PrimitiveType.INT64.getValue());
        argument2.setPointer(YesNoType.NO);
        argument2.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument2);
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetLeader() {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("get_leader");
        libraryFunction.setReturnType(PrimitiveType.INT32.getValue());
        libraryFunction.setReturnSize(BigInteger.valueOf(PrimitiveType.INT32.getSize()));
        LibraryFunctionArgumentType argument = new LibraryFunctionArgumentType();
        argument.setName("group_id");
        argument.setType(PrimitiveType.INT32.getValue());
        argument.setPointer(YesNoType.NO);
        argument.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument);
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetLeader() {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName("set_leader");
        libraryFunction.setReturnType("void");
        LibraryFunctionArgumentType argument1 = new LibraryFunctionArgumentType();
        argument1.setName("group_id");
        argument1.setType(PrimitiveType.INT32.getValue());
        argument1.setPointer(YesNoType.NO);
        argument1.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument1);
        LibraryFunctionArgumentType argument2 = new LibraryFunctionArgumentType();
        argument2.setName("robot_id");
        argument2.setSize(BigInteger.valueOf(PrimitiveType.INT32.getSize()));
        argument2.setType(PrimitiveType.INT32.getValue());
        argument2.setPointer(YesNoType.NO);
        argument2.setOutput(YesNoType.NO);
        libraryFunction.getArgument().add(argument2);
        return libraryFunction;
    }
}

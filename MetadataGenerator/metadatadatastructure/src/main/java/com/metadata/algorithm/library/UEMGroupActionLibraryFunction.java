package com.metadata.algorithm.library;

import com.dbmanager.datastructure.variable.PrimitiveType;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.YesNoType;

public class UEMGroupActionLibraryFunction extends UEMLibraryFunction {
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

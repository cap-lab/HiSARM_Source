package com.metadata.algorithm.library;

import com.dbmanager.datastructure.variable.PrimitiveType;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.YesNoType;

public class UEMGroupingLibraryFunction extends UEMLibraryFunction {
    public static UEMLibraryFunction makeGetGroupCandidateList() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("grouping_candidate_list", null, "SEMO_GROUP*");
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetGroupCandidateNum() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("grouping_candidate_num", null, PrimitiveType.INT32.getValue());
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetGroupInfo() {
        UEMLibraryFunction libraryFunction = makeGetFunction("group_info", null, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "group_id", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("SEMO_GROUP*", "group", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetGroupSharedDataFromGrouping() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("shared_data", AlgorithmConstant.GROUPING, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "index", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("semo_uint8*", "data", YesNoType.YES));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "length", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetGroupSharedDataFromReport() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("shared_data", AlgorithmConstant.REPORT, "void");
        libraryFunction.getArgument().add(makeArgument("semo_int32*", "mode_id", YesNoType.YES));
        libraryFunction.getArgument().add(makeArgument("semo_uint8*", "data", YesNoType.YES));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "length", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeAvailSharedDataFromReport() {
        UEMLibraryFunction libraryFunction =
                makeAvailFunction("shared_data", AlgorithmConstant.REPORT);
        return libraryFunction;

    }

    public static UEMLibraryFunction makeSetGroupSelectionState() {
        UEMLibraryFunction libraryFunction = makeSetFunction("grouping_state", null, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "mode", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument("SEMO_GROUP_SELECTION_STATE", "state", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetGroupSelectionState() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("grouping_state", null, "SEMO_GROUP_SELECTION_STATE");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "mode", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetGroupSharedDataFromGrouping() {
        UEMLibraryFunction libraryFunction =
                makeSetFunction("shared_data", AlgorithmConstant.GROUPING, "void");
        libraryFunction.getArgument().add(makeArgument("semo_uint8*", "data", YesNoType.YES));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "length", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetGroupSharedDataFromListen() {
        UEMLibraryFunction libraryFunction =
                makeSetFunction("shared_data", AlgorithmConstant.LISTEN, "void");
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "robot_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "mode_id", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("semo_uint8*", "data", YesNoType.YES));
        libraryFunction.getArgument()
                .add(makeArgument(PrimitiveType.INT32.getValue(), "length", YesNoType.NO));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetGroupSharedRobotNum() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction("shared_robot_num", null, PrimitiveType.INT32.getValue());
        return libraryFunction;
    }
}

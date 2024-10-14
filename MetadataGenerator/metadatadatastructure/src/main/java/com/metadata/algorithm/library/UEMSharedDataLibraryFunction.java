package com.metadata.algorithm.library;

import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.YesNoType;

public class UEMSharedDataLibraryFunction extends UEMLibraryFunction {
    public static UEMLibraryFunction makeAvailFuncFromReport() {
        return makeAvailFunction(AlgorithmConstant.SHARED_DATA, AlgorithmConstant.REPORT);
    }

    public static UEMLibraryFunction makeGetFuncFromAction() {
        UEMLibraryFunction libraryFunction =
                makeGetFunction(AlgorithmConstant.SHARED_DATA, AlgorithmConstant.ACTION, "void");
        libraryFunction.getArgument().add(makeArgument("semo_int32*", "robot_num", YesNoType.YES));
        libraryFunction.getArgument().add(makeArgument("semo_int32*", "robot_list", YesNoType.YES));
        libraryFunction.getArgument().add(makeArgument("void*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetSpecificFuncFromAction() {
        UEMLibraryFunction libraryFunction = makeGetFunction(
                "specific_" + AlgorithmConstant.SHARED_DATA, AlgorithmConstant.ACTION, "semo_int8");
        libraryFunction.getArgument().add(makeArgument("semo_int32", "robot_id", YesNoType.NO));
        libraryFunction.getArgument().add(makeArgument("void*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeGetFuncFromReport() {
        UEMLibraryFunction libraryFunction = makeGetFunction(
                AlgorithmConstant.SHARED_DATA, AlgorithmConstant.REPORT, "void");
        libraryFunction.getArgument().add(makeArgument("semo_int8*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncFromAction() {
        UEMLibraryFunction libraryFunction = makeSetFunction(
                AlgorithmConstant.SHARED_DATA, AlgorithmConstant.ACTION, "void");
        libraryFunction.getArgument().add(makeArgument("void*", "buffer", YesNoType.YES));
        return libraryFunction;
    }

    public static UEMLibraryFunction makeSetFuncFromListen() {
        UEMLibraryFunction libraryFunction = makeSetFunction(
                AlgorithmConstant.SHARED_DATA, AlgorithmConstant.LISTEN, "void");
        libraryFunction.getArgument().add(makeArgument("semo_int32", "robot_id", YesNoType.NO));
        libraryFunction.getArgument()
                .add(makeArgument("semo_int64", "updated_time", YesNoType.YES));
        libraryFunction.getArgument().add(makeArgument("semo_int8*", "buffer", YesNoType.YES));
        return libraryFunction;
    }
}

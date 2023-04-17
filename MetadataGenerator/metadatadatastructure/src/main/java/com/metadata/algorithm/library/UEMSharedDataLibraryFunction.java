package com.metadata.algorithm.library;

import com.metadata.constant.AlgorithmConstant;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.xml.YesNoType;

public class UEMSharedDataLibraryFunction extends UEMLibraryFunction {
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



}

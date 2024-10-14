package com.metadata.algorithm.library;

import java.math.BigInteger;
import com.dbmanager.datastructure.variable.PrimitiveType;
import hopes.cic.xml.LibraryFunctionArgumentType;
import hopes.cic.xml.LibraryFunctionType;
import hopes.cic.xml.YesNoType;

public class UEMLibraryFunction extends LibraryFunctionType {
    protected static String makeFuncName(String funcId, String id, String caller) {
        String result = funcId;
        if (id != null) {
            result = result + "_" + id;
        }
        if (caller != null) {
            result = result + "_" + caller;
        }
        return result;
    }

    protected static LibraryFunctionArgumentType makeArgument(String type, String name,
            YesNoType isPointer) {
        LibraryFunctionArgumentType argument = new LibraryFunctionArgumentType();
        argument.setName(name);
        argument.setType(type);
        argument.setPointer(isPointer);
        argument.setOutput(isPointer);
        return argument;
    }

    protected static UEMLibraryFunction makeAvailFunction(String id, String caller) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName(makeFuncName("avail", id, caller));
        libraryFunction.setReturnType(PrimitiveType.INT8.getValue());
        libraryFunction.setReturnSize(BigInteger.valueOf(PrimitiveType.INT8.getSize()));
        return libraryFunction;
    }

    protected static UEMLibraryFunction makeGetFunction(String id, String caller,
            String returType) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName(makeFuncName("get", id, caller));
        libraryFunction.setReturnType(returType);
        return libraryFunction;
    }

    protected static UEMLibraryFunction makeSetFunction(String id, String caller,
            String returnType) {
        UEMLibraryFunction libraryFunction = new UEMLibraryFunction();
        libraryFunction.setName(makeFuncName("set", id, caller));
        libraryFunction.setReturnType(returnType);
        return libraryFunction;
    }
}

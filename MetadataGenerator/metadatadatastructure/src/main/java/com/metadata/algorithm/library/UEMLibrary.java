package com.metadata.algorithm.library;

import java.math.BigInteger;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.LibraryType;
import hopes.cic.xml.YesNoType;

public class UEMLibrary extends LibraryType {
    public UEMLibrary(String robotId) {
        setHasInternalStates(YesNoType.YES);
        setLanguage(AlgorithmConstant.LANGUAGE_C);
        getExtraHeader().add(AlgorithmConstant.SEMO + AlgorithmConstant.COMMON_HEADER_SUFFIX);
        getExtraHeader().add(AlgorithmConstant.SEMO + AlgorithmConstant.LOGGER_HEADER_SUFFIX);
        getExtraHeader().add(AlgorithmConstant.SEMO + AlgorithmConstant.VARIABLE_HEADER_SUFFIX);
        getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.VARIABLE_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.COMMON_HEADER_SUFFIX);
        getExtraSource().add(robotId + AlgorithmConstant.VARIABLE_SOURCE_SUFFIX);
        getExtraHeader().add(robotId + AlgorithmConstant.VARIABLE_HEADER_SUFFIX);
        getExtraHeader().add("UFTimer.h");
        setCflags("");
        setLdflags("");
    }

    public static String makeName(String parentName, String scope, String libraryName) {
        if (scope == null) {
            return parentName + "_" + libraryName;
        } else {
            return parentName + "_" + scope + "_" + libraryName;
        }
    }

    public void setName(String parentName, String libraryName) {
        setName(makeName(parentName, null, libraryName));
        setType(getName());
    }

    public void setId(int index) {
        setId(BigInteger.valueOf(index));
    }
}

package com.metadata.algorithm.library;

import java.math.BigInteger;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.LibraryType;
import hopes.cic.xml.YesNoType;

public class UEMLibrary extends LibraryType {
    public UEMLibrary() {
        setHasInternalStates(YesNoType.YES);
        setLanguage(AlgorithmConstant.LANGUAGE_C);
    }

    public static String makeName(String parentName, String scope, String libraryName) {
        if (scope == null) {
            return parentName + libraryName;
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

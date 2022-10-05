package com.metadata.generator.algorithm;

import com.metadata.generator.constant.AlgorithmConstant;
import hopes.cic.xml.LibraryType;
import hopes.cic.xml.YesNoType;

public class UEMLibrary extends LibraryType {
    public UEMLibrary() {
        setHasInternalStates(YesNoType.YES);
    }

    public void setName(String parentName, String libraryName) {
        setName(parentName + "_" + libraryName);
    }

}

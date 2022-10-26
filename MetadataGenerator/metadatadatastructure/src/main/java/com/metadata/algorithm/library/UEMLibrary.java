package com.metadata.algorithm.library;

import hopes.cic.xml.LibraryType;
import hopes.cic.xml.YesNoType;

public class UEMLibrary extends LibraryType {
    public UEMLibrary() {
        setHasInternalStates(YesNoType.YES);
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
    }

}

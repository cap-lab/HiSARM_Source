package com.metadata.generator.algorithm;

import com.metadata.generator.constant.AlgorithmConstant;
import hopes.cic.xml.HardwareDependencyType;
import hopes.cic.xml.LibraryType;
import hopes.cic.xml.YesNoType;

public class UEMLibrary extends LibraryType {
    public UEMLibrary() {
        setHasInternalStates(YesNoType.YES);
    }

    public static String makeName(String parentName, String libraryName) {
        return parentName + "_" + libraryName;
    }

    public void setName(String parentName, String libraryName) {
        setName(makeName(parentName, libraryName));
    }

    public void makeGeneratedLibrary(String name) {
        setName(name);
        setCflags("");
        setLdflags("");
        setFile(getName() + AlgorithmConstant.LIBRARY_FILE_SUFFIX);
        setHeader(getName() + AlgorithmConstant.LIBRARY_HEADER_SUFFIX);
        setIsHardwareDependent(YesNoType.NO);
        setLanguage(AlgorithmConstant.LANGUAGE_C);
        setType(getName());
    }

}

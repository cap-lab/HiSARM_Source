package com.metadata.generator.algorithm;

import com.metadata.generator.constant.AlgorithmConstant;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.xml.LibraryType;
import hopes.cic.xml.YesNoType;

public class UEMLibrary extends LibraryType {
    private VariableTypeWrapper variableType;
    private String group;

    public UEMLibrary() {
        setHasInternalStates(YesNoType.YES);
    }

    public static String makeGroup(String groupScope, String actionName, int argIndex){
        return groupScope + "_" + actionName + "_" + String.valueOf(argIndex);
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

    public void makeGeneratedLibrary(String name) {
        setName(name);
        setCflags("");
        setLdflags("");
        setFile(getName() + AlgorithmConstant.LIBRARY_FILE_EXTENSION);
        setHeader(getName() + AlgorithmConstant.LIBRARY_HEADER_EXTENSION);
        setIsHardwareDependent(YesNoType.NO);
        setLanguage(AlgorithmConstant.LANGUAGE_C);
        setType(getName());
    }

    public VariableTypeWrapper getVariableType() {
        return variableType;
    }

    public void setVariableType(VariableTypeWrapper variableType) {
        this.variableType = variableType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}

package com.metadata.algorithm.library;

import com.metadata.algorithm.task.UEMRobotTask;
import com.metadata.constant.AlgorithmConstant;
import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.xml.YesNoType;

public class UEMSharedData extends UEMLibrary {
    private VariableTypeWrapper variableType;
    private String group;

    public UEMSharedData(UEMRobotTask robot) {
        super(robot.getName());
    }

    public static String makeGroup(String groupScope, String actionName, int argIndex) {
        return groupScope + "_" + actionName + "_" + String.valueOf(argIndex);
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
        getFunction().add(UEMLibraryFunction.makeAvailFuncFromReport(variableType));
        getFunction().add(UEMLibraryFunction.makeAvailFuncFromAction(variableType));
        getFunction().add(UEMLibraryFunction.makeGetFuncFromAction(variableType));
        getFunction().add(UEMLibraryFunction.makeGetFuncFromReport(variableType));
        getFunction().add(UEMLibraryFunction.makeSetFuncFromListen(variableType));
        getFunction().add(UEMLibraryFunction.makeSetFuncFromAction(variableType));

    }

    public void makeLIbraryFunctions() {

    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}

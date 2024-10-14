package com.metadata.algorithm.library;

import com.strategy.strategydatastructure.wrapper.VariableTypeWrapper;
import hopes.cic.xml.LibraryMasterPortType;

public class UEMLibraryPort extends LibraryMasterPortType {
    UEMLibrary library;
    VariableTypeWrapper variableType;
    int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public UEMLibrary getLibrary() {
        return library;
    }

    public void setLibrary(UEMLibrary library) {
        this.library = library;
    }

    public VariableTypeWrapper getVariableType() {
        return variableType;
    }

    public void setVariableType(VariableTypeWrapper variableType) {
        this.variableType = variableType;
    }

}

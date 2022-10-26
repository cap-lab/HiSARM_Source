package com.metadata.algorithm.library;

import hopes.cic.xml.LibraryMasterPortType;

public class UEMLibraryPort extends LibraryMasterPortType {
    UEMLibrary library;
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

}

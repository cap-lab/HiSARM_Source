package com.metadata.algorithm.library;

import com.metadata.algorithm.task.UEMTask;
import hopes.cic.xml.TaskLibraryConnectionType;

public class UEMLibraryConnection extends TaskLibraryConnectionType {

    public void setConnection(UEMTask task, UEMLibraryPort taskPort, UEMLibrary library) {
        setConnection(task.getName(), taskPort, library);
    }

    public void setConnection(String srcTask, UEMLibraryPort taskPort, UEMLibrary library) {
        setMasterTask(srcTask);
        setMasterPort(taskPort.getName());
        setSlaveLibrary(library.getName());
    }

}

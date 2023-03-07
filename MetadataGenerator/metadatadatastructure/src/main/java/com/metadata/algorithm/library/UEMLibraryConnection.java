package com.metadata.algorithm.library;

import com.metadata.algorithm.task.UEMTask;
import hopes.cic.xml.TaskLibraryConnectionType;

public class UEMLibraryConnection extends TaskLibraryConnectionType {

    public void setConnection(UEMTask task, UEMLibraryPort taskPort, UEMLibrary library) {
        setMasterTask(task.getName());
        setMasterPort(taskPort.getName());
        setSlaveLibrary(library.getName());
    }

    public void setConnection(String srcTask, UEMLibraryPort taskPort, UEMLibrary library) {
        setMasterTask(srcTask);
        setMasterPort(taskPort.getName());
        setSlaveLibrary(library.getName());
    }

}

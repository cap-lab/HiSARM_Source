package com.dbmanager.datastructure.task;

import com.dbmanager.datastructure.architecture.CPUArchitecture;
import com.dbmanager.datastructure.architecture.SWPlatform;

public class HardwareDependency {
    private SWPlatform swPlatform;
    private CPUArchitecture architecture;

    public SWPlatform getSwPlatform() {
        return swPlatform;
    }

    public void setSwPlatform(SWPlatform swPlatform) {
        this.swPlatform = swPlatform;
    }

    public CPUArchitecture getArchitecture() {
        return architecture;
    }

    public void setArchitecture(CPUArchitecture architecture) {
        this.architecture = architecture;
    }

}

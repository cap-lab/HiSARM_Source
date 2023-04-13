package com.strategy.strategydatastructure.util;

import java.util.List;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;

public interface CompileTimeAllocatorInterface {
    public List<RobotImplWrapper> allocate(List<RobotImplWrapper> robotList,
            MissionWrapper mission);
}

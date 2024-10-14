package com.strategy.strategydatastructure.util;

import java.util.List;
import java.util.Map;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;

public interface CompileTimeAllocatorInterface {
    public Map<String, Map<String, Integer>> allocate(List<RobotImplWrapper> robotList,
            MissionWrapper mission); // return: Map<robot id, Map<group id, group index>>
}

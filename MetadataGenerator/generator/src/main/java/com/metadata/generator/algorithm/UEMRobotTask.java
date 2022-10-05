package com.metadata.generator.algorithm;

import com.metadata.generator.constant.AlgorithmConstant;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.YesNoType;

public class UEMRobotTask extends UEMTask {
    public UEMRobotTask(int taskIndex, String name) {
        super(taskIndex);
        setName(name);
        setRunCondition(RunConditionType.TIME_DRIVEN);
        setTaskType(AlgorithmConstant.COMPUTATION_TASK);
        setHasSubGraph(AlgorithmConstant.YES);
        setParentTask(name);
        setIsHardwareDependent(YesNoType.NO);
        setSubGraphProperty(AlgorithmConstant.PROCESS_NETWORK);
    }

}

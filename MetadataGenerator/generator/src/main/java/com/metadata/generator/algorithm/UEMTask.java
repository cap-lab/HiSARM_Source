package com.metadata.generator.algorithm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.metadata.generator.constant.AlgorithmConstant;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TaskType;
import hopes.cic.xml.YesNoType;

public class UEMTask extends TaskType {
    UEMModeTask mode;
    List<UEMPortMap> portMapList;

    public UEMTask(int taskIndex) {
        super();
        portMapList = new ArrayList<>();
        setHasInternalStates(YesNoType.YES);
        setId(BigInteger.valueOf(taskIndex));
    }

    protected RunConditionType runCondition(String runCondition) throws Exception {
        if (runCondition.equals(AlgorithmConstant.CONTROL_DRIVEN)) {
            return RunConditionType.CONTROL_DRIVEN;
        } else if (runCondition.equals(AlgorithmConstant.TIME_DRIVEN)) {
            return RunConditionType.TIME_DRIVEN;
        } else if (runCondition.equals(AlgorithmConstant.DATA_DRIVEN)) {
            return RunConditionType.DATA_DRIVEN;
        } else {
            throw new Exception("Wrong Run Condition: " + runCondition);
        }
    }

    protected YesNoType convertYesNoType(boolean bool) {
        return bool == true ? YesNoType.YES : YesNoType.NO;
    }

    protected String convertYesNoString(boolean bool) {
        return bool == true ? AlgorithmConstant.YES : AlgorithmConstant.NO;
    }

    public static String makeName(String parentName, String taskName) {
        return parentName + "_" + taskName;
    }

    public void setName(String parentName, String taskName) {
        setName(makeName(parentName, taskName));
    }

    public void setMode(UEMModeTask mode) {
        this.mode = mode;
    }

    public List<UEMPortMap> getPortMapList() {
        return portMapList;
    }
}

package com.metadata.generator.algorithm.task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.metadata.generator.algorithm.UEMModeTask;
import com.metadata.generator.algorithm.UEMPortMap;
import com.metadata.generator.constant.AlgorithmConstant;
import hopes.cic.xml.MulticastPortType;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TaskPortType;
import hopes.cic.xml.TaskType;
import hopes.cic.xml.YesNoType;

public class UEMTask extends TaskType {
    UEMModeTask mode;
    List<UEMPortMap> portMapList = new ArrayList<>();

    public UEMTask() {
        super();
        setHasInternalStates(YesNoType.YES);
    }

    public void setId(int index) {
        setId(BigInteger.valueOf(index));
    }

    protected boolean existChannelPort(String portName) {
        for (TaskPortType port : getPort()) {
            if (port.getName().equals(portName)) {
                return true;
            }
        }
        return false;
    }

    protected boolean existMulticastPort(String portName) {
        for (MulticastPortType port : getMulticastPort()) {
            if (port.getName().equals(portName)) {
                return true;
            }
        }
        return false;
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

    public void setExtraCommonCode(String robotName) {
        getExtraHeader().add(AlgorithmConstant.COMMON_HEADER);
        getExtraHeader().add(robotName + AlgorithmConstant.ROBOT_COMMON_HEADER_SUFFIX);
    }
}

package com.metadata.algorithm.task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.metadata.algorithm.UEMModeTask;
import com.metadata.algorithm.UEMMulticastPortMap;
import com.metadata.algorithm.UEMPortMap;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.MulticastPortType;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TaskPortType;
import hopes.cic.xml.TaskType;
import hopes.cic.xml.YesNoType;

public class UEMTask extends TaskType {
    private UEMModeTask uemMode;
    private List<UEMPortMap> portMapList = new ArrayList<>();
    private List<UEMLibraryPortMap> libPortMapList = new ArrayList<>();
    private List<UEMMulticastPortMap> multicastPortMapList = new ArrayList<>();
    private String taskName;

    public UEMTask(String robotName) {
        super();
        setHasInternalStates(YesNoType.YES);
        setFile(AlgorithmConstant.DEFAULT_FILE);
        setLanguage(AlgorithmConstant.LANGUAGE_C);
        getExtraHeader().add(AlgorithmConstant.SEMO + AlgorithmConstant.COMMON_HEADER_SUFFIX);
        getExtraHeader().add(robotName + AlgorithmConstant.COMMON_HEADER_SUFFIX);
        getExtraHeader().add(AlgorithmConstant.SEMO + AlgorithmConstant.LOGGER_HEADER_SUFFIX);
        getExtraHeader().add(AlgorithmConstant.SEMO + AlgorithmConstant.VARIABLE_HEADER_SUFFIX);
        getExtraSource().add(AlgorithmConstant.SEMO + AlgorithmConstant.VARIABLE_SOURCE_SUFFIX);
        getExtraHeader().add("UFTimer.h");
        setCflags("");
        setLdflags("");
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
        this.taskName = taskName;
        setName(makeName(parentName, taskName));
    }

    public void setMode(UEMModeTask mode) {
        this.uemMode = mode;
    }

    public List<UEMPortMap> getPortMapList() {
        return portMapList;
    }

    public void setExtraCommonCode(String robotName) {}

    public UEMModeTask getUEMMode() {
        return uemMode;
    }

    public String getTaskName() {
        return taskName;
    }

    public List<UEMLibraryPortMap> getLibPortMapList() {
        return libPortMapList;
    }

    public List<UEMMulticastPortMap> getMulticastPortMapList() {
        return multicastPortMapList;
    }

}

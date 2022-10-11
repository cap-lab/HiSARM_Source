package com.metadata.generator.algorithm;

import java.com.xmlparser.xml.handler.TaskXMLTaskGraphHandler;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.metadata.generator.algorithm.task.UEMTask;
import com.metadata.generator.constant.AlgorithmConstant;
import com.xmlparser.TaskXMLChannelType;
import com.xmlparser.TaskXMLControlParamType;
import com.xmlparser.TaskXMLFuncArgumentType;
import com.xmlparser.TaskXMLHardwareDependencyType;
import com.xmlparser.TaskXMLLibFunctionType;
import com.xmlparser.TaskXMLLibPortType;
import com.xmlparser.TaskXMLLibraryConnectionType;
import com.xmlparser.TaskXMLLibraryPortType;
import com.xmlparser.TaskXMLLibraryType;
import com.xmlparser.TaskXMLParameterType;
import com.xmlparser.TaskXMLPortCategoryType;
import com.xmlparser.TaskXMLPortMapType;
import com.xmlparser.TaskXMLPortRateType;
import com.xmlparser.TaskXMLPortType;
import com.xmlparser.TaskXMLTaskGraphType;
import com.xmlparser.TaskXMLTaskType;
import com.xmlparser.TaskXMLextraSettingType;
import hopes.cic.xml.ChannelPortType;
import hopes.cic.xml.HardwareDependencyType;
import hopes.cic.xml.HardwarePlatformType;
import hopes.cic.xml.LibraryFunctionArgumentType;
import hopes.cic.xml.LibraryFunctionType;
import hopes.cic.xml.LibraryMasterPortType;
import hopes.cic.xml.PortDirectionType;
import hopes.cic.xml.PortTypeType;
import hopes.cic.xml.RunConditionType;
import hopes.cic.xml.TaskParameterType;
import hopes.cic.xml.TaskRateType;
import hopes.cic.xml.YesNoType;

public class TaskXMLtoAlgorithm {
    private TaskXMLTaskGraphHandler taskXMLHandler;

    public TaskXMLtoAlgorithm() {
        taskXMLHandler = new TaskXMLTaskGraphHandler();
    }

    private YesNoType convertYesNoString(String bool) {
        if (bool.equals(AlgorithmConstant.YES)) {
            return YesNoType.YES;
        } else {
            return YesNoType.NO;
        }
    }

    private RunConditionType convertRunConditionType(String condition) throws Exception {
        if (condition.equals(AlgorithmConstant.TIME_DRIVEN)) {
            return RunConditionType.TIME_DRIVEN;
        } else if (condition.equals(AlgorithmConstant.CONTROL_DRIVEN)) {
            return RunConditionType.CONTROL_DRIVEN;
        } else if (condition.equals(AlgorithmConstant.DATA_DRIVEN)) {
            return RunConditionType.DATA_DRIVEN;
        }
        throw new Exception("Wrong Run Condition: " + condition);
    }

    private PortTypeType convertPortType(TaskXMLPortCategoryType type) {
        if (type.equals(TaskXMLPortCategoryType.FIFO)) {
            return PortTypeType.FIFO;
        } else if (type.equals(TaskXMLPortCategoryType.OVERWRITABLE)) {
            return PortTypeType.OVERWRITABLE;
        }
        return null;
    }

    private List<UEMChannelPort> addPortList(List<TaskXMLPortType> beforePortList,
            PortDirectionType direction) {
        List<UEMChannelPort> afterPortList = new ArrayList<>();
        for (TaskXMLPortType beforePort : beforePortList) {
            UEMChannelPort afterPort = new UEMChannelPort();
            afterPort.setName(beforePort.getName());
            afterPort.setDirection(direction);
            afterPort.setType(convertPortType(beforePort.getType()));
            afterPort.setSampleSize(beforePort.getSampleSize());
            afterPort.setExport(beforePort.isExport());
            for (TaskXMLPortRateType beforeRate : beforePort.getRate()) {
                TaskRateType afterRate = new TaskRateType();
                afterRate.setMode(beforeRate.getMode());
                afterRate.setRate(beforeRate.getRate());
                afterPort.getRate().add(afterRate);
            }
            afterPortList.add(afterPort);
        }
        return afterPortList;
    }

    private void convertConfig(UEMTask after, TaskXMLTaskType before) throws Exception {
        UEMModeTask mode = new UEMModeTask();
        mode.setName(after.getName());
        after.setMode(mode);
        for (TaskXMLParameterType param : before.getConfig().getParameter()) {
            switch (param.getName()) {
                case HAS_INTERNAL_STATES:
                    after.setHasInternalStates(convertYesNoString(param.getValue()));
                    break;
                case RUN_CONDITION:
                    after.setRunCondition(convertRunConditionType(param.getValue()));
                    break;
                case CIC_FILE:
                    after.setFile(param.getValue());
                    break;
                case COMPILE_FLAGS:
                    after.setCflags(param.getValue());
                    break;
                case LINK_FLAGS:
                    after.setLdflags(param.getValue());
                    break;
                case HAS_SUB_GRAPH:
                    after.setHasSubGraph(param.getValue());
                    break;
                case HAS_MTM:
                    after.setHasMTM(param.getValue());
                    break;
                case IS_DEPENDENCY:
                    after.setIsHardwareDependent(convertYesNoString(param.getValue()));
                    break;
                case SUBGRAPH_PROPERTY:
                    after.setSubGraphProperty(param.getValue());
                    break;
                case LANGUAGE:
                    after.setLanguage(param.getValue());
                    break;
                case PRIORITY:
                    mode.setPriority(Integer.parseInt(param.getValue()));
                    break;
                case PERIOD:
                    mode.setPeriod(Integer.parseInt(param.getValue()));
                    break;
                case PERIOD_UNIT:
                    mode.setPeriodUnit(param.getValue());
                    break;
                case DEADLINE:
                    mode.setDeadline(Integer.parseInt(param.getValue()));
                    break;
                case DEADLINE_UNIT:
                    mode.setDeadlineUnit(param.getValue());
                    break;
                default:
                    break;
            }
        }
    }

    private void convertExtraSetting(UEMTask after, TaskXMLTaskType before) {
        for (TaskXMLextraSettingType extraSetting : before.getExtraSettingList()
                .getExtraSetting()) {
            switch (extraSetting.getType()) {
                case EXTRA_CIC:
                    after.getExtraCIC().add(extraSetting.getName());
                    break;
                case EXTRA_FILE:
                    after.getExtraFile().add(extraSetting.getName());
                    break;
                case EXTRA_HEADER:
                    after.getExtraHeader().add(extraSetting.getName());
                    break;
                case EXTRA_SOURCE:
                    after.getExtraSource().add(extraSetting.getName());
                    break;
            }
        }
    }

    private void convertLibraryPort(UEMTask after, TaskXMLTaskType before) {
        for (TaskXMLLibraryPortType beforePort : before.getLibraryPortList().getLibraryPort()) {
            LibraryMasterPortType afterPort = new LibraryMasterPortType();
            afterPort.setName(beforePort.getName());
            afterPort.setType(beforePort.getType());
        }
    }

    private void convertControlParameter(UEMTask after, TaskXMLTaskType before) {
        if (before.getControl().getControlParams() != null) {
            for (TaskXMLControlParamType beforeParam : before.getControl().getControlParams()
                    .getParameter()) {
                TaskParameterType afterParam = new TaskParameterType();
                afterParam.setName(beforeParam.getName());
                afterParam.setType(beforeParam.getType().value());
                afterParam.setValue(beforeParam.getValue());
                after.getParameter().add(afterParam);
            }
        }
    }


    private void convertHardwareDependency(UEMTask after, TaskXMLTaskType before) {
        if (before.getHardwareDependency() != null) {
            after.setHardwareDependency(new HardwareDependencyType());
            for (TaskXMLHardwareDependencyType beforeHW : before.getHardwareDependency()
                    .getHardware()) {
                HardwarePlatformType afterHW = new HardwarePlatformType();
                afterHW.setArchitecture(beforeHW.getArchitecture());
                afterHW.setPlatform(beforeHW.getPlatform());
                afterHW.setRuntime(beforeHW.getRuntime());
                afterHW.setProcessorType(beforeHW.getProcessorType());
                after.getHardwareDependency().getHardware().add(afterHW);
            }
        }
    }

    private void convertPortMap(UEMTask after, TaskXMLTaskType before) {
        if (before.getPortMapList() != null) {
            for (TaskXMLPortMapType beforeMap : before.getPortMapList().getPortMap()) {
                UEMPortMap afterMap = new UEMPortMap();
                afterMap.setTask(after.getName());
                afterMap.setNotFlattenedTask(beforeMap.getMacroNode());
                afterMap.setPort(beforeMap.getMacroNodePort());
                afterMap.setChildTask(UEMTask.makeName(after.getName(), beforeMap.getInsideTask()));
                afterMap.setChildNotFlattenedTask(beforeMap.getInsideTask());
                afterMap.setChildTaskPort(beforeMap.getInsideTaskPort());
                after.getPortMapList().add(afterMap);
            }
        }
    }

    private UEMTask convertTask(String parentTasks, TaskXMLTaskType before) throws Exception {
        UEMTask after = new UEMTask();
        after.setName(parentTasks, before.getName());
        after.setHasInternalStates(YesNoType.YES);
        after.setHasSubGraph(before.getType().value());
        after.setParentTask(parentTasks);
        convertConfig(after, before);
        after.getPort()
                .addAll(addPortList(before.getInportList().getPort(), PortDirectionType.INPUT));
        after.getPort()
                .addAll(addPortList(before.getOutportList().getPort(), PortDirectionType.OUTPUT));
        convertExtraSetting(after, before);
        convertLibraryPort(after, before);
        convertControlParameter(after, before);
        convertHardwareDependency(after, before);
        convertPortMap(after, before);
        return after;
    }

    private UEMChannel convertChannel(TaskXMLChannelType before) {
        UEMChannel after = new UEMChannel();
        after.setInitialDataSize(before.getInitSize());
        after.setSampleSize(before.getSampleSize());
        after.setSampleType(before.getSampleType());
        after.setSize(before.getSize());
        ChannelPortType srcPort = new ChannelPortType();
        srcPort.setTask(before.getSrcTask());
        srcPort.setPort(before.getSrcPort());
        after.getSrc().add(srcPort);
        ChannelPortType dstPort = new ChannelPortType();
        dstPort.setTask(before.getDstTask());
        dstPort.setPort(before.getDstPort());
        after.getDst().add(dstPort);
        return after;
    }

    private void convertLibraryConfig(UEMLibrary after, TaskXMLLibraryType before) {
        for (TaskXMLParameterType param : before.getConfig().getParameter()) {
            switch (param.getName()) {
                case LIBRARY_TYPE:
                    after.setType(param.getValue());
                    break;
                case LIBRARY_FILE:
                    after.setFile(param.getValue());
                    break;
                case LIBRARY_HEADER:
                    after.setHeader(param.getValue());
                    break;
                case LANGUAGE:
                    after.setLanguage(param.getValue());
                    break;
                case COMPILE_FLAGS:
                    after.setCflags(param.getValue());
                    break;
                case LINK_FLAGS:
                    after.setLdflags(param.getValue());
                    break;
                case IS_DEPENDENCY:
                    after.setIsHardwareDependent(convertYesNoString(param.getValue()));
                    break;
                default:
                    break;
            }
        }
    }

    private void convertLibraryExtraSetting(UEMLibrary after, TaskXMLLibraryType before) {
        for (TaskXMLextraSettingType extraSetting : before.getExtraSettingList()
                .getExtraSetting()) {
            switch (extraSetting.getType()) {
                case EXTRA_CIC:
                    after.getExtraCIC().add(extraSetting.getName());
                    break;
                case EXTRA_FILE:
                    after.getExtraFile().add(extraSetting.getName());
                    break;
                case EXTRA_HEADER:
                    after.getExtraHeader().add(extraSetting.getName());
                    break;
                case EXTRA_SOURCE:
                    after.getExtraSource().add(extraSetting.getName());
                    break;
            }
        }
    }

    private void addLibraryMasterPort(UEMLibrary after, TaskXMLLibraryType before) {
        for (TaskXMLLibPortType beforePort : before.getInPortList().getInPort()) {
            LibraryMasterPortType afterPort = new LibraryMasterPortType();
            afterPort.setName(beforePort.getName());
            afterPort.setType(beforePort.getType().value());
            after.getLibraryMasterPort().add(afterPort);
        }
    }

    private void addLibraryFunction(UEMLibrary after, TaskXMLLibraryType before) {
        for (TaskXMLLibFunctionType beforeFunction : before.getFunctionList().getFunction()) {
            LibraryFunctionType afterFunction = new LibraryFunctionType();
            afterFunction.setName(beforeFunction.getName());
            afterFunction.setReturnType(beforeFunction.getType());
            for (TaskXMLFuncArgumentType beforeArg : beforeFunction.getArgument()) {
                LibraryFunctionArgumentType afterArg = new LibraryFunctionArgumentType();
                afterArg.setName(beforeArg.getName());
                afterArg.setType(beforeArg.getType());
                afterFunction.getArgument().add(afterArg);
            }
            after.getFunction().add(afterFunction);
        }
    }

    private UEMLibrary convertLibrary(String parentTasks, TaskXMLLibraryType before) {
        UEMLibrary after = new UEMLibrary();
        after.setName(parentTasks, before.getName());
        convertLibraryConfig(after, before);
        convertLibraryExtraSetting(after, before);
        addLibraryMasterPort(after, before);
        addLibraryFunction(after, before);
        return after;
    }

    private UEMLibraryConnection convertLibraryConnection(TaskXMLLibraryConnectionType before) {
        UEMLibraryConnection after = new UEMLibraryConnection();
        after.setMasterPort(before.getSrcPort());
        after.setMasterTask(before.getSrcTask());
        after.setSlaveLibrary(before.getDstLibrary());
        return after;
    }

    private void replaceToFlattenedTask(ChannelPortType port, List<UEMPortMap> portMapList) {
        String taskName = port.getTask();
        String portName = port.getPort();
        for (UEMPortMap portMap : portMapList) {
            if (portMap.getNotFlattenedTask().equals(taskName)
                    && portMap.getPort().equals(portName)) {
                port.setTask(portMap.getTask());
            }
        }
    }

    private void convertIndirectPortToDirect(List<UEMTask> taskList, List<UEMChannel> channelList) {
        List<UEMPortMap> portMapList = new ArrayList<>();
        for (UEMTask task : taskList) {
            portMapList.addAll(task.getPortMapList());
        }
        for (UEMChannel channel : channelList) {
            replaceToFlattenedTask(channel.getSrc().get(0), portMapList);
            replaceToFlattenedTask(channel.getDst().get(0), portMapList);
        }
    }

    private void replaceToFlattenedTaskOfLibraryConnnection(UEMLibraryConnection connection,
            List<UEMPortMap> portMapList) {
        String taskName = connection.getMasterTask();
        String portName = connection.getMasterPort();
        for (UEMPortMap portMap : portMapList) {
            if (portMap.getNotFlattenedTask().equals(taskName)
                    && portMap.getPort().equals(portName)) {
                connection.setMasterTask(portMap.getTask());
            }
        }
    }

    private void convertIndirectConnectionToDirect(List<UEMTask> taskList,
            List<UEMLibraryConnection> libraryConnectionList) {
        List<UEMPortMap> portMapList = new ArrayList<>();
        for (UEMTask task : taskList) {
            portMapList.addAll(task.getPortMapList());
        }
        for (UEMLibraryConnection connection : libraryConnectionList) {
            replaceToFlattenedTaskOfLibraryConnnection(connection, portMapList);
        }
    }

    public UEMTaskGraph convertTaskXMLtoAlgorithm(int level, String parentTasks, Path taskxmlPath) {
        try {
            UEMTaskGraph uemTaskGraph = new UEMTaskGraph();
            uemTaskGraph.setLevel(level);
            taskXMLHandler.loadXMLfileToHandler(taskxmlPath.toString());
            TaskXMLTaskGraphType taskgraph = taskXMLHandler.getTaskGraph();
            List<UEMTask> taskList = new ArrayList<>();
            uemTaskGraph.setTaskList(taskList);
            for (TaskXMLTaskType task : taskgraph.getTaskList().getTask()) {
                taskList.add(convertTask(parentTasks, task));
            }
            List<UEMChannel> channelList = new ArrayList<>();
            uemTaskGraph.setChannelList(channelList);
            for (TaskXMLChannelType channel : taskgraph.getChannelList().getChannel()) {
                channelList.add(convertChannel(channel));
            }
            convertIndirectPortToDirect(taskList, channelList);
            List<UEMLibrary> libraryList = new ArrayList<>();
            uemTaskGraph.setLibraryList(libraryList);
            for (TaskXMLLibraryType library : taskgraph.getLibraryList().getLibrary()) {
                libraryList.add(convertLibrary(parentTasks, library));
            }
            List<UEMLibraryConnection> libraryConnectionList = new ArrayList<>();
            uemTaskGraph.setLibraryConnectionList(libraryConnectionList);
            for (TaskXMLLibraryConnectionType connection : taskgraph.getTaskLibraryConnectionList()
                    .getTaskLibraryConnection()) {
                libraryConnectionList.add(convertLibraryConnection(connection));
            }
            convertIndirectConnectionToDirect(taskList, libraryConnectionList);
            return uemTaskGraph;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}

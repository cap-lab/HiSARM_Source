package com.metadata.algorithm.library;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.dbmanager.datastructure.variable.PrimitiveType;
import com.metadata.constant.AlgorithmConstant;
import com.scriptparser.parserdatastructure.util.KeyValue;
import com.scriptparser.parserdatastructure.wrapper.GroupWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.strategy.strategydatastructure.wrapper.GroupingAlgorithmWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import hopes.cic.xml.YesNoType;

public class UEMGroupingLibrary extends UEMLibrary {
    private Map<String, KeyValue<String, List<GroupWrapper>>> groupMap = new HashMap<>();
    private int sharedDataSize;
    private int robotInfoSize = 8;

    public UEMGroupingLibrary(RobotImplWrapper robot, GroupingAlgorithmWrapper groupingAlgorithm) {
        super(robot.getRobot().getRobotId());
        setName(robot.getRobot().getRobotId(), AlgorithmConstant.GROUPING);
        setDefaultFunction();
        setCflags("");
        setLdflags("");
        setFile(robot.getRobot().getRobotId() + "_" + AlgorithmConstant.GROUPING
                + AlgorithmConstant.LIBRARY_FILE_EXTENSION);
        setHeader(robot.getRobot().getRobotId() + "_" + AlgorithmConstant.GROUPING
                + AlgorithmConstant.LIBRARY_HEADER_EXTENSION);
        setIsHardwareDependent(YesNoType.NO);
        setLanguage(AlgorithmConstant.LANGUAGE_CPP);
        setSharedDataSize(robot.getGroupingAlgorithm().getGroupingAlgorithm().getSharedDataSize());
        getExtraHeader().add(AlgorithmConstant.COMMON_GROUP_HEADER);
        getExtraHeader()
                .add(robot.getRobot().getRobotId() + AlgorithmConstant.ROBOT_GROUP_HEADER_SUFFIX);
        getExtraHeader()
                .add(robot.getRobot().getRobotId() + AlgorithmConstant.ROBOT_MODE_HEADER_SUFFIX);
        getExtraHeader().add(AlgorithmConstant.MUTEX_HEADER);
        sharedDataSize = groupingAlgorithm.getGroupingAlgorithm().getSharedDataSize();
    }

    public int getPacketSize(int robotNum) {
        return sharedDataSize * robotNum + robotInfoSize * robotNum + PrimitiveType.INT32.getSize();
    }

    private void setDefaultFunction() {
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupCandidateList());
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupCandidateNum());
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupInfo());
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupSharedDataFromGrouping());
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupSharedDataFromReport());
        getFunction().add(UEMGroupingLibraryFunction.makeAvailSharedDataFromReport());
        getFunction().add(UEMGroupingLibraryFunction.makeSetGroupSelectionState());
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupSelectionState());
        getFunction().add(UEMGroupingLibraryFunction.makeSetGroupSharedDataFromGrouping());
        getFunction().add(UEMGroupingLibraryFunction.makeSetGroupSharedDataFromListen());
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupSharedRobotNum());
    }

    public void addGroupMap(String modeName, String groupName, List<GroupWrapper> groupList) {
        KeyValue<String, List<GroupWrapper>> kv =
                new KeyValue<String, List<GroupWrapper>>(groupName, groupList);
        groupMap.put(modeName, kv);
    }

    private void setSharedDataSize(Integer size) {
        this.sharedDataSize = size;
    }

    public int getSharedDataSize() {
        return sharedDataSize;
    }

    public Set<String> getModeSet() {
        return groupMap.keySet();
    }

    public List<GroupWrapper> getGroupList(String modeName) throws Exception {
        if (groupMap.containsKey(modeName)) {
            return groupMap.get(modeName).value;
        } else {
            throw new Exception("There's no mode which name is " + modeName);
        }
    }

    public String makeGroupId(String modeName, String groupName) {
        return ModeWrapper.makeGroupId(groupMap.get(modeName).key, groupName);
    }
}

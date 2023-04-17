package com.metadata.algorithm.library;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.metadata.constant.AlgorithmConstant;
import com.scriptparser.parserdatastructure.wrapper.GroupWrapper;
import com.strategy.strategydatastructure.wrapper.GroupingAlgorithmWrapper;
import hopes.cic.xml.YesNoType;

public class UEMGroupingLibrary extends UEMLibrary {
    private Map<String, Map<String, List<GroupWrapper>>> groupMap = new HashMap<>();
    private int sharedDataSize;

    public UEMGroupingLibrary(String robotId, GroupingAlgorithmWrapper groupingAlgorithm) {
        super(robotId);
        setName(robotId, AlgorithmConstant.GROUPING);
        setDefaultFunction();
        setCflags("");
        setLdflags("");
        setFile(robotId + "_" + AlgorithmConstant.GROUPING
                + AlgorithmConstant.LIBRARY_FILE_EXTENSION);
        setHeader(robotId + "_" + AlgorithmConstant.GROUPING
                + AlgorithmConstant.LIBRARY_HEADER_EXTENSION);
        setIsHardwareDependent(YesNoType.NO);
        setLanguage(AlgorithmConstant.LANGUAGE_CPP);
        getExtraHeader().add(AlgorithmConstant.COMMON_GROUP_HEADER);
        getExtraHeader().add(AlgorithmConstant.MUTEX_HEADER);
        sharedDataSize = groupingAlgorithm.getGroupingAlgorithm().getSharedDataSize();
    }

    private void setDefaultFunction() {
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupCandidateList());
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupCandidateNum());
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupInfo());
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupSharedDataFromGrouping());
        getFunction().add(UEMGroupingLibraryFunction.makeGetGroupSharedDataFromReport());
        getFunction().add(UEMGroupingLibraryFunction.makeSetGroupSelectionState());
        getFunction().add(UEMGroupingLibraryFunction.makeSetGroupSharedDataFromGrouping());
        getFunction().add(UEMGroupingLibraryFunction.makeSetGroupSharedDataFromListen());
    }

    public Map<String, Map<String, List<GroupWrapper>>> getGroupMap() {
        return groupMap;
    }

    public void addGroupMap(String modeName, String groupPrefix, List<GroupWrapper> groupList) {
        groupMap.put(modeName, new HashMap<>());
        groupMap.get(modeName).put(groupPrefix, groupList);
    }

    public int getSharedDataSize() {
        return sharedDataSize;
    }
}

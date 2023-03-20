package com.metadata.algorithm.task;

import java.util.ArrayList;
import java.util.List;
import com.metadata.algorithm.library.UEMLibrary;
import com.metadata.algorithm.library.UEMLibraryFunction;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.YesNoType;

public class UEMGroupActionTask extends UEMLibrary {
    private List<UEMActionTask> groupActionList = new ArrayList<>();

    public UEMGroupActionTask(UEMRobotTask robot) {
        super(robot.getRobot().getRobot().getRobotId());
        setName(AlgorithmConstant.GROUP_ACTION);
        setType(getName());
        setCflags("");
        setLdflags("");
        setFile(robot.getName() + "_" + AlgorithmConstant.GROUP_ACTION
                + AlgorithmConstant.LIBRARY_FILE_EXTENSION);
        setHeader(robot.getName() + "_" + AlgorithmConstant.GROUP_ACTION
                + AlgorithmConstant.LIBRARY_HEADER_EXTENSION);
        setIsHardwareDependent(YesNoType.NO);
        setLanguage(AlgorithmConstant.LANGUAGE_C);
        getExtraHeader().add(AlgorithmConstant.COMMON_LEADER_HEADER);
        getExtraHeader().add(AlgorithmConstant.MUTEX_HEADER);
        setFunctionList(robot);
    }

    private void setFunctionList(UEMRobotTask robot) {
        getFunction().add(UEMLibraryFunction.makeSetFuncFromListenForGroupAction());
        getFunction().add(UEMLibraryFunction.makeGetFuncFromControlForGroupAction());
        getFunction().add(UEMLibraryFunction.makeSetFuncFromControlForGroupAction());
        getFunction().add(UEMLibraryFunction.makeSetRobotIdFuncFromControlForGroupAction());
        getFunction().add(UEMLibraryFunction.makeAvailFuncFromReportForGroupAction());
    }

    public List<UEMActionTask> getGroupActionList() {
        return groupActionList;
    }

    public boolean existGroupAction(UEMActionTask groupAction) {
        return groupActionList.stream()
                .anyMatch(a -> a.getGroupActionIndex() == groupAction.getGroupActionIndex());
    }
}

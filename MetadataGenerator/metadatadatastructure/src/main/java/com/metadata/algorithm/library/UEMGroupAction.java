package com.metadata.algorithm.library;

import java.util.ArrayList;
import java.util.List;
import com.metadata.algorithm.task.UEMActionTask;
import com.metadata.algorithm.task.UEMRobotTask;
import com.metadata.constant.AlgorithmConstant;
import hopes.cic.xml.YesNoType;

public class UEMGroupAction extends UEMLibrary {
    private List<UEMActionTask> groupActionList = new ArrayList<>();

    public UEMGroupAction(UEMRobotTask robot) {
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
        getExtraHeader().add(AlgorithmConstant.SEMO + AlgorithmConstant.LEADER_HEADER_SUFFIX);
        getExtraHeader().add(AlgorithmConstant.MUTEX_HEADER);
        setFunctionList();
    }

    private void setFunctionList() {
        getFunction().add(UEMGroupActionLibraryFunction.makeSetFuncFromListenForGroupAction());
        getFunction().add(UEMGroupActionLibraryFunction.makeGetFuncFromControlForGroupAction());
        getFunction().add(UEMGroupActionLibraryFunction.makeSetFuncFromControlForGroupAction());
        getFunction()
                .add(UEMGroupActionLibraryFunction.makeSetRobotIdFuncFromControlForGroupAction());
        getFunction().add(UEMGroupActionLibraryFunction.makeAvailFuncFromReportForGroupAction());
    }

    public List<UEMActionTask> getGroupActionList() {
        return groupActionList;
    }

    public boolean existGroupAction(UEMActionTask groupAction) {
        return groupActionList.stream()
                .anyMatch(a -> a.getGroupActionIndex() == groupAction.getGroupActionIndex());
    }
}

package com.strategy.strategymaker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.dbmanager.commonlibraries.DBService;
import com.strategy.strategydatastructure.wrapper.ActionImplWrapper;
import com.strategy.strategydatastructure.wrapper.ControlStrategyWrapper;
import com.strategy.strategydatastructure.wrapper.ResourceWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;

public class ResourceInfoMaker {
    public static void makeResourceInfoList(List<RobotImplWrapper> robotList) {
        try {
            for (RobotImplWrapper robot : robotList) {
                Set<String> resourceSet = new HashSet<>();
                for (ControlStrategyWrapper cs : robot.getControlStrategyList()) {
                    for (ActionImplWrapper action : cs.getActionList()) {
                        for (String resourceId : action.getActionImpl().getResource()) {
                            if (!resourceSet.contains(resourceId)) {
                                ResourceWrapper resource = new ResourceWrapper();
                                resource.setResource(DBService.getResource(
                                        robot.getRobotType().getRobotType().getRobotClass(),
                                        resourceId));
                                robot.getResourceList().add(resource);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

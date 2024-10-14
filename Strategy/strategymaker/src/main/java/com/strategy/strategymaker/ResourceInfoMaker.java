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

    private static ResourceWrapper setResourceToResourceWrapper(RobotImplWrapper robot,
            String resourceId) {
        ResourceWrapper resource = new ResourceWrapper();
        resource.setResource(DBService
                .getResource(robot.getRobotType().getRobotType().getRobotClass(), resourceId));
        resource.setTask(DBService.getTask(resource.getResource().getTaskId()));
        return resource;
    }

    private static void addResourceToRobot(RobotImplWrapper robot, Set<String> resourceSet,
            String resourceId) {
        if (!resourceSet.contains(resourceId)) {
            resourceSet.add(resourceId);
            ResourceWrapper resource = setResourceToResourceWrapper(robot, resourceId);
            robot.getResourceList().add(resource);
            if (!resource.getResource().getRequiredResources().isEmpty()) {
                for (String requiredResourceId : resource.getResource().getRequiredResources()) {
                    addResourceToRobot(robot, resourceSet, requiredResourceId);
                }
            }
        }
    }

    public static void makeResourceInfoList(List<RobotImplWrapper> robotList) {
        try {
            for (RobotImplWrapper robot : robotList) {
                Set<String> resourceSet = new HashSet<>();
                for (ControlStrategyWrapper cs : robot.getControlStrategyList()) {
                    for (ActionImplWrapper action : cs.getActionList()) {
                        for (String resourceId : action.getActionImpl().getResource()) {
                            addResourceToRobot(robot, resourceSet, resourceId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

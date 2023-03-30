package com.codegenerator.generator.mapper;

import com.codegenerator.wrapper.CodeActionWrapper;
import com.codegenerator.wrapper.CodeResourceWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.metadata.algorithm.task.UEMResourceTask;

public class RobotResourceMapper {
    private void mapActionToResource(CodeRobotWrapper robot, CodeResourceWrapper resource) {
        for (CodeServiceWrapper service : robot.getServiceList()) {
            for (CodeActionWrapper action : service.getActionList()) {
                for (String actionResource : action.getActionTask().getActionImpl().getActionImpl()
                        .getResource()) {
                    if (resource.getResourceId().equals(actionResource)) {
                        if (!resource.isExistAction(action.getActionTask().getActionName())) {
                            resource.getRelatedActionList().add(action);
                        }
                    }
                }
            }
        }
    }

    public void mapResource(CodeRobotWrapper robot) {
        for (UEMResourceTask resourceTask : robot.getRobot().getRobotTask().getResourceTaskList()) {
            CodeResourceWrapper resource = new CodeResourceWrapper();
            resource.setResource(resourceTask);
            mapActionToResource(robot, resource);
            robot.getResourceList().add(resource);
        }
    }
}

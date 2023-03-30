package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.codegenerator.generator.util.LocalFileCopier;
import com.codegenerator.wrapper.CodeActionWrapper;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeResourceWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.dbmanager.datastructure.task.TaskFile;

public class TaskCodeCopier {
    private void copyTaskCode(TaskFile file, String fileName, Path componentPrefix, Path targetDir)
            throws Exception {
        if (file.isDirectory()) {
            LocalFileCopier.copyDirectory(Paths.get(componentPrefix.toString(), file.getPath()),
                    targetDir);
        } else {
            LocalFileCopier.copyFile(Paths.get(componentPrefix.toString(), file.getPath()),
                    Paths.get(targetDir.toString(), fileName));
        }
    }

    public void copyActionTaskCode(Path componentPrefix, Path targetDir,
            List<CodeRobotWrapper> codeRobotList) {
        try {
            for (CodeRobotWrapper codeRobot : codeRobotList) {
                for (CodeModeWrapper codeMode : codeRobot.getModeList()) {
                    for (CodeServiceWrapper service : codeMode.getServiceMap().keySet()) {
                        for (CodeActionWrapper action : service.getActionList()) {
                            for (TaskFile file : action.getActionTask().getActionImpl().getTask()
                                    .getTaskFiles()) {
                                copyTaskCode(file, action.getActionTask().getFile(),
                                        componentPrefix, targetDir);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyResourceTaskCode(Path componentPrefix, Path targetDir,
            List<CodeRobotWrapper> codeRobotList) {
        try {
            for (CodeRobotWrapper codeRobot : codeRobotList) {
                for (CodeResourceWrapper resourceWrapper : codeRobot.getResourceList()) {
                    for (TaskFile file : resourceWrapper.getResource().getResource().getTask()
                            .getTaskFiles()) {
                        copyTaskCode(file, resourceWrapper.getResource().getFile(), componentPrefix,
                                targetDir);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

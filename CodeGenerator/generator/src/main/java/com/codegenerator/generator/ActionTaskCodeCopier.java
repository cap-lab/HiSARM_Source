package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.codegenerator.generator.util.LocalFileCopier;
import com.codegenerator.wrapper.CodeActionWrapper;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.dbmanager.datastructure.task.TaskFile;

public class ActionTaskCodeCopier {
    public void copyActionTaskCode(Path componentPrefix, Path targetDir,
            List<CodeRobotWrapper> codeRobotList) {
        try {
            for (CodeRobotWrapper codeRobot : codeRobotList) {
                for (CodeModeWrapper codeMode : codeRobot.getModeList()) {
                    for (CodeServiceWrapper service : codeMode.getServiceMap().keySet()) {
                        for (CodeActionWrapper action : service.getActionList()) {
                            for (TaskFile file : action.getActionTask().getActionImpl().getTask()
                                    .getTaskFiles()) {
                                if (file.isDirectory()) {
                                    LocalFileCopier.copyDirectory(
                                            Paths.get(componentPrefix.toString(), file.getPath()),
                                            targetDir);
                                } else {
                                    LocalFileCopier.copyFile(
                                            Paths.get(componentPrefix.toString(), file.getPath()),
                                            Paths.get(targetDir.toString(),
                                                    action.getActionTask().getFile()));
                                }
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

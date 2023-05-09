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
import com.dbmanager.datastructure.common.FileItem;
import com.metadata.algorithm.task.UEMGroupingTask;
import com.metadata.algorithm.task.UEMLeaderTask;

public class TaskCodeCopier {
    private void copyTaskCode(FileItem file, Path componentPrefix, Path targetDir)
            throws Exception {
        if (file.isDirectory()) {
            LocalFileCopier.copyDirectory(Paths.get(componentPrefix.toString(), file.getPath()),
                    targetDir);
        } else {
            LocalFileCopier.copyFile(Paths.get(componentPrefix.toString(), file.getPath()), Paths
                    .get(targetDir.toString(), Paths.get(file.getPath()).getFileName().toString()));
        }
    }

    public void copyActionTaskCode(Path componentPrefix, Path targetDir,
            List<CodeRobotWrapper> codeRobotList) {
        try {
            for (CodeRobotWrapper codeRobot : codeRobotList) {
                for (CodeModeWrapper codeMode : codeRobot.getModeList()) {
                    for (CodeServiceWrapper service : codeMode.getServiceMap().keySet()) {
                        for (CodeActionWrapper action : service.getActionList()) {
                            for (FileItem file : action.getActionTask().getActionImpl().getTask()
                                    .getTaskFiles()) {
                                copyTaskCode(file, componentPrefix, targetDir);
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
                    for (FileItem file : resourceWrapper.getResource().getResource().getTask()
                            .getTaskFiles()) {
                        copyTaskCode(file, componentPrefix, targetDir);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyLeaderSourceFile(Path componentPrefix, Path targetDir,
            List<CodeRobotWrapper> codeRobotList) {
        try {
            for (CodeRobotWrapper codeRobot : codeRobotList) {
                UEMLeaderTask leaderTask = codeRobot.getRobot().getRobotTask().getLeaderTask();
                for (FileItem file : leaderTask.getLeaderTask().getTaskFiles()) {
                    copyTaskCode(file, componentPrefix, targetDir);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyGroupingTaskCode(Path componentPrefix, Path targetDir,
            List<CodeRobotWrapper> codeRobotList) {
        try {
            for (CodeRobotWrapper codeRobot : codeRobotList) {
                UEMGroupingTask groupingTask =
                        codeRobot.getRobot().getRobotTask().getGroupingTask();
                for (FileItem file : groupingTask.getGroupingTask().getTaskFiles()) {
                    copyTaskCode(file, componentPrefix, targetDir);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

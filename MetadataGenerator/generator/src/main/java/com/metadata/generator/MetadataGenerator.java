package com.metadata.generator;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.metadata.generator.constant.MetadataConstant;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.wrapper.StrategyWrapper;
import com.strategy.strategymaker.additionalinfo.AdditionalInfo;

public class MetadataGenerator {
    private String makeProjectDirName(String projectName) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_");
        return formatter.format(new Date()) + projectName;
    }

    private boolean makeProjectDirectory(Path projectDirPath) {
        File projectDir = projectDirPath.toFile();
        return projectDir.mkdirs();
    }

    public void metadataGenerator(MissionWrapper mission, StrategyWrapper strategy,
            AdditionalInfo additionalInfo, String targetDirectory) {
        String projectName = additionalInfo.getProjectName();
        Path projectDir = null;
        if (targetDirectory == null) {
            projectDir = Paths.get(MetadataConstant.GENERATE_DIRECTORY.toString(),
                    makeProjectDirName(projectName));
        } else {
            projectDir = Paths.get(targetDirectory);
        }
        makeProjectDirectory(projectDir);
        ConfigurationGenerator.generateConfiguration(projectDir, projectName);
    }
}

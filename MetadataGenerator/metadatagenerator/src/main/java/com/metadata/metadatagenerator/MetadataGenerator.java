package com.metadata.metadatagenerator;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.metadata.UEMRobot;
import com.metadata.algorithm.UEMAlgorithm;
import com.metadata.metadatagenerator.algorithm.AlgorithmGenerator;
import com.metadata.metadatagenerator.architecture.ArchitectureGenerator;
import com.metadata.metadatagenerator.constant.MetadataConstant;
import com.metadata.metadatagenerator.mapping.MappingGenerator;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import com.strategy.strategydatastructure.wrapper.StrategyWrapper;

public class MetadataGenerator {
    private UEMAlgorithm uemAlgorithm;

    private String makeProjectDirName(String projectName) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_");
        return formatter.format(new Date()) + projectName;
    }

    private boolean makeProjectDirectory(Path projectDirPath) {
        File projectDir = projectDirPath.toFile();
        return projectDir.mkdirs();
    }

    public List<UEMRobot> metadataGenerate(MissionWrapper mission, StrategyWrapper strategy,
            AdditionalInfo additionalInfo, Path projectDir) {
        String projectName = additionalInfo.getProjectName();
        if (projectDir == null) {
            projectDir = Paths.get(MetadataConstant.GENERATE_DIRECTORY.toString(),
                    makeProjectDirName(projectName));
        }
        makeProjectDirectory(projectDir);
        ConfigurationGenerator.generateConfiguration(projectDir, projectName);

        AlgorithmGenerator algorithm = new AlgorithmGenerator();
        algorithm.generate(mission, strategy, additionalInfo, projectDir);
        algorithm.generateAlgorithmXML(projectDir, projectName);
        uemAlgorithm = algorithm.getAlgorithm();

        ArchitectureGenerator architecture = new ArchitectureGenerator();
        List<UEMRobot> robotList =
                architecture.generate(mission, additionalInfo, algorithm.getAlgorithm());
        architecture.generateArchitectureXML(projectDir, projectName);

        MappingGenerator mapping = new MappingGenerator();
        mapping.generate(robotList, algorithm.getAlgorithm());
        mapping.generateMappingXML(projectDir, projectName);

        return robotList;
    }

    public UEMAlgorithm getUemAlgorithm() {
        return uemAlgorithm;
    }

}

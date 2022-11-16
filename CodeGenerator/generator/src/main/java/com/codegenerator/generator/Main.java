package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.metadata.UEMRobot;
import com.metadata.metadatagenerator.MetadataGenerator;
import com.scriptparser.parser.Parser;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.wrapper.StrategyWrapper;
import com.strategy.strategymaker.StrategyMaker;

public class Main {
    private static Path makeProjectDirName(String projectName) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_");
        return Paths.get(CodeGeneratorConstant.GENERATE_DIRECTORY.toString(),
                formatter.format(new Date()) + projectName);
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
        MissionWrapper mission = parser.parseScript(args[0]);
        StrategyMaker strategyMaker = new StrategyMaker();
        StrategyWrapper strategy = strategyMaker.strategyMake(mission, args[1]);
        MetadataGenerator generator = new MetadataGenerator();
        Path projectDir = makeProjectDirName(strategy.getAdditionalInfo().getProjectName());
        List<UEMRobot> robotList = generator.metadataGenerate(mission, strategy,
                strategy.getAdditionalInfo(), projectDir);
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.codeGenerate(projectDir, mission, strategy.getAdditionalInfo(), robotList);
    }
}

package com.metadata.metadatagenerator;

import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.metadata.constant.ConfigurationConstant;
import com.metadata.metadatagenerator.constant.MetadataConstant;
import hopes.cic.exception.CICXMLException;
import hopes.cic.xml.CICConfigurationType;
import hopes.cic.xml.CodeGenerationType;
import hopes.cic.xml.SimulationType;
import hopes.cic.xml.TimeMetricType;
import hopes.cic.xml.TimeType;
import hopes.cic.xml.handler.CICConfigurationXMLHandler;

public class ConfigurationGenerator {
    public static boolean generateConfiguration(Path rootDirectory, String projectName) {
        try {
            Path filePath = Paths.get(rootDirectory.toString(),
                    projectName + MetadataConstant.CONFIGURATION_SUFFIX);
            CICConfigurationXMLHandler handler = new CICConfigurationXMLHandler();
            CICConfigurationType configuration = handler.getConfiguration();

            CodeGenerationType codeGeneration = new CodeGenerationType();
            codeGeneration.setRuntimeExecutionPolicy(ConfigurationConstant.RUN_TIME_EXECUTION); 
            codeGeneration.setThreadOrFunctioncall(ConfigurationConstant.THREAD);
            configuration.setCodeGeneration(codeGeneration);

            SimulationType simulation = new SimulationType();
            TimeType time = new TimeType();
            time.setValue(BigInteger.valueOf(1000000000));
            time.setMetric(TimeMetricType.MS);
            simulation.setExecutionTime(time);
            configuration.setSimulation(simulation);

            handler.storeXMLString(filePath.toString());
            return true;
        } catch (CICXMLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

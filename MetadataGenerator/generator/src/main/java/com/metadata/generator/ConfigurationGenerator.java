package com.metadata.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import com.metadata.generator.constant.MetadataConstant;
import hopes.cic.exception.CICXMLException;
import hopes.cic.xml.handler.CICConfigurationXMLHandler;

public class ConfigurationGenerator {
    public static boolean generateConfiguration(Path rootDirectory, String projectName) {
        try {
            Path templatePath = Paths.get(MetadataConstant.RESOURCE_DIRECTORY.toString(),
                    MetadataConstant.CONFIGURATION_XML);
            Path filePath = Paths.get(rootDirectory.toString(),
                    projectName + MetadataConstant.CONFIGURATION_SUFFIX);
            CICConfigurationXMLHandler handler = new CICConfigurationXMLHandler();
            handler.loadXMLfileToHandler(templatePath.toAbsolutePath().toString());
            handler.storeXMLString(filePath.toString());
            return true;
        } catch (CICXMLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

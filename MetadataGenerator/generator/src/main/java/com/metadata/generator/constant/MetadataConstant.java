package com.metadata.generator.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MetadataConstant {
        public static final Path PROJECT_DIRECTORY = Paths.get("");
        public static final Path RESOURCE_DIRECTORY =
                        Paths.get(PROJECT_DIRECTORY.toString(), "resources");
        public static final Path GENERATE_DIRECTORY =
                        Paths.get(PROJECT_DIRECTORY.toString(), "generates");
        public static final String CONFIGURATION_XML = "configuration.xml";
        public static final String CONFIGURATION_SUFFIX = "_" + CONFIGURATION_XML;
}

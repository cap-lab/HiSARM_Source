package com.metadata.metadatagenerator.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MetadataConstant {
        public static final Path PROJECT_DIRECTORY = Paths.get("");
        public static final Path RESOURCE_DIRECTORY =
                        Paths.get(PROJECT_DIRECTORY.toString(), "src", "resources");
        public static final Path GENERATE_DIRECTORY =
                        Paths.get(PROJECT_DIRECTORY.toString(), "generated");

        public static final String CONFIGURATION_XML = "configuration.xml";
        public static final String CONFIGURATION_SUFFIX = "_" + CONFIGURATION_XML;
        public static final String ALGORITHM_XML = "algorithm.xml";
        public static final String ALGORITHM_SUFFIX = "_" + ALGORITHM_XML;
        public static final String ARCHITECTURE_XML = "architecture.xml";
        public static final String ARCHITECTURE_SUFFIX = "_" + ARCHITECTURE_XML;
        public static final String MAPPING_XML = "mapping.xml";
        public static final String MAPPING_SUFFIX = "_" + MAPPING_XML;

        public static final String LINUX = "linux";
}

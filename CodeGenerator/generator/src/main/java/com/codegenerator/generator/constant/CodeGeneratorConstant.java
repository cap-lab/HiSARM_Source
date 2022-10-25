package com.codegenerator.generator.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeGeneratorConstant {
    public static final Path PROJECT_DIRECTORY = Paths.get("");
    public static final Path RESOURCE_DIRECTORY =
            Paths.get(PROJECT_DIRECTORY.toString(), "src", "resources");
    public static final Path TEMPLATE_DIRECTORY =
            Paths.get(RESOURCE_DIRECTORY.toString(), "templates");
    public static final Path GENERATE_DIRECTORY =
            Paths.get(PROJECT_DIRECTORY.toString(), "generated");
}

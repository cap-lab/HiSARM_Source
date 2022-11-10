package com.codegenerator.generator.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeGeneratorConstant {
        public static final Path PROJECT_DIRECTORY = Paths.get("");
        public static final Path RESOURCE_DIRECTORY =
                        Paths.get(PROJECT_DIRECTORY.toString(), "src", "resources");
        public static final Path TEMPLATE_DIRECTORY =
                        Paths.get(RESOURCE_DIRECTORY.toString(), "templates");

        public static final Path COMMON_TEMPLATE_DIRECTORY =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "common");
        public static final Path ROBOT_SPECIFIC_COMMON_TEMPLATE = Paths
                        .get(COMMON_TEMPLATE_DIRECTORY.toString(), "robot_specific_common.ftl");
        public static final Path VARIABLE_HEADER_TEMPLATE =
                        Paths.get(COMMON_TEMPLATE_DIRECTORY.toString(), "variable_header.ftl");
        public static final Path VARIABLE_SOURCE_TEMPLATE =
                        Paths.get(COMMON_TEMPLATE_DIRECTORY.toString(), "variable_source.ftl");


        public static final Path COMMUNICATION_TEMPLATE_DIRECTORY =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "communication");
        public static final Path LISTEN_TASK_TEMPLATE =
                        Paths.get(COMMUNICATION_TEMPLATE_DIRECTORY.toString(), "listentask.ftl");
        public static final Path REPORT_TASK_TEMPLATE =
                        Paths.get(COMMUNICATION_TEMPLATE_DIRECTORY.toString(), "reporttask.ftl");

        public static final Path SHARED_DATA_TEMPLATE_DIRECTORY =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "shareddata");
        public static final Path SHARED_DATA_HEADER_TEMPLATE =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "shared_data_header.ftl");
        public static final Path SHARED_DATA_SOURCE_TEMPLATE =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "shared_data_source.ftl");

        public static final Path LEADER_TEMPLATE_DIRECTORY =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "leader");
        public static final Path LEADER_HEADER_TEMPLATE =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "leader_header.ftl");
        public static final Path LEADER_SOURCE_TEMPLATE =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "leader_source.ftl");

        public static final Path GROUP_TEMPLATE_DIRECTORY =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "group");
        public static final Path GROUP_HEADER_TEMPLATE =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "group_header.ftl");
        public static final Path GROUP_SOURCE_TEMPLATE =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "group_source.ftl");

        public static final Path CONTROL_TEMPLATE_DIRECTORY =
                        Paths.get(TEMPLATE_DIRECTORY.toString(), "control");
        public static final Path CONTROL_TASK_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "controltask.ftl");

        public static final Path GENERATE_DIRECTORY =
                        Paths.get(PROJECT_DIRECTORY.toString(), "generated");

        public static final String COMMON_HEADER_SUFFIX = "_common.h";
        public static final String VARIABLE_HEADER_SUFFIX = "_variable.h";
        public static final String VARIABLE_SOURCE_SUFFIX = "_variable.c";
        public static final String GROUP_HEADER_SUFFIX = "_group.h";
        public static final String GROUP_SOURCE_SUFFIX = "_group.c";
}

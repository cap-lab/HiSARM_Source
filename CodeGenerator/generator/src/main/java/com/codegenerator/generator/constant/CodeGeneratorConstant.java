package com.codegenerator.generator.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeGeneratorConstant {
        public static final String ID_TEAM = "ID_TEAM_";
        public static final String JAR_EXTENSION = ".jar";
        public static final String JARS = "jars";
        public static final String BIN = "bin";

        public static final String COMMON_HEADER_SUFFIX = "_common.h";
        public static final String VARIABLE_HEADER_SUFFIX = "_variable.h";
        public static final String VARIABLE_SOURCE_SUFFIX = "_variable.c";
        public static final String GROUP_HEADER_SUFFIX = "_group.h";
        public static final String GROUP_SOURCE_SUFFIX = "_group.c";
        public static final String TEAM_HEADER_SUFFIX = "_team.h";
        public static final String EVENT_HEADER_SUFFIX = "_event.h";
        public static final String EVENT_SOURCE_SUFFIX = "_event.c";
        public static final String PORT_HEADER_SUFFIX = "_port.h";
        public static final String PORT_SOURCE_SUFFIX = "_port.c";
        public static final String RESOURCE_HEADER_SUFFIX = "_resource.h";
        public static final String RESOURCE_SOURCE_SUFFIX = "_resource.c";
        public static final String ACTION_HEADER_SUFFIX = "_action.h";
        public static final String ACTION_SOURCE_SUFFIX = "_action.c";
        public static final String TIMER_HEADER_SUFFIX = "_timer.h";
        public static final String TIMER_SOURCE_SUFFIX = "_timer.c";
        public static final String SERVICE_HEADER_SUFFIX = "_service.h";
        public static final String SERVICE_SOURCE_SUFFIX = "_service.c";
        public static final String MODE_HEADER_SUFFIX = "_mode.h";
        public static final String MODE_SOURCE_SUFFIX = "_mode.c";
        public static final String TRANSITION_HEADER_SUFFIX = "_transition.h";
        public static final String TRANSITION_SOURCE_SUFFIX = "_transition.c";

        public static final String CONTROL_TASK_CIC = "control.cic";
        public static final String LEADER_HEADER = "semo_leader.h";
        public static final String VARIABLE_HEADER = "semo_variable.h";
        public static final String VARIABLE_SOURCE = "semo_variable.c";
        public static final String PORT_HEADER = "semo_port.h";
        public static final String PORT_SOURCE = "semo_port.c";
        public static final String COMMUNICATION_HEADER = "semo_communication.h";
        public static final String COMMUNICATION_SOURCE = "semo_communication.c";
        public static final String COMMON_HEADER = "semo_common.h";
        public static final String LOGGER_HEADER = "semo_logger.h";

        public static final Path PROJECT_DIRECTORY = Paths.get("");
        public static final Path RESOURCE_DIRECTORY =
                        Paths.get(PROJECT_DIRECTORY.toString(), "resources");
        public static final Path INCLUDE_DIRECTORY =
                        Paths.get(RESOURCE_DIRECTORY.toString(), "include");
        public static final Path SOURCE_DIRECTORY = Paths.get(RESOURCE_DIRECTORY.toString(), "src");
        public static final Path TEMPLATE_DIRECTORY =
                        Paths.get(RESOURCE_DIRECTORY.toString(), "templates");
        public static final Path GENERATE_DIRECTORY =
                        Paths.get(PROJECT_DIRECTORY.toString(), "generated");

        public static final Path COMMON_TEMPLATE_DIRECTORY = Paths.get("common");
        public static final Path ROBOT_SPECIFIC_COMMON_TEMPLATE = Paths
                        .get(COMMON_TEMPLATE_DIRECTORY.toString(), "robot_specific_common.ftl");
        public static final Path VARIABLE_HEADER_TEMPLATE =
                        Paths.get(COMMON_TEMPLATE_DIRECTORY.toString(), "variable_header.ftl");
        public static final Path VARIABLE_SOURCE_TEMPLATE =
                        Paths.get(COMMON_TEMPLATE_DIRECTORY.toString(), "variable_source.ftl");
        public static final Path VARIABLE_HEADER_CODE =
                        Paths.get(INCLUDE_DIRECTORY.toString(), VARIABLE_HEADER);
        public static final Path VARIABLE_SOURCE_CODE =
                        Paths.get(SOURCE_DIRECTORY.toString(), VARIABLE_SOURCE);
        public static final Path COMMON_HEADER_CODE =
                        Paths.get(INCLUDE_DIRECTORY.toString(), COMMON_HEADER);
        public static final Path LOGGER_HEADER_CODE =
                        Paths.get(INCLUDE_DIRECTORY.toString(), LOGGER_HEADER);

        public static final Path COMMUNICATION_TEMPLATE_DIRECTORY = Paths.get("communication");
        public static final Path LISTEN_TASK_TEMPLATE =
                        Paths.get(COMMUNICATION_TEMPLATE_DIRECTORY.toString(), "listentask.ftl");
        public static final Path REPORT_TASK_TEMPLATE =
                        Paths.get(COMMUNICATION_TEMPLATE_DIRECTORY.toString(), "reporttask.ftl");
        public static final Path PORT_HEADER_CODE =
                        Paths.get(INCLUDE_DIRECTORY.toString(), PORT_HEADER);
        public static final Path PORT_SOURCE_CODE =
                        Paths.get(SOURCE_DIRECTORY.toString(), PORT_SOURCE);
        public static final Path COMMUNICATION_HEADER_CODE =
                        Paths.get(INCLUDE_DIRECTORY.toString(), COMMUNICATION_HEADER);
        public static final Path COMMUNICATION_SOURCE_CODE =
                        Paths.get(SOURCE_DIRECTORY.toString(), COMMUNICATION_SOURCE);

        public static final Path SHARED_DATA_TEMPLATE_DIRECTORY = Paths.get("shareddata");
        public static final Path SHARED_DATA_HEADER_TEMPLATE = Paths
                        .get(SHARED_DATA_TEMPLATE_DIRECTORY.toString(), "shared_data_header.ftl");
        public static final Path SHARED_DATA_SOURCE_TEMPLATE = Paths
                        .get(SHARED_DATA_TEMPLATE_DIRECTORY.toString(), "shared_data_source.ftl");

        public static final Path LEADER_TEMPLATE_DIRECTORY = Paths.get("leader");
        public static final Path LEADER_HEADER_TEMPLATE =
                        Paths.get(LEADER_TEMPLATE_DIRECTORY.toString(), "leader_header.ftl");
        public static final Path LEADER_SOURCE_TEMPLATE =
                        Paths.get(LEADER_TEMPLATE_DIRECTORY.toString(), "leader_source.ftl");
        public static final Path LEADER_HEADER_CODE =
                        Paths.get(INCLUDE_DIRECTORY.toString(), LEADER_HEADER);
        public static final Path LEADER_SOURCE_CODE = Paths.get("additional_task",
                        LEADER_TEMPLATE_DIRECTORY.toString(), "leader_selection.cic");

        public static final Path GROUP_TEMPLATE_DIRECTORY = Paths.get("group");
        public static final Path GROUP_HEADER_TEMPLATE =
                        Paths.get(GROUP_TEMPLATE_DIRECTORY.toString(), "group_header.ftl");
        public static final Path GROUP_SOURCE_TEMPLATE =
                        Paths.get(GROUP_TEMPLATE_DIRECTORY.toString(), "group_source.ftl");
        public static final Path TEAM_HEADER_TEMPLATE =
                        Paths.get(GROUP_TEMPLATE_DIRECTORY.toString(), "team_header.ftl");

        public static final Path CONTROL_TEMPLATE_DIRECTORY = Paths.get("control");
        public static final Path EVENT_HEADER_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "event_header.ftl");
        public static final Path EVENT_SOURCE_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "event_source.ftl");
        public static final Path PORT_HEADER_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "port_header.ftl");
        public static final Path PORT_SOURCE_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "port_source.ftl");
        public static final Path RESOURCE_HEADER_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "resource_header.ftl");
        public static final Path RESOURCE_SOURCE_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "resource_source.ftl");
        public static final Path ACTION_HEADER_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "action_header.ftl");
        public static final Path ACTION_SOURCE_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "action_source.ftl");
        public static final Path TIMER_HEADER_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "timer_header.ftl");
        public static final Path TIMER_SOURCE_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "timer_source.ftl");
        public static final Path SERVICE_HEADER_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "service_header.ftl");
        public static final Path SERVICE_SOURCE_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "service_source.ftl");
        public static final Path MODE_HEADER_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "mode_header.ftl");
        public static final Path MODE_SOURCE_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "mode_source.ftl");
        public static final Path TRANSITION_HEADER_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "transition_header.ftl");
        public static final Path TRANSITION_SOURCE_TEMPLATE =
                        Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "transition_source.ftl");
        public static final Path CONTROL_TASK_CODE =
                        Paths.get(SOURCE_DIRECTORY.toString(), CONTROL_TASK_CIC);
}

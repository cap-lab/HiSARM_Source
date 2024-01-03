package com.codegenerator.generator.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeGeneratorConstant {
    public static final String ID_TEAM = "ID_TEAM_";
    public static final String JAR_EXTENSION = ".jar";
    public static final String JARS = "jars";
    public static final String BIN = "bin";
	public static final String SEMO = "semo";

    public static final String COMMON_HEADER_SUFFIX = "_common.h";
    public static final String VARIABLE_HEADER_SUFFIX = "_variable.h";
    public static final String VARIABLE_SOURCE_SUFFIX = "_variable.c";
    public static final String GROUP_HEADER_SUFFIX = "_group.h";
    public static final String GROUP_SOURCE_SUFFIX = "_group.c";
	public static final String LEADER_HEADER_SUFFIX = "_leader.h";
	public static final String LEADER_SOURCE_SUFFIX = "_leader.c";
	public static final String LEADER_LIBRARY_SUFFIX = "_leader_lib.cicl";
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
	public static final String LEADER_DATA_HEADER_SUFFIX = "_leader_data.h";
	public static final String LEADER_DATA_SOURCE_SUFFIX = "_leader_data.c";

	public static final String LISTEN_HEADER_SUFFIX = "_listen.h";
	public static final String LISTEN_SOURCE_SUFFIX = "_listen.c";
	public static final String REPORT_HEADER_SUFFIX = "_report.h";
	public static final String REPORT_SOURCE_SUFFIX = "_report.c";

    public static final String CONTROL_TASK_CIC = "control.cic";
    public static final String LEADER_HEADER = "semo_leader.h";
	public static final String LEADER_SOURCE = "semo_leader.c";
	public static final String LEADER_LIBRARY = "semo_leader.cicl";
    public static final String VARIABLE_HEADER = "semo_variable.h";
    public static final String VARIABLE_SOURCE = "semo_variable.c";
    public static final String PORT_HEADER = "semo_port.h";
    public static final String PORT_SOURCE = "semo_port.c";
    public static final String COMMUNICATION_HEADER = "semo_communication.h";
    public static final String COMMUNICATION_SOURCE = "semo_communication.c";
    public static final String COMMON_HEADER = "semo_common.h";
    public static final String LOGGER_HEADER = "semo_logger.h";
	public static final String GROUP_HEADER = "semo_group.h";
	public static final String GROUP_SOURCE = "semo_group.c";
	public static final String EVENT_HEADER = "semo_event.h";
	public static final String EVENT_SOURCE = "semo_event.c";
	public static final String RESOURCE_HEADER = "semo_resource.h";
	public static final String RESOURCE_SOURCE = "semo_resource.c";
	public static final String ACTION_HEADER = "semo_action.h";
	public static final String ACTION_SOURCE = "semo_action.c";
	public static final String TIMER_HEADER = "semo_timer.h";
	public static final String TIMER_SOURCE = "semo_timer.c";
	public static final String SERVICE_HEADER = "semo_service.h";
	public static final String SERVICE_SOURCE = "semo_service.c";
	public static final String MODE_HEADER = "semo_mode.h";
	public static final String MODE_SOURCE = "semo_mode.c";
	public static final String TRANSITION_HEADER = "semo_transition.h";
	public static final String TRANSITION_SOURCE = "semo_transition.c";

	public static final String LISTEN_TASK_CIC = "semo_listen.cic";
	public static final String REPORT_TASK_CIC = "semo_report.cic";

	public static final String SIMULATION_TASK_CICL_NAME = "semo_simulator.cicl";
	public static final String SIMULATION_TASK_CICL_HEADER_NAME = "semo_simulator.cicl.h";
	public static final String SIMULATION_CLIENT_HEADER_NAME = "semo_simulation_client.h";
	public static final String SIMULATION_CLIENT_SOURCE_NAME = "semo_simulation_client.cpp";
	public static final String SIMULATION_HEADER = "semo_simulation.h";

	public static final Path PROJECT_DIRECTORY = Paths.get("generator");
    public static final Path RESOURCE_DIRECTORY =
            Paths.get(PROJECT_DIRECTORY.toString(), "resources");
    public static final Path INCLUDE_DIRECTORY =
            Paths.get(RESOURCE_DIRECTORY.toString(), "include");
    public static final Path SOURCE_DIRECTORY = Paths.get(RESOURCE_DIRECTORY.toString(), "src");
    public static final Path TEMPLATE_DIRECTORY =
            Paths.get(RESOURCE_DIRECTORY.toString(), "templates");
    public static final Path GENERATE_DIRECTORY =
            Paths.get(PROJECT_DIRECTORY.toString(), "generated");

	public static final Path COMMON_INCLUDE_DIRECTORY =
			Paths.get(INCLUDE_DIRECTORY.toString(), "common");
	public static final Path COMMON_HEADER_PATH =
			Paths.get(COMMON_INCLUDE_DIRECTORY.toString(), COMMON_HEADER);
	public static final Path LOGGER_HEADER_PATH =
			Paths.get(COMMON_INCLUDE_DIRECTORY.toString(), LOGGER_HEADER);
	public static final Path VARIABLE_HEADER_PATH =
			Paths.get(COMMON_INCLUDE_DIRECTORY.toString(), VARIABLE_HEADER);

	public static final Path COMMUNICATION_INCLUDE_DIRECTORY =
			Paths.get(INCLUDE_DIRECTORY.toString(), "communication");
	public static final Path PORT_HEADER_PATH =
			Paths.get(COMMUNICATION_INCLUDE_DIRECTORY.toString(), PORT_HEADER);
	public static final Path COMMUNICATION_HEADER_PATH =
			Paths.get(COMMUNICATION_INCLUDE_DIRECTORY.toString(), COMMUNICATION_HEADER);

	public static final Path CONTROL_INCLUDE_DIRECTORY =
			Paths.get(INCLUDE_DIRECTORY.toString(), "control");
	public static final Path EVENT_HEADER_PATH =
			Paths.get(CONTROL_INCLUDE_DIRECTORY.toString(), EVENT_HEADER);
	public static final Path RESOURCE_HEADER_PATH =
			Paths.get(CONTROL_INCLUDE_DIRECTORY.toString(), RESOURCE_HEADER);
	public static final Path ACTION_HEADER_PATH =
			Paths.get(CONTROL_INCLUDE_DIRECTORY.toString(), ACTION_HEADER);
	public static final Path TIMER_HEADER_PATH =
			Paths.get(CONTROL_INCLUDE_DIRECTORY.toString(), TIMER_HEADER);
	public static final Path SERVICE_HEADER_PATH =
			Paths.get(CONTROL_INCLUDE_DIRECTORY.toString(), SERVICE_HEADER);
	public static final Path MODE_HEADER_PATH =
			Paths.get(CONTROL_INCLUDE_DIRECTORY.toString(), MODE_HEADER);
	public static final Path TRANSITION_HEADER_PATH =
			Paths.get(CONTROL_INCLUDE_DIRECTORY.toString(), TRANSITION_HEADER);

	public static final Path GROUP_INCLUDE_DIRECTORY =
			Paths.get(INCLUDE_DIRECTORY.toString(), "group");
	public static final Path GROUP_HEADER_PATH =
			Paths.get(GROUP_INCLUDE_DIRECTORY.toString(), GROUP_HEADER);

	public static final Path LEADER_INCLUDE_DIRECTORY =
			Paths.get(INCLUDE_DIRECTORY.toString(), "leader");
	public static final Path LEADER_HEADER_PATH =
			Paths.get(LEADER_INCLUDE_DIRECTORY.toString(), LEADER_HEADER);

	public static final Path SIMULATION_INCLUDE_PATH =
			Paths.get(INCLUDE_DIRECTORY.toString(), "simulation");
	public static final Path SIMULATION_TASK_CICL_HEADER =
			Paths.get(SIMULATION_INCLUDE_PATH.toString(), SIMULATION_TASK_CICL_HEADER_NAME);
	public static final Path SIMULATION_CLIENT_HEADER =
			Paths.get(SIMULATION_INCLUDE_PATH.toString(), SIMULATION_CLIENT_HEADER_NAME);

	public static final Path COMMON_SOURCE_DIRECTORY =
			Paths.get(SOURCE_DIRECTORY.toString(), "common");
	public static final Path VARIABLE_SOURCE_PATH =
			Paths.get(COMMON_SOURCE_DIRECTORY.toString(), VARIABLE_SOURCE);

	public static final Path COMMUNICATION_SOURCE_DIRECTORY =
			Paths.get(SOURCE_DIRECTORY.toString(), "communication");
	public static final Path LISTEN_TASK_CODE_PATH =
			Paths.get(COMMUNICATION_SOURCE_DIRECTORY.toString(), LISTEN_TASK_CIC);
	public static final Path REPORT_TASK_CODE_PATH =
			Paths.get(COMMUNICATION_SOURCE_DIRECTORY.toString(), REPORT_TASK_CIC);
	public static final Path COMMUNICATION_SOURCE_PATH =
			Paths.get(COMMUNICATION_SOURCE_DIRECTORY.toString(), COMMUNICATION_SOURCE);

	public static final Path CONTROL_SOURCE_DIRECTORY =
			Paths.get(SOURCE_DIRECTORY.toString(), "control");
	public static final Path CONTROL_TASK_CODE_PATH =
			Paths.get(CONTROL_SOURCE_DIRECTORY.toString(), CONTROL_TASK_CIC);
	public static final Path EVENT_SOURCE_PATH =
			Paths.get(CONTROL_SOURCE_DIRECTORY.toString(), EVENT_SOURCE);
	public static final Path RESOURCE_SOURCE_PATH =
			Paths.get(CONTROL_SOURCE_DIRECTORY.toString(), RESOURCE_SOURCE);
	public static final Path ACTION_SOURCE_PATH =
			Paths.get(CONTROL_SOURCE_DIRECTORY.toString(), ACTION_SOURCE);
	public static final Path TIMER_SOURCE_PATH =
			Paths.get(CONTROL_SOURCE_DIRECTORY.toString(), TIMER_SOURCE);
	public static final Path SERVICE_SOURCE_PATH =
			Paths.get(CONTROL_SOURCE_DIRECTORY.toString(), SERVICE_SOURCE);
	public static final Path MODE_SOURCE_PATH =
			Paths.get(CONTROL_SOURCE_DIRECTORY.toString(), MODE_SOURCE);
	public static final Path TRANSITION_SOURCE_PATH =
			Paths.get(CONTROL_SOURCE_DIRECTORY.toString(), TRANSITION_SOURCE);

	public static final Path GROUP_SOURCE_DIRECTORY =
			Paths.get(SOURCE_DIRECTORY.toString(), "group");
	public static final Path GROUP_SOURCE_PATH =
			Paths.get(GROUP_SOURCE_DIRECTORY.toString(), GROUP_SOURCE);

	public static final Path LEADER_SOURCE_DIRECTORY =
			Paths.get(SOURCE_DIRECTORY.toString(), "leader");
	public static final Path LEADER_SOURCE_PATH =
			Paths.get(LEADER_SOURCE_DIRECTORY.toString(), LEADER_SOURCE);
	public static final Path LEADER_LIBRARY_PATH =
			Paths.get(LEADER_SOURCE_DIRECTORY.toString(), LEADER_LIBRARY);

	public static final Path SIMULATION_SOURCE_DIRECTORY =
			Paths.get(SOURCE_DIRECTORY.toString(), "simulation");
	public static final Path SIMULATION_TASK_CICL =
			Paths.get(SIMULATION_SOURCE_DIRECTORY.toString(), SIMULATION_TASK_CICL_NAME);
	public static final Path SIMULATION_CLIENT_SOURCE_PATH =
			Paths.get(SIMULATION_SOURCE_DIRECTORY.toString(), SIMULATION_CLIENT_SOURCE_NAME);

    public static final Path COMMON_TEMPLATE_DIRECTORY = Paths.get("common");
    public static final Path ROBOT_SPECIFIC_COMMON_TEMPLATE =
            Paths.get(COMMON_TEMPLATE_DIRECTORY.toString(), "robot_specific_common.ftl");
    public static final Path VARIABLE_HEADER_TEMPLATE =
            Paths.get(COMMON_TEMPLATE_DIRECTORY.toString(), "variable_header.ftl");
    public static final Path VARIABLE_SOURCE_TEMPLATE =
			Paths.get(COMMON_TEMPLATE_DIRECTORY.toString(), "variable_source.ftl");

    public static final Path COMMUNICATION_TEMPLATE_DIRECTORY = Paths.get("communication");
	public static final Path LISTEN_PORT_HEADER_TEMPLATE =
			Paths.get(COMMUNICATION_TEMPLATE_DIRECTORY.toString(), "listen_port_header.ftl");
	public static final Path LISTEN_PORT_SOURCE_TEMPLATE =
			Paths.get(COMMUNICATION_TEMPLATE_DIRECTORY.toString(), "listen_port_source.ftl");
	public static final Path REPORT_PORT_HEADER_TEMPLATE =
			Paths.get(COMMUNICATION_TEMPLATE_DIRECTORY.toString(), "report_port_header.ftl");
	public static final Path REPORT_PORT_SOURCE_TEMPLATE =
			Paths.get(COMMUNICATION_TEMPLATE_DIRECTORY.toString(), "report_port_source.ftl");

    public static final Path SHARED_DATA_TEMPLATE_DIRECTORY = Paths.get("shareddata");
    public static final Path SHARED_DATA_HEADER_TEMPLATE =
            Paths.get(SHARED_DATA_TEMPLATE_DIRECTORY.toString(), "shared_data_header.ftl");
    public static final Path SHARED_DATA_SOURCE_TEMPLATE =
            Paths.get(SHARED_DATA_TEMPLATE_DIRECTORY.toString(), "shared_data_source.ftl");

    public static final Path LEADER_TEMPLATE_DIRECTORY = Paths.get("leader");
	public static final Path LEADER_DATA_HEADER_TEMPLATE =
			Paths.get(LEADER_TEMPLATE_DIRECTORY.toString(), "leader_data_header.ftl");
	public static final Path LEADER_DATA_SOURCE_TEMPLATE =
			Paths.get(LEADER_TEMPLATE_DIRECTORY.toString(), "leader_data_source.ftl");

    public static final Path GROUP_TEMPLATE_DIRECTORY = Paths.get("group");
    public static final Path GROUP_ACTION_HEADER_TEMPLATE =
            Paths.get(GROUP_TEMPLATE_DIRECTORY.toString(), "group_action_header.ftl");
    public static final Path GROUP_ACTION_SOURCE_TEMPLATE =
			Paths.get(GROUP_TEMPLATE_DIRECTORY.toString(), "group_action_source.ftl");
    public static final Path TEAM_HEADER_TEMPLATE =
            Paths.get(GROUP_TEMPLATE_DIRECTORY.toString(), "team_header.ftl");

    public static final Path GROUPING_HEADER_TEMPLATE =
            Paths.get(GROUP_TEMPLATE_DIRECTORY.toString(), "grouping_header.ftl");
    public static final Path GROUPING_SOURCE_TEMPLATE =
			Paths.get(GROUP_TEMPLATE_DIRECTORY.toString(), "grouping_source.ftl");

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
	public static final Path GROUP_HEADER_TEMPLATE =
			Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "group_header.ftl");
	public static final Path GROUP_SOURCE_TEMPLATE =
			Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "group_source.ftl");
	public static final Path LEADER_HEADER_TEMPLATE =
			Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "leader_header.ftl");
	public static final Path LEADER_SOURCE_TEMPLATE =
			Paths.get(CONTROL_TEMPLATE_DIRECTORY.toString(), "leader_source.ftl");

	public static final Path SIMULATION_TEMPLATE_DIRECTORY = Paths.get("simulation");
	public static final Path SIMULATION_HEADER_TEMPLATE =
			Paths.get(SIMULATION_TEMPLATE_DIRECTORY.toString(), "simulation.ftl");
}

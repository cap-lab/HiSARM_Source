grammar Script;

@header {
package com.scriptparser.grammar.generated;
}

script: LBRACE team_composition+ RBRACE scenario_behavior+ EOF;

team_composition:
	team_name COLON robot (COMMA robot)* SEPERATOR?;

team_name: IDENTIFIER;

robot: robot_type robot_name (LSBRACKET INTEGER RSBRACKET)?;

robot_type: IDENTIFIER;

robot_name: IDENTIFIER;

scenario_behavior:
	service_define
	| mode_define
	| mode_copy
	| mode_transition_define
	| main_define;

service_define:
	service_name (LPAREN parameter_list RPAREN)? LBRACE statement* RBRACE repeat_statement?;

parameter_list: identifier_list;

service_name: IDENTIFIER;

statement:
	throw_statement
	| assign_statement
	| if_statement
	| else_statement
	| loop_statement
	| send_statement
	| publish_statement
	| receive_statement
	| subscribe_statement
	| action_statement
	| compound_statement;

throw_statement: THROW event broadcast_flag?;

broadcast_flag: BROADCAST;

event: IDENTIFIER | FINISH;

assign_statement: IDENTIFIER ASSIGN item;

if_statement: IF LPAREN condition_list RPAREN;

condition_list:
	condition
	| LPAREN condition_list RPAREN condition_operator LPAREN condition_list RPAREN;

condition: variable BIOP item;

variable: IDENTIFIER;

item: IDENTIFIER | string;

condition_operator: AND | OR;

loop_statement: LOOP LPAREN (time_condition_list)? RPAREN;

else_statement: ELSE;

time_condition_list:
	time (COMMA condition_list)?
	| condition_list;

time: INTEGER TIMEUNIT;

send_statement: SEND LPAREN target_team COMMA variable RPAREN;

target_team: IDENTIFIER;

publish_statement:
	PUBLISH LPAREN target_team COMMA variable RPAREN;

identifier_list: IDENTIFIER (COMMA IDENTIFIER)*;

action_statement:
	(identifier_list ASSIGN)? action_name LPAREN item_set_list? RPAREN timeout_property?;

action_name: IDENTIFIER;

item_set_list: (item | item_set) (COMMA (item | item_set))*;

item_set: LPAREN item_list RPAREN;

item_list: item (COMMA item)*;

timeout_property: TIMEOUT ASSIGN time;

receive_statement:
	output ASSIGN RECEIVE LPAREN target_team COMMA variable RPAREN;

subscribe_statement:
	output ASSIGN SUBSCRIBE LPAREN target_team COMMA IDENTIFIER RPAREN;

output: IDENTIFIER;

compound_statement: LBRACE statement* RBRACE;

repeat_statement: REPEAT LPAREN time_condition_list RPAREN;

mode_define:
	MODE_UPPER DOT mode_name (LPAREN parameter_list RPAREN)? LBRACE group_behavior* others_behavior?
		parallel_behavior? RBRACE;

mode_name: (IDENTIFIER | FINISH | PREVIOUS_MODE);

group_behavior:
	GROUP LPAREN group_name (COMMA group_condition)* RPAREN COLON mode_transition;

group_name: IDENTIFIER;

group_condition: min_count | proper_count | tag;

min_count: MIN ASSIGN INTEGER;

proper_count: PROPER ASSIGN INTEGER;

tag: TAGLIST ASSIGN LBRACE tag_condition RBRACE;

tag_condition:
	tag_list
	| LPAREN tag_condition RPAREN OR LPAREN tag_condition RPAREN;

tag_list: IDENTIFIER (COMMA IDENTIFIER)*;

mode_transition:
	MODE_TRANSITION DOT mode_transition_name (
		LPAREN item_set_list RPAREN
	)?;

mode_transition_name: IDENTIFIER;

others_behavior:
	OTHERS LPAREN group_name RPAREN COLON mode_transition;

parallel_behavior: SERVICES COLON service_mapping+;

service_mapping:
	service_name (LPAREN item_set_list RPAREN)? (
		COMMA service_property
	)*;

service_property: priority | timeout_property;

priority: PRIORITY ASSIGN PRIORITY_ENUM;

mode_copy:
	MODE_UPPER DOT mode_name COLON mode_name (COMMA mode_name)*;

mode_transition_define:
	MODE_TRANSITION DOT mode_transition_name (
		LPAREN parameter_list RPAREN
	)? LBRACE transition_behavior RBRACE;

main_define:
	MAIN DOT team_name LBRACE transition_behavior RBRACE;

transition_behavior: transition* default_mode;

transition: CASE LPAREN mode_name RPAREN COLON catch_statement+;

catch_statement: CATCH LPAREN event RPAREN COLON mode_assign;

mode_assign:
	MODE ASSIGN mode_name (LPAREN item_set_list RPAREN)?;

default_mode: DEFAULT COLON mode_assign;

string: '"' ~('"')+ '"';

BIOP: ('>=' | '>' | '<' | '<=' | '==' | '!=');
TIMEUNIT: ('MSEC' | 'SEC' | 'MINUTE' | 'HOUR' | 'DAY');
SEND: 'send';
PUBLISH: 'publish';
RECEIVE: 'receive';
SUBSCRIBE: 'subscribe';
MODE_UPPER: 'Mode';
MODE_TRANSITION: 'ModeTransition';
MAIN: 'Main';
THROW: 'throw';
REPEAT: 'repeat';
IF: 'if';
ELSE: 'else';
LOOP: 'loop';
TIME: 'time';
TIMEOUT: 'timeout';
AND: 'and';
OR: 'or';
ALL: 'all';
ASSIGN: '=';
COLON: ':';
COMMA: ',';
DOT: '.';
MODE: 'mode';
CASE: 'case';
CATCH: 'catch';
DEFAULT: 'default';
FINISH: 'FINISH';
PREVIOUS_MODE: 'PREVIOUS_MODE';
GROUP: 'group';
OTHERS: 'others';
SERVICES: 'services';
MIN: 'min';
PROPER: 'proper';
TAGLIST: 'tagList';
PRIORITY: 'priority';
PRIORITY_ENUM: ('H' | 'M' | 'L');
BROADCAST: 'broadcast';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
LSBRACKET: '[';
RSBRACKET: ']';
IDENTIFIER: [a-zA-Z][_a-zA-Z0-9]*;
INTEGER: [0-9]+;
SEPERATOR: ';';
LINE_COMMENT: '#' ~[\r\n]* -> skip;
WS: [ \t\r\n]+ -> skip;

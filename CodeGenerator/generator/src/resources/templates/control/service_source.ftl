<#import "/control/composite_service_macro.ftl" as composite_service_macro>
#include "${robotId}_service.h"
#include "${robotId}_port.h"
#include "${robotId}_action.h"
#include "${robotId}_event.h"
#include "${robotId}_variable.h"
#include "${robotId}_timer.h"
#include "${robotId}_common.h"
#include "logger.h"

// DECLARE SERVICE EXECUTION FUNCTION
<#list serviceList as service>
STATIC void execute_service_${service.name}();
</#list>

// DEFINE SERVICE ACTION LIST
<#list serviceList as service>
STATIC ACTION_ID action_list_of_${service.name}[${service.actionList?size}] = {
    <#list service.actionList as action>
    ID_ACTION_${action.name},
    </#list>
};
</#list>

// DEFINE SERVICE
SERVICE service_list[${serviceList?size}] = {
<#list serviceList as service>
    {ID_SERVICE_${service.name}, STOP, 0, ${service.actionList?size}, action_list_of_${service.name}, execute_service_${service.name}},
</#list>
};

// SERVICE STATE DEFINE
<#list compositeServiceFSM as service>
typedef enum _CSERVICE_${service.name} {
	<#list service.actionList as statement>
    ID_SERVICE_${service.name}_${statement.id},
	</#list> 
} SERVICE_${service.name};
</#list>

void stop_service(int32 service_id)
{
    ACTION *action_list = service
    service_list[service_id].state = STOP;
    for (int i = 0 ; i < service_list[service_id].action_list_size ; i++)
    {
        int action_id = service_list[service_id].action_list[i];
        if (action_list[action_id].state == RUN)
        {
            stop_action(action_id);
        }
    }
}

void run_service(int32 service_id)
{
    service_list[service_id].state = RUN;
    service_list[service_id].current_statement_id = 0;
}

void init_service() {
    for(int i = 0 ; i < ${serviceList?size} ; i++)
    {
        service_list[i].state = STOP;
        service_list[i].current_statement_id = 0;
    }
}

void execute_service() 
{
    for(int i = 0 ; i < ${serviceList?size} ; i++)
    {
        if (service_list[i].state == RUN)
        {
            service_lsit[i].execute_func();
        }
    }
}

<#list serviceList as service>
STATIC void execute_service_${service.name}() {
	switch (serviceList[ID_SERVICE_${service.name}].current_statement_id)
	{
	<#list service.statementList as statement>
        case ID_SERVICE_${service.name}_${statement.id}: 
        {
        <#switch statement.statementType>
        <#case "action">
            <@composite_service_macro.ACTION statement service.name/>
            <#break>
        <#case "receive">
        <#case "subscribe">
            <@composite_service_macro.RECEIVE statement service.name/>
            <#break>
        <#case "send">
        	<@composite_service_macro.SEND statement service.name/>
            <#break>
        <#case "publish">
            <@composite_service_macro.PUBLISH statement service.name/>
            <#break>
        <#case "if">
        <#case "loop">
        <#case "repeat">
            <@composite_service_macro.IF statement service.name/>
            <#break>
        <#case "throw">
            <@composite_service_macro.THROW statement service.name/>
            <#break>
        <#case "leader">
            <@composite_service_macro.LEADER statement service.name/>
            <#break>
        <#case "leader_end">
            <@composite_service_macro.LEADER_END statement service.name/>
            <#break>
        <#default>
            <@composite_service_macro.TRANSITION statement.transitionList "TRUE" service.name 0/>
        </#switch>

			break;
		}
	</#list>
	}
}

</#list>
#include "${robotId}_action.h"

// DEFINE ACTION RESOURCE LIST
<#list actionList as action>
RESOURCE_ID resource_list_of_${action.name}[${action.resourceList?size}] = {
    <#list action.resourceList as resource>
    ID_RESORUCE_${resource},
};
</#list>

// DEFINE ACTION
ACTION action_list[${actionList?size}] = {
<#list actionList as action>
    {ID_ACTION_${action.name}, STOP, ${action.resourceList?size}, resource_list_of_${action.name}},
</#list>
};

void stop_action(int32 action_id)
{
    UFControl_StopTask(CONTROL_TASK_ID, action_list[action_id].task_id);
    for (int i = 0 ; i < action_list[action_id].resource_list_size ; j++)
    {
        resource_list[action_list[action_id].resource_list[i]].state = NOT_OCCUPIED;
    } 
    action_list[action_id].state = STOP;
}
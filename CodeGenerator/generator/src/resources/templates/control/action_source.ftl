#include "${robotId}_action.h"
#include "${robotId}_port.h"

// DEFINE ACTION RESOURCE LIST
<#list actionTaskList as actionTask>
    <#if actionTask.actionImpl.actionImpl.resourceList?size gt 0>
RESOURCE_ID resource_list_of_${actionTask.name}[${actionTask.actionImpl.actionImpl.resourceList?size}] = {
        <#list actionTask.actionImpl.actionImpl.resourceList as resource>
    ID_RESORUCE_${resource},
        </#list>
};
    </#if>
</#list>

// DEFINE ACTION TASK
ACTION_TASK action_task_list[${actionTaskList?size}] = {
<#list actionTaskList as actionTask>
    {ID_ACTION_TASK_${actionTask.name}, ID_ACTION_TYPE_${actionTask.actionImpl.actionType.action.name}, ${actionTask.index}, ${actionTask.name}, 
     STOP, ${actionTask.actionImpl.actionImpl.isReturnImmediate.value}, ${actionTask.actionImpl.actionImpl.needdedResource?size}, <#if actionTask.actionImpl.actionImpl.needdedResource?size gt 0>resource_list_of_${actionTask.name}<#else>NULL</#if>, 
     ${actionTask.inputPortList?size}, input_port_of_${actionTask.name}, ${actionTask.outputPortList?size}, output_port_of_${actionTask.name}
     <#if actionTask.groupPort?has_content>&group_port_of_${actionTask.name}<#else>NULL</#if>},
</#list>
};

ACTION_TASK* get_action_task(int32 list_size, ACTION_TASK_ID *candidate_task_list)
{
    return action_task_list + candidate_task_list[0];
}

void stop_action_task(int32 action_task_id)
{
    UFControl_StopTask(CONTROL_TASK_ID, action_task_list[action_task_id].task_id);
    for (int i = 0 ; i < action_task_list[action_task_id].resource_list_size ; j++)
    {
        resource_list[action_task_list[action_task_id].resource_list[i]].state = NOT_OCCUPIED;
    } 
    action_task_list[action_task_id].state = STOP;
}

void action_task_state_polling()
{
    for (int i = 0 ; i < ${actionTaskList?size} ; i++)
    {
        if (action_task_list[i].state == WRAPUP)
        {
            stop_action(action_task_list[i].action_id);
        }
        else if (action_task_list[i].state == RUN)
        {
            ETaskState state;
            UFTask_GetState(action_task_list[i].task_id, action_task_list[i].task_name, &state);
            if (state == STATE_END || state == STATE_STOP)
            {
                action_task_list[i].state = STOP;
            }
        }
    }
}
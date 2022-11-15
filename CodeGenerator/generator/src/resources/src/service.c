#include "logger.h"
#include "service.h"

// MANAGE SERVICE LIST
SERVICE* get_service(SERVICE *serviceList, int serviceNum, char *serviceName) {
    LOG_DEBUG("Get service %s", serviceName);
    for (int i = 0 ; i < serviceNum ; i++)
    {
        if(!strcmp(serviceList[i].serviceName, serviceName))
        {
            return &(serviceList[i]);
        }
    }
    return NULL;
}

SERVICE* resource_conflict_check(SERVICE *serviceList, int serviceNum, SERVICETASK *task) {
	for (int i = 0 ; i < serviceNum ; i++)
	{
		if (serviceList[i].task != NULL)
		{
			if (!strcmp(serviceList[i].task->name, task->name))
			{
				continue;
			}
			for (int j = 0 ; j < task->resourceNum ; j++)
			{
				for (int k = 0 ; k < serviceList[i].task->resourceNum ; k++)
				{
					if (task->resource[j] == serviceList[i].task->resource[k])
					{
						return serviceList + i;
					}
				}
			}
		}
	}
	return NULL;
}

void remove_service(SERVICE *service, int taskId) {
    LOG_DEBUG("Remove service %s task %s", service->serviceName, service->task->name);
    UFControl_StopTask(taskId, service->task->name, FALSE);
    if(service->timer != NULL)
    {
        remove_timer(service->timer);
        service->timer = NULL;
    }
    service->task = NULL;
    service->state = SERVICE_STOP;
}

void service_finish_check(SERVICE *service, int taskId) {
	ETaskState state;
	UFTask_GetState(taskId, service->task->name, &state);
	if (state == STATE_STOP) {
		service->state = SERVICE_FINISH;
	}
}

void service_state_polling(SERVICE *serviceList, int serviceNum, int taskId) {
    for (int i = 0 ; i < serviceNum ; i++)
    {
        if (serviceList[i].task == NULL)
        {
            continue;
        }

        LOG_DEBUG("The state of service %s: %d", serviceList[i].serviceName, serviceList[i].state);

        switch(serviceList[i].state)
        {
            case SERVICE_TIMEOUT:
            case SERVICE_FINISH:
                {
                    serviceList[i].state = SERVICE_WRAPUP;
                    break;
                }
            case SERVICE_WRAPUP:
                {
                    remove_service((&serviceList[i]), taskId);
                    break;
                }
            default:
                {
                    if(serviceList[i].timer != NULL)
                    {
                        service_timer_check((&serviceList[i]));
                    }
                    service_finish_check((&serviceList[i]), taskId);
                }
        };
    }
}

// MANAGE SERVICE TASK LIST
SERVICETASK* get_task(SERVICETASK *serviceTaskList, int serviceTaskNum, char *taskName) {
    LOG_DEBUG("Get task %s", taskName);
    for (int i = 0 ; i < serviceTaskNum ; i++)
    {
        if(!strcmp(serviceTaskList[i].name, taskName))
        {
            return &(serviceTaskList[i]);
        }
    }
    return NULL;
}

void service_task_init(SERVICETASK *serviceTaskList, int serviceTaskNum, int taskId) {
	for (int i = 0 ; i < serviceTaskNum ; i++)
	{
		UFControl_StopTask(taskId, serviceTaskList[i].name, FALSE);
		LOG_INFO("task %s stop", serviceTaskList[i].name);
	}
}

// TIMER CHECK
void service_timer_check(SERVICE *service){
    if (timer_check(service->timer) == TRUE)
    {
        service->state = SERVICE_TIMEOUT;
        LOG_DEBUG("Service (%s) timeout", service->serviceName);
    }
}

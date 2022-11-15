#ifndef __SERVICE_HEADER__
#define __SERVICE_HEADER__

#include "semo_timer.h"
#include <UFControl.h>
#include <UFTask.h>

typedef enum _SERVICE_STATE
{
    SERVICE_STOP,
    SERVICE_RUN,
    SERVICE_TIMEOUT,
    SERVICE_FINISH,
    SERVICE_WRAPUP
} SERVICE_STATE;

typedef struct _SERVICETASK
{
    char *name;
    int resourceNum;
    int *resource;
    int returnImmediate;
} SERVICETASK;

typedef struct _SERVICE
{
    char *serviceName;
    int returnImmediate;
    SERVICE_STATE state;
    SERVICETASK *task;
    TIMER *timer;
} SERVICE;

SERVICE *get_service(SERVICE *serviceList, int serviceNum, char *serviceName);
SERVICE *resource_conflict_check(SERVICE *serviceList, int serviceNum, SERVICETASK *task);
void remove_service(SERVICE *service, int taskId);
SERVICETASK *get_task(SERVICETASK *serviceTaskList, int serviceTaskNum, char *taskName);
void service_task_init(SERVICETASK *serviceTaskList, int serviceTaskNum, int taskId);
void service_timer_check(SERVICE *service);
void service_finish_check(SERVICE *service, int taskId);
void service_state_polling(SERVICE *serviceList, int serviceNum, int taskId);

#endif

#ifndef __${robotId}_SERVICE_HEADER__
#define __${robotId}_SERVICE_HEADER__

#include "${robotId}_action.h"

typedef enum _SERVICE_ID {
<#list serviceList as service>
    ID_SERVICE_${service.name},
</#list>
} SERVICE_ID;

typedef void SERVICE_EXECUTION_FUNC();

typedef struct _SERVICE {
    SERVICE_ID service_id;
    STATE state;
    int32 current_statement_id;
    int32 action_list_size;
    ACTION_ID *action_list;
    SERVICE_EXECUTION_FUNC execution_func;
} SERVICE;

extern SERVICE service_list[${serviceList?size}];

// DECLARE EXTERN FUNCTION
void init_service();
void execute_service();

#endif
#ifndef __${robotId}_ACTION_HEADER__
#define __${robotId}_ACTION_HEADER__

#include "${robotId}_resource.h"

typedef enum _ACTION_ID {
<#list actionList as action>
    ID_ACTION_${action.name},
</#list>
} ACTION_ID;

typedef struct _ACTION {
    ACTION_ID action_id;
    STATE state;
    int32 resource_list_size;
    RESOURCE_ID *resource_list;
} ACTION;

extern ACTION action_list[${actionList?size}];

void stop_action(int32 action_id);

#endif
#ifndef __${robotId}_MODE_HEADER__
#define __${robotId}_MODE_HEADER__

// MODE STATE DEFINE
typedef enum _MODE {
<#list modeList as mode>
    ID_MODE_${mode.name},
</#list>
} MODE_ID;

typedef struct _MODE_INFO {
    MODE_ID mode_id
    STATE state;
    int32 service_list_size;
    SERVICE_ID *service_list;
    int32 group_list_size;
    int32 *group_list
    GROUP_ALLOCATION_STATE group_allocation_state;
} MODE;

extern MODE mode_list[${modeList?size}];

#endif
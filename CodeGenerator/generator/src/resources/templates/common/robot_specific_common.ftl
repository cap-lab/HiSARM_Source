#ifndef __ROBOT_SPECIFIC_COMMON_HEADER__
#define __ROBOT_SPECIFIC_COMMON_HEADER__

#define THIS_ROBOT_ID ${robotId}
#define CONTROL_TASK_ID ${controlTaskId}

typedef enum _GROUP {
    <#list groupList as group>
    ${group},
    </#list>
} GROUP;

#endif
#ifndef __${robotId}_GROUP_HEADER__
#define __${robotId}_GROUP_HEADER__

// GROUP DEFINE
typedef enum _GROUP {
<#list groupList as group>
    ID_GROUP_${group.name} = ${group.index},
</#list>
} GROUP_ID;

extern GROUP group_list[${groupList?size}];
extern int32 group_num;

#endif
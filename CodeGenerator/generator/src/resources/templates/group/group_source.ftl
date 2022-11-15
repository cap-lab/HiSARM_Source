#include "${robotId}_group.h"

GROUP_ID group_list[${groupMap?size}] = {
    <#list groupMap as group, index>
    ID_GROUP_${group},
    </#list>
};
int8 group_state_list[${groupMap?size}];
int32 group_num = ${groupMap?size};

void group_init(void)
{
    for (int i = 0; i < group_num; i++)
    {
        group_state_list[i] = FALSE;
    }
}

void set_group_state(GROUP_ID group, int8 state)
{
    for (int i = 0; i < group_num; i++)
    {
        if (group_list[i] == group)
        {
            group_state_list[i] = state;
            break;
        }
    }
}

int8 get_group_state(GROUP_ID group)
{
    for (int i = 0; i < group_num; i++)
    {
        if (group_list[i] == group)
        {
            return group_state_list[i];
        }
    }
    return FALSE;
}
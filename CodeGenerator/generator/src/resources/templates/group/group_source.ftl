
GROUP group_list[${groupList?size}] = {
    <#list groupList as group>
    ID_GROUP_${group.name},
    </#list>
};

int32 group_num = ${groupList?size};
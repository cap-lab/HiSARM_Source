#include "${robotId}_variable.h"
#include "semo_common.h"

// VARIABLE BUFFER DEFINE
<#list variableList as variable>
    <#if variable.type.type.getValue() == "enum"">
VARIABLE_TYPE_${variable.type.name} variable_buffer_of_${variable.id}[${variable.type.count}]<#if variable.defaultValue?exist> = {<#list variable.defaultValue as value> ${value},</#list>}</#if>;
    <#else>
${variable.type.type.getValue()} variable_buffer_of_${variable.id}[${variableType.count}]<#if variable.defaultValue?exist> = {<#list variable.defaultValue as value> ${value},</#list>}</#if>;
    </#if>
</#list>

// VARIABEL RELATION DEFINE
<#list variableList as variable>
    <#if variable.childVariable?size gt 0>
void variable_relation_of_${variable.id}[${variable.type.count}] = {
        <#list variable.childVariable as childVariable>
    variable_buffer_of_${variable_buffer_of_${childVariable.id}},
        </#list>
};
    </#if>
</#list>

// VARIABLE DEFINE
<#list variableList as variable>
VARIABLE variable_${variable.id} = {
    ${variable.type.size}, variable_buffer_of_${variable.id}, ${variable.childVariable.type.size}, ${variable.childVariable?size}, variable_relation_of_${variable.id}
};
</#list>
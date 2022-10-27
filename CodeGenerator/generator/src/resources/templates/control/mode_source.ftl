#include "${robotId}_mode.h"
#include "${robotId}_service.h"

// DEFINE MODE SERVICE LIST
<#list modeList as mode>
SERVICE_ID service_list_of_${mode.name}[${mode.serviceList?size}] = {
    <#list mode.serviceList as service>
    ID_SERVICE_${service.name},
    </#list>
};
</#list>

// DEFINE MODE
MODE_INFO mode_list[${modeList?size}] = {
<#list modeList as mode>
    {ID_MODE_${mode.name}, STOP, ${mode.serviceList?size}, service_list_of_${mode.name}},
</#list>
};

void stop_mode(int32 mode_id)
{
    mode_list[mode_id].state = STOP;
    for (int i = 0 ; i < mode_list[mode_id].service_list_size; i++)
    {
        stop_service(mode_list[mode_id].service_list[i]);
    }
}

void run_mode(int32 mode_id)
{
    mode_list[mode_id].state = RUN;
    for (int i = 0 ; i < mode_list[mode_id].service_list_size; i++)
    {
        run_service(mode_list[mode_id].service_list[i]);
    }
}
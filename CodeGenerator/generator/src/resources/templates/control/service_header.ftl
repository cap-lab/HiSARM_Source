#ifndef __${robotId}_SERVICE_HEADER__
#define __${robotId}_SERVICE_HEADER__

typedef enum _SERVICE_ID {
<#list serviceList as service>
    ID_SERVICE_${service.serviceId},
</#list>
} SERVICE_ID;

// DECLARE EXTERN FUNCTION
void init_service();
void execute_service();
void run_service(SERVICE_ID service_id, GROUP_ID group);
void stop_service(int32 service_id);

#endif
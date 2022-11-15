#include "${robotId}_event.h"
#include "${robotId}_port.h"

int8 event_list[${eventList?size}] = {<#list 1..eventList?size as i>FALSE,</#list>};

void event_list_init() {
    memset(event_list, FALSE, ${eventList?size}*sizeof(int8));
}

void event_polling() {
    int32 dataNum = 0;
    int32 dataLen = 0;
    int32 event;
    for (int i = 0 ; i < throw_in_port_list_size ; i++)
    {
        UFPort_GetNumOfAvailableData(throw_in_port_list[i].port->portId, 0, &dataNum);
        if (dataNum > 0)
        {
            UFPort_ReadFromQueue(throw_in_port_list[i].port->portId, (unsigned char*) &event, sizeof(int32), 0, &dataLen);
            event_list[event] = TRUE;
        }
    }
}
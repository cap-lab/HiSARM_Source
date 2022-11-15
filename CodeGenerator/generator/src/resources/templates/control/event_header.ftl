#ifndef __${robotId}_EVENT_HEADER__
#define __${robotId}_EVENT_HEADER__

// EVENT DEFINE
typedef enum _EVENT_ID {
<#list eventList as event>
    ID_EVENT_${event},
</#list>
} EVENT_ID;

extern int8 event_list[${eventList?size}];

void event_list_init();

#endif
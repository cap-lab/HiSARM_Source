#ifndef __${robotId}_COMMON_HEADER__
#define __${robotId}_COMMON_HEADER__

#include "timer.h"

typedef struct TIMER {
    int32 serviceId;
    int32 statementId;
    int32 timerId;
    int alarmed;
} TIMER;

// TIMER LIST DEFINE

TIMER* get_timer(int32 serviceId, int32 statementId);
TIMER* new_timer(int time, char *unit, int32 serviceId, int32 statementId);
void remove_timer(TIMER* timer);
void remove_all_timer();
int timer_check(TIMER *timer);

#endif
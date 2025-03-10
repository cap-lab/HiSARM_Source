#ifndef __ROBOT_SPECIFIC_COMMON_HEADER__
#define __ROBOT_SPECIFIC_COMMON_HEADER__

#include "semo_common.h"

#define THIS_ROBOT_ID (${robotId})
#define THIS_ROBOT_NAME "${robotName}"
#define THIS_ROBOT_TYPE "${robotType}"
#define SEMO_ROBOT_NUM (${robotNum})

#define ROBOT_SPECIFIC(x) ${robotName}_##x

#endif
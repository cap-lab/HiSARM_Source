#include "common.h"
#include "${robotId}_variable.h"

extern LIBFUNC(void, init, void);
extern LIBFUNC(void, wrapup, void);

extern LIBFUNC(void, set_${libName}_listen, int32 sender_robot, void *buffer);
extern LIBFUNC(int8, get_${libName}_action, int32 *sender_robot, void *buffer);
extern LIBFUNC(int8, avail_${libName}_action);
extern LIBFUNC(void, set_${libName}_action, void *buffer);
extern LIBFUNC(int8, get_${libName}_report, void *buffer);
extern LIBFUNC(int8, avail_${libName}_report);
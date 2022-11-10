#ifndef __SEMO_COMMUNICATION_HEADER__
#define __SEMO_COMMUNICATION_HEADER__

#include "semo_common.h"

int channelPortRead(int portId, void *buffer, int32 bufferSize, int8 isBuffer);
int channelPortWrite(int portId, void *buffer, int32 bufferSize, int8 isBuffer);

#endif

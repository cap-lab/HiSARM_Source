#ifndef __COMMUNICATION_HEADER__
#define __COMMUNICATION_HEADER__

int channelPortRead(int portId, void *buffer, int bufferSize, int isBuffer);
int channelPortWrite(int portId, void *buffer, int bufferSize, int isBuffer);

#endif

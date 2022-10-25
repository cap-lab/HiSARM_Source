#ifndef __PORT_HEADER__
#define __PORT_HEADER__

#include <UFPort.h>
#include "common.h"

typedef struct _PORT
{
	char *portName;
	int portId;
} PORT;

typedef struct _PORT_INFO
{
	char *taskName;
	int inputPortNum;
	int outputPortNum;
	PORT *inputPortList;
	PORT *outputPortList;
} PORT_INFO;

typedef struct _CHANNEL_PORT
{
	char *inPortName;
	int inPortId;
	char *outPortName;
	int outPortId;
	void *buffer;
	int size;
	int refreshed;
} CHANNEL_PORT;

typedef struct _MULTICAST_PACKET_HEADER
{
	int64 time;
	int32 senderRobotId;
} MULTICAST_PACKET_HEADER;

typedef struct _MULTICAST_PACKET
{
	MULTICAST_PACKET_HEADER header;
	void *body;
} MULTICAST_PACKET;

typedef struct _MULTICAST_PORT
{
	char *multicastPortName;
	int multicastGroupId;
	int multicastPortId;
	char *channelPortName;
	int channelPortId;
	MULTICAST_PACKET buffer;
	int size;
	int64 beforeTime;
} MULTICAST_PORT;

typedef int LIBRARY_AVAIL_FUNC();
typedef int LIBRARY_GET_FUNC(void *buffer);
typedef void LIBRARY_SET_FUNC(int32 sender_robot, void *buffer);
typedef struct _GROUP_SERVICE_PORT
{
	char *multicastPortName;
	int multicastGroupId;
	int multicastPortId;
	LIBRARY_AVAIL_FUNC *libAvailFunc;
	LIBRARY_GET_FUNC *libGetFunc;
	LIBRARY_SET_FUNC *libSetFunc;
	MULTICAST_PACKET buffer;
	int size;
	int64 beforeTime;
} GROUP_SERVICE_PORT;

PORT *findPortListByTaskName(const PORT_INFO **service_task_port_list, int portNum, char *taskName, DIRECTION direction);

#endif

/////////////////////////////////////
// include header section
/////////////////////////////////////

// ##DEFINE_SECTION::START
#include "UFTimer.h"
#pragma pack(push, 1)
// ##DEFINE_SECTION::END

TASK_CODE_BEGIN

/////////////////////////////////////
// global definition
/////////////////////////////////////

// CHANNEL VARIABLE DEFINE
<#list channelPortMap as outPort, inPort>
    <#if outPort.variableType.variableType.type.getValue() == "enum">
STATIC VARIABLE_${outPort.variableType.variableType.name} ${outPort.getVariableName()}[${outPort.variableType.variableType.count}];
    <#else>
STATIC ${outPort.variableType.variableType.type.getValue()} ${outPort.getVariableName()}[${outPort.variableType.variableType.count}];
    </#if>
</#list>

// MULTICAST VARIABLE DEFINE
<#list multicastPortMap as multicastPort, channelPort>
STATIC struct _${multicastPort.getVariableName()} {
    MULTICAST_PACKET_HEADER header;
    <#if multicastPort.variable.variableType.type.getValue() == "enum">
    VARIABLE_${multicastPort.variableType.variableType.name} body[${multicastPort.variableType.variableType.count}];
    <#else>
    ${multicastPort.variableType.variableType.type.getValue()} body[${multicastPort.variableType.variableType.count}];
    </#if>
} ${multicastPort.getVariableName()} = {{-1, THIS_ROBOT_ID}, {0,}};
STATIC MULTICAST_PACKET packet_${multicastPort.getVariableName()} = {&${multicastPort.getVariableName()}.header, ${multicastPort.getVariableName()}.body};
</#list>

// SHARED DATA VARIABLE DEFINE
<#list sharedDataPortMap as libPort, multicastPort>
STATIC struct _${libPort.library.name} {
    MULTICAST_PACKET_HEADER header;
    <#if libPort.library.variableType.variableType.type.getValue() == "enum">
    VARIABLE_${libPort.variableType.variableType.name} body[${libPort.variableType.variableType.count}];
    <#else>
    ${libPort.variableType.variableType.type.getValue()} body[${libPort.variableType.variableType.count}];
    </#if>
} ${libPort.library.name} = {{-1, THIS_ROBOT_ID}, {0,}};
STATIC MULTICAST_PACKET packet_${libPort.library.name} = {&${libPort.library.name}.header, ${libPort.library.name}.body}
</#list>

// CHANNEL_PORT_SECTION
STATIC CHANNEL_PORT channelPortList[${channelPortMap?size}] = {
<#list channelPortMap as outPort, inPort>
    {"${inPort.name}", -1, "${outPort.name}", -1, ${outPort.getVariableName()}, ${outPort.sampleSize}, FALSE}, 
</#list>
};

/ MULTICAST PORT SECTION
STATIC MULTICAST_PORT multicastPortList[${multicastPortMap?size}] = {
<#list multicastPortMap as multicastPort, channelPort>
    {"${multicastPort.name}", -1, -1, "${channelPort.name}", -1,  ${multicastPort.getVariableName()}, packet_${multicastPort.getVariableName()}, ${multicastPort.variableType.getSize()}, -1},
</#list>
};

// GROUP SERVICE PORT SECTION
STATIC SHARED_DATA_PORT groupServicePortList[${sharedDataPortMap?size}] = {
<#list sharedDataPortMap as libPort, multicastPort>
    {"${multicastPort.name}", -1, -1, l_${libPort.library.name}_avail_${libPort.library.name}_report, l_${libPort.library.name}_get_${libPort.library.name}_report, NULL, ${libPort.library.name}, packet_${libPort.library.name}, ${libPort.variableType.getSize()}, -1},
</#list>
};

// LEADER PORT SECTION
STATIC LEADER_PORT leaderPortList[${leaderPortList.values()?size}] = {
    <#list leaderPortMap as robotIdPort, heartbeatPort>
    {"${robotIdPort.name}", -1, -1, {{-1, THIS_ROBOT_ID}, -1}, -1, l_leader_avail_robot_id_report, l_leader_get_robot_id_report, NULL,
    "${heartbeatPort.name}", -1, -1, {{-1, THIS_ROBOT_ID}, -1}, -1, l_leader_avail_heartbeat_report, l_leader_get_heartbeat_report, NULL, ${robotIdPort.message}},
    </#list>
};

/////////////////////////////////////
// init code
/////////////////////////////////////

STATIC void channelPortInit() {
    for (int i = 0 ; i<sizeof(channelPortList)/sizeof(CHANNEL_PORT) ; i++)
    {
        UFPort_Initialize(TASK_ID, channelPortList[i].inPortName, &(channelPortList[i].inPortId));
        UFPort_Initialize(TASK_ID, channelPortList[i].outPortName, &(channelPortList[i].outPortId));
    }
}

STATIC void multicastPortInit() {
    for (int i = 0 ; i<sizeof(multicastPortList)/sizeof(MULTICAST_PORT) ; i++)
    {
        UFPort_Initialize(TASK_ID, multicastPortList[i].channelPortName, &(multicastPortList[i].channelPortId));
        UFMulticastPort_Initialize(TASK_ID, multicastPortList[i].multicastPortName, &(multicastPortList[i].multicastGroupId), &(multicastPortList[i].multicastPortId));
    }
}

STATIC void libraryPortInit() {
    for (int i = 0 ; i<sizeof(groupServicePortList)/sizeof(GROUP_SERVICE_PORT) ; i++)
    {
        UFMulticastPort_Initialize(TASK_ID, groupServicePortList[i].multicastPortName, &(groupServicePortList[i].multicastGroupId), &(groupServicePortList[i].multicastPortId));
    }
}

STATIC void leaderPortInit() {
    for (int i = 0 ; i < sizeof(leaderPortList)/sizeof(LEADER_PORT) ; i++)
    {
        UFMulticastPort_Initialize(TASK_ID, leaderPortList[i].robotIdPortName, &(leaderPortList[i].robotIdGroupId), &(leaderPortList[i].robotIdPortId));
        UFMulticastPort_Initialize(TASK_ID, leaderPortList[i].heartbeatPortName, &(leaderPortList[i].heartbeatGroupId), &(leaderPortList[i].heartbeatPortId));
    }
}

TASK_INIT
{
	LOG_INFO("INIT");
    channelPortInit(); 
    multicastPortInit();
    libraryPortInit();
    leaderPortInit();
}

/////////////////////////////////////
// go code
/////////////////////////////////////

STATIC void channelPortSend() {
    int dataLen = 0;
    
    for (int i = 0 ; i<sizeof(channelPortList)/sizeof(CHANNEL_PORT) ; i++)
    {
    	if (channelPortRead(channelPortList[i].inPortId, channelPortList[i].buffer, channelPortList[i].size, FALSE) > 0) 
    	{
    	    channelPortList[i].refreshed = TRUE;
    	}
    	if (channelPortList[i].refreshed = TRUE) 
    	{
    	    if (channelPortWrite(channelPortList[i].outPortId, channelPortList[i].buffer, channelPortList[i].size, FALSE) > 0) 
    	    {
    	        channelPortList[i].refreshed = FALSE;
    	    }
    	}    	    
    }
}

STATIC void multicastPortSend() {
    for (int i = 0 ; i<sizeof(multicastPortList)/sizeof(MULTICAST_PORT) ; i++)
    {
        if (channelPortRead(multicastPortList[i].channelPortId, multicastPortList[i].packet->body, multicastPortList[i].size), FALSE) > 0) 
        {
            int dataLen;
        	UFTimer_GetCurrentTime(THIS_TASK_ID, &(multicastPortList[i].packet->header->time));
        	UFMulticastPort_WriteToBuffer(multicastPortList[i].multicastGroupId, multicastPortList[i].multicastPortId, (unsigned char * ) &(multicastPortList[i].buffer), multicastPortList[i].size + sizeof(MULTICAST_PACKET_HEADER), &dataLen);
        }
    }
}

STATIC void groupServicePortSend() {
    for (int i = 0 ; i<sizeof(groupServicePortList)/sizeof(GROUP_SERVICE_PORT) ; i++)
    {
        if (groupServicePortList[i].libAvailFunc() == TRUE) 
        {
            int dataLen;
            groupServicePortList[i].libGetFunc(groupServicePortList[i].packet->body);
            UFTimer_GetCurrentTime(THIS_TASK_ID, &(groupServicePortList[i].packet->header->time));
            UFMulticastPort_WriteToBuffer(groupServicePortList[i].multicastGroupId, groupServicePortList[i].multicastPortId, (unsigned char * ) &(groupServicePortList[i].buffer), groupServicePortList[i].size + sizeof(MULTICAST_PACKET_HEADER), &dataLen);
        }
    }
}

STATIC void leaderPortSend() {
    for (int i = 0 ; i < sizeof(leaderPortList)/sizeof(LEADER_PORT) ; i++)
    {
        if (leaderPortList[i].robotIdAvailFunc(leaderPortList[i].groupId) == TRUE) 
        {
            int dataLen;
            leaderPortList[i].robotIdBuffer.body = leaderPortList[i].robotIdGetFunc(leaderPortList[i].groupId);
            UFTimer_GetCurrentTime(THIS_TASK_ID, &(leaderPortList[i].robotIdBuffer.header.time));
            UFMulticastPort_WriteToBuffer(leaderPortList[i].robotIdGroupId, leaderPortList[i].robotIdPortId, (unsigned char * ) &(leaderPortList[i].robotIdBuffer), sizeof(LEADER_PACKET), &dataLen);
        }
        if (leaderPortList[i].heartbeatAvailFunc(leaderPortList[i].groupId) == TRUE) 
        {
            int dataLen;
            leaderPortList[i].heartbeatBuffer.body = leaderPortList[i].heartbeatGetFunc(leaderPortList[i].groupId);
            UFTimer_GetCurrentTime(THIS_TASK_ID, &(leaderPortList[i].heartbeatBuffer.header.time));
            UFMulticastPort_WriteToBuffer(leaderPortList[i].heartbeatGroupId, leaderPortList[i].heartbeatPortId, (unsigned char * ) &(leaderPortList[i].heartbeatBuffer), sizeof(LEADER_PACKET), &dataLen);
        }
    }
}

TASK_GO
{
    channelPortSend();
    multicastPortSend();
    groupServicePortSend();
    leaderPortSend();
}

/////////////////////////////////////
// wrapup code
/////////////////////////////////////

TASK_WRAPUP
{

}

TASK_CODE_END

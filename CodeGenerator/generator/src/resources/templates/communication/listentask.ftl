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
<#list channelPortMap as inPort, outPort>
    <#if inPort.variableType.variableType.type.getValue() == "enum">
STATIC VARIABLE_${inPort.variableType.variableType.name} ${inPort.getVariableName()}[${inPort.variableType.variableType.count}];
    <#else>
STATIC ${inPort.variableType.variableType.type.getValue()} ${inPort.getVariableName()}[${inPort.variableType.variableType.count}];
    </#if>
</#list>

// MULTICAST VARIABLE DEFINE
<#list multicastPortMap as multicastPort, channelPort>
STATIC struct _${multicastPort.getVariableName()} {
    MULTICAST_PACKET_HEADER header;
    <#if multicastPort.variableType.variableType.type.getValue() == "enum">
    VARIABLE_${multicastPort.variableType.variableType.name} body[${multicastPort.variableType.variableType.count}];
    <#else>
    ${multicastPort.variableType.variableType.type.getValue()} body[${multicastPort.variableType.variableType.count}];
    </#if>
} ${multicastPort.getVariableName()} = {{-1, -1}, {0,}};
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
} ${libPort.library.name} = {{-1, -1}, {0,}};
STATIC MULTICAST_PACKET packet_${libPort.library.name} = {&${libPort.library.name}.header, ${libPort.library.name}.body}
</#list>

// CHANNEL PORT SECTION
STATIC CHANNEL_PORT channelPortList[${channelPortMap?size}] = {
<#list channelPortMap as inPort, outPort>
    {"${inPort.name}", -1, "${outPort.name}", -1, ${inPort.getVariableName()}, ${inPort.sampleSize}, FALSE}, 
</#list>
};

// MULTICAST PORT SECTION
STATIC MULTICAST_PORT multicastPortList[${multicastPortMap?size}] = {
<#list multicastPortMap as multicastPort, channelPort>
    {"${multicastPort.name}", -1, -1, "${channelPort.name}", -1, ${multicastPort.getVariableName()}, packet_${multicastPort.getVariableName()}, ${multicastPort.variableType.getSize()}, -1},
</#list>
};

// GROUP SERVICE PORT SECTION
STATIC GROUP_SERVICE_PORT sharedDataPortList[${sharedDataPortMap?size}] = {
<#list sharedDataPortMap as libPort, multicastPort>
    {"${multicastPort.name}", -1, -1, NULL, NULL, l_${libPort.library.name}_set_${libPort.library.name}_listen, ${libPort.library.name}, packet_${libPort.library.name}, ${libPort.variableType.getSize()}, -1},
</#list>
};

// LEADER PORT SECTION
STATIC LEADER_PORT leaderPortList[${leaderPortMap?size}] = {
<#list leaderPortMap as robotIdPort, heartbeatPort>
    {"${robotIdPort.name}", -1, -1, {{-1, -1}, -1}, -1, NULL, NULL, l_leader_set_robot_id_listen, 
    "${heartbeatPort.name}", -1, -1, {{-1, -1}, -1}, -1, NULL, NULL, l_leader_set_heartbeat_listen, ID_GROUP_${robotIdPort.group}},
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
    for (int i = 0 ; i<sizeof(sharedDataPortList)/sizeof(GROUP_SERVICE_PORT) ; i++)
    {
        UFMulticastPort_Initialize(TASK_ID, sharedDataPortList[i].multicastPortName, &(sharedDataPortList[i].multicastGroupId), &(sharedDataPortList[i].multicastPortId));
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

STATIC void channelPortReceive() {
    int dataLen;
    for (int i = 0 ; i<sizeof(channelPortList)/sizeof(CHANNEL_PORT) ; i++)
    {
    	if (channelPortRead(channelPortList[i].inPortId, channelPortList[i].buffer, channelPortList[i].size, FALSE) > 0) 
    	{
    	    channelPortList[i].refreshed = TRUE;
    	}
    	if (channelPortList[i].refreshed == TRUE) 
    	{
    	    if (channelPortWrite(channelPortList[i].outPortId, channelPortList[i].buffer, channelPortList[i].size, FALSE) > 0) 
    	    {
    	        channelPortList[i].refreshed = FALSE;
    	    }
    	}    	    
    }
}

STATIC void multicastPortReceive() {
    int dataLen;
    for (int i = 0 ; i<sizeof(multicastPortList)/sizeof(MULTICAST_PORT) ; i++)
    {
        UFMulticastPort_ReadFromBuffer(multicastPortList[i].multicastGroupId, multicastPortList[i].multicastPortId, (unsigned char*) &multicastPortList[i].buffer, multicastPortList[i].size + sizeof(MULTICAST_PACKET_HEADER), &dataLen);
        if (dataLen > 0) 
        {
            if (multicastPortList[i].beforeTime < multicastPortList[i].buffer.header.time) 
            {
                if (channelPortWrite(multicastPortList[i].channelPortId, &multicastPortList[i].buffer.body, multicastPortList[i].size, FALSE) > 0) 
                {
                	    multicastPortList[i].beforeTime = multicastPortList[i].buffer.header.time;
                }
            }
        }
    }
}

STATIC void groupServicePortReceive() {
    int dataLen;
    for (int i = 0 ; i<sizeof(sharedDataPortList)/sizeof(GROUP_SERVICE_PORT) ; i++)
    {
        UFMulticastPort_ReadFromBuffer(sharedDataPortList[i].multicastGroupId, sharedDataPortList[i].multicastPortId, (unsigned char*) &(sharedDataPortList[i].buffer), sharedDataPortList[i].size + sizeof(MULTICAST_PACKET_HEADER), &dataLen);
        if (dataLen > 0) 
        {
	         if (sharedDataPortList[i].beforeTime < sharedDataPortList[i].buffer.header.time) 
	         {
        		    sharedDataPortList[i].libSetFunc(sharedDataPortList[i].buffer.body);
        		    sharedDataPortList[i].beforeTime = sharedDataPortList[i].buffer.header.time;
         	 }
        }
    }
}

STATIC void leaderPortReceive() {
    int dataLen;
    for (int i = 0 ; i < sizeof(leaderPortList)/sizeof(LEADER_PORT) ; i++)
    {
        UFMulticastPort_ReadFromBuffer(leaderPortList[i].robotIdGroupId, leaderPortList[i].robotIdPortId, (unsigned char*) &(leaderPortList[i].robotIdBuffer), sizeof(LEADER_PACKET), &dataLen);
        if (dataLen > 0) 
        {
	         if (leaderPortList[i].robotIdBeforeTime < leaderPortList[i].robotIdBuffer.header.time) 
             {
        		    leaderPortList[i].robotIdSetFunc(leaderPortList.groupId, leaderPortList[i].robotIdBuffer.body);
        		    leaderPortList[i].robotIdBeforeTime = leaderPortList[i].robotIdBuffer.header.time;
             }
        }
        UFMulticastPort_ReadFromBuffer(leaderPortList[i].heartbeatGroupId, leaderPortList[i].heartbeatPortId, (unsigned char*) &(leaderPortList[i].heartbeatBuffer), sizeof(LEADER_PACKET), &dataLen);
        if (dataLen > 0) 
        {
	         if (leaderPortList[i].heartbeatBeforeTime < leaderPortList[i].heartbeatBuffer.header.time) 
             {
        		    leaderPortList[i].heartbeatSetFunc(leaderPortList.groupId, leaderPortList[i].heartbeatBuffer.body);
        		    leaderPortList[i].heartbeatBeforeTime = leaderPortList[i].heartbeatBuffer.header.time;
             }
        }
    }
}

TASK_GO
{
    channelPortReceive();
    multicastPortReceive();
    groupServicePortReceive();
    leaderPortReceive();
}

/////////////////////////////////////
// wrapup code
/////////////////////////////////////

TASK_WRAPUP
{

}

TASK_CODE_END

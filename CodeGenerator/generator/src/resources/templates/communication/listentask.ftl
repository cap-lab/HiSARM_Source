/////////////////////////////////////
// include header section
/////////////////////////////////////

// ##DEFINE_SECTION::START
#include "logger.h"
#include "UFTimer.h"
#pragma pack(push, 1)
// ##DEFINE_SECTION::END

TASK_CODE_BEGIN

/////////////////////////////////////
// global definition
/////////////////////////////////////
// CHANNEL VARIABLE DEFINE
<#list channelPortMap as inPort, outPort>
    <#if inPort.variable.variableType.type.getValue() == "enum">
VARIABLE_${inPort.variable.variableType.name} ${inPort.variableName}[${inPort.variable.variableType.count}];
    <#else>
${inPort.variable.variableType.type.getValue()} ${inPort.variableName}[${inPort.variable.variableType.count}];
    </#if>
</#list>

// MULTICAST VARIABLE DEFINE
<#list multicastPortMap as multicastPort, channelPort>
    <#if multicastPort.variable.variableType.type.getValue() == "enum">
VARIABLE_${multicastPort.variable.variableType.name} ${multicastPort.variableName}[${multicastPort.variable.variableType.count}];
    <#else>
${multicastPort.variable.variableType.type.getValue()} ${multicastPort.variableName}[${multicastPort.variable.variableType.count}];
    </#if>
</#list>

// SHARED DATA VARIABLE DEFINE
<#list sharedDataPortMap as libPort, multicastPort>
    <#if libPort.variable.variableType.type.getValue() == "enum">
VARIABLE_${libPort.variable.variableType.name} ${libPort.library.name}[${libPort.variable.variableType.count}];
    <#else>
${libPort.variable.variableType.type.getValue()} ${libPort.library.name}[${libPort.variable.variableType.count}];
    </#if>
</#list>

// CHANNEL PORT SECTION
STATIC CHANNEL_PORT channelPortList[${channelPortMap?size}] = {
<#list channelPortMap as inPort, outPort>
    {"${inPort.name}", -1, "${outPort.name}", -1, ${inPort.variableName}, ${inPort.sampleSize}, FALSE}, 
</#list>
};

// MULTICAST PORT SECTION
STATIC MULTICAST_PORT multicastPortList[${multicastPortMap?size}] = {
<#list multicastPortMap as multicastPort, channelPort>
    {"${multicastPort.name}", -1, -1, "${channelPort.name}", -1, {{-1, -1}, ${multicastPort.variableName}}, ${multicastPort.size}, -1},
</#list>
};

// GROUP SERVICE PORT SECTION
STATIC GROUP_SERVICE_PORT sharedDataPortList[${sharedDataPortMap?size}] = {
<#list sharedDataPortMap as libPort, multicastPort>
    {"${multicastPort.name}", -1, -1, NULL, NULL, l_${libPort.library.name}_set_${libPort.library.name}_listen, {{-1, -1}, ${libPort.library.name}}, ${libPort.size}, -1},
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

TASK_INIT
{
	LOG_INFO("INIT");
    channelPortInit();
    multicastPortInit();
    libraryPortInit();
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
        		    sharedDataPortList[i].libSetFunc(sharedDataPortList[i].buffer.header.senderRobotId, sharedDataPortList[i].buffer.body);
        		    sharedDataPortList[i].beforeTime = sharedDataPortList[i].buffer.header.time;
         	 }
        }
    }
}

TASK_GO
{
    channelPortReceive();
    multicastPortReceive();
    groupServicePortReceive();
}

/////////////////////////////////////
// wrapup code
/////////////////////////////////////

TASK_WRAPUP
{

}

TASK_CODE_END

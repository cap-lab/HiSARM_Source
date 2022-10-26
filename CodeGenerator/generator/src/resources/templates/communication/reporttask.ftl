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
<#list channelPortMap as outPort, inPort>
    <#if outPort.variable.variableType.type.getValue() == "enum">
VARIABLE_${outPort.variable.variableType.name} ${outPort.variableName}[${outPort.variable.variableType.count}];
    <#else>
${outPort.variable.variableType.type.getValue()} ${outPort.variableName}[${outPort.variable.variableType.count}];
    </#if>
</#list>

// MULTICAST VARIABLE DEFINE
<#list multicastPortMap as multicastPort, channelPort>
    <#if multicastPort.variable.variableType.type.getValue() == "enum">
STATIC VARIABLE_${multicastPort.variable.variableType.name} ${multicastPort.variableName}[${multicastPort.variable.variableType.count}];
    <#else>
STATIC ${multicastPort.variable.variableType.type.getValue()} ${multicastPort.variableName}[${multicastPort.variable.variableType.count}];
    </#if>
</#list>

// SHARED DATA VARIABLE DEFINE
<#list sharedDataPortMap as libPort, multicastPort>
    <#if libPort.variable.variableType.type.getValue() == "enum">
STATIC VARIABLE_${libPort.variable.variableType.name} ${libPort.library.name}[${libPort.variable.variableType.count}];
    <#else>
STATIC ${libPort.variable.variableType.type.getValue()} ${libPort.library.name}[${libPort.variable.variableType.count}];
    </#if>
</#list>

// CHANNEL_PORT_SECTION
STATIC CHANNEL_PORT channelPortList[${channelPortMap?size}] = {
<#list channelPortMap as outPort, inPort>
    {"${inPort.name}", -1, "${outPort.name}", -1, ${outPort.variableName}, ${outPort.sampleSize}, FALSE}, 
</#list>
};

/ MULTICAST PORT SECTION
STATIC MULTICAST_PORT multicastPortList[${multicastPortMap?size}] = {
<#list multicastPortMap as multicastPort, channelPort>
    {"${multicastPort.name}", -1, -1, "${channelPort.name}", -1, {{-1, THIS_ROBOT_ID}, ${multicastPort.variableName}}, ${multicastPort.size}, -1},
</#list>
};

// GROUP SERVICE PORT SECTION
STATIC GROUP_SERVICE_PORT sharedDataPortList[${sharedDataPortMap?size}] = {
<#list sharedDataPortMap as libPort, multicastPort>
    {"${multicastPort.name}", -1, -1, l_${libPort.library.name}_avail_${libPort.library.name}_report, l_${libPort.library.name}_get_${libPort.library.name}_report, NULL, {{-1, THIS_ROBOT_ID}, ${libPort.library.name}}, ${libPort.size}, -1},
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
        if (channelPortRead(multicastPortList[i].channelPortId, multicastPortList[i].buffer.body, multicastPortList[i].size), FALSE) > 0) 
        {
            int dataLen;
        	UFTimer_GetCurrentTime(THIS_TASK_ID, &(multicastPortList[i].buffer.header.time));
        	UFMulticastPort_WriteToBuffer(multicastPortList[i].multicastGroupId, multicastPortList[i].multicastPortId, (unsigned char * ) &(multicastPortList[i].buffer), multicastPortList[i].size + sizeof(MULTICAST_PACKET_HEADER), &dataLen);
        }
    }
}

STATIC void groupServicePortSend() {
    for (int i = 0 ; i<sizeof(sharedDataPortList)/sizeof(GROUP_SERVICE_PORT) ; i++)
    {
        if (sharedDataPortList[i].libAvailFunc() == TRUE) 
        {
            int dataLen;
            sharedDataPortList[i].libGetFunc(sharedDataPortList[i].buffer.body);
            UFTimer_GetCurrentTime(THIS_TASK_ID, &(sharedDataPortList[i].buffer.header.time));
            UFMulticastPort_WriteToBuffer(sharedDataPortList[i].multicastGroupId, sharedDataPortList[i].multicastPortId, (unsigned char * ) &(sharedDataPortList[i].buffer), sharedDataPortList[i].size + sizeof(MULTICAST_PACKET_HEADER), &dataLen);
        }
    }
}

TASK_GO
{
    channelPortSend();
    multicastPortSend();
    groupServicePortSend();
}

/////////////////////////////////////
// wrapup code
/////////////////////////////////////

TASK_WRAPUP
{

}

TASK_CODE_END

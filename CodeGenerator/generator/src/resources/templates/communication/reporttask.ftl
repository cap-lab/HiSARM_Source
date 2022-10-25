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
<#list variableMap as teamName, variableList>
    <#list variableList as variable>
        <#if variable.variableBase.type.getValue() == "enum">
VARIABLE_${variable.variableBase.name} ${teamName}_${variable.name}[${variable.variableBase.count}]<#if variable.defalutValue??> = {${variable.defaultValue},}</#if>;
        <#else>
${variable.variableBase.type.getValue()} ${teamName}_${variable.name}[${variable.variableBase.count}]<#if variable.defalutValue??> = {${variable.defaultValue},}</#if>;
        </#if>
    </#list>
</#list>

// MULTICAST VARIABLE DEFINE
<#list multicastPortMap as multicastPort, channelPort>
    <#if multicastPort.variable.variableBase.type.getValue() == "enum">
VARIABLE_${multicastPort.variable.variableType.name} ${multicastPort.variableName}[${multicastPort.variable.variableType.count}];
    <#else>
${multicastPort.variable.variableType.type.getValue()} ${multicastPort.variableName}[${multicastPort.variable.variableType.count}];
    </#if>
</#list>

// SHARED DATA VARIABLE DEFINE
<#list sharedDataPortMap as libPort, multicastPort>
    <#if variable.variableBase.type.getValue() == "enum">
VARIABLE_${variable.variableBase.name} ${libPort.variableName}[${variable.variableBase.count}];
    <#else>
${variable.variableBase.type.getValue()} ${libPort.variableName}[${variable.variableBase.count}];
    </#if>
</#list>

// CHANNEL_PORT_SECTION
STATIC CHANNEL_PORT channelPortList[${channelPortMap?size}] = {
<#list channelPortMap as inPort, outPort>
    {"${inPort.name}", -1, "${outPort.name}", -1, ${inPort.counterTeamName}_${inPort.variable.name}, ${inPort.sampleSize}, FALSE}, 
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
    {"${multicastPort.name}", -1, -1, l_${libPort.library.name}_avail_${libPort.variable.name}_report, l_${libPort.library.name}_get_${libPort.variable.name}_report, NULL, {{-1, THIS_ROBOT_ID}, ${libPort.variableName}}, ${libPort.size}, -1},
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
        sharedDataPortList[i].libGetFunc(&${libPort.name}.header.targetRobotId, sharedDataPortList[i].buffer.body);
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

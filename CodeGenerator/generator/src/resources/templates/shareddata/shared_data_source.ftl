typedef struct _${libName} {
    int32 robot_id;
    <#if variablevariableType.type.value == "enum">VARIABLE_${variableType.name}<#else>${variableType.type.value}</#if> buffer[${variableType.count}];
    int32 size;
} VARIABLE_${libName};

VARIABLE_${libName} listen_${libName};
int8 ${libName}_listen_${libName}_updated;
STreadMutex listen_${libName}_mutex;

VARIABLE_${libName} action_${libName};
int8 action_${libName}_updated;
STreadMutex action_${libName}_mutex;


LIBFUNC(void, init, void) {
    listen_${libName}_updated = FALSE;
    action_${libName}_updated = FALSE;
    listen_${libName}.size = ${variableSize}
    action_${libName}.size = ${variableSize}
    UCTreadMutex_Create(&listen_${libName}_mutex);
    UCTreadMutex_Create(&action_${libName}_mutex);
}

LIBFUNC(void, wrapup, void) {
    UCTreadMutex_Destroy(&listen_${libName}_mutex);
    UCTreadMutex_Destroy(&action_${libName}_mutex);
}

LIBFUNC(void, set_${libName}_listen, int32 sender_robot, void *buffer) {
    UCTreadMutex_Lock(listen_${libName}_mutex);
    listen_${libName}_updated = TRUE;
    listen_${libName}.robot_id = sender_robot;
    memcpy(listen_${libName}.buffer, buffer, listen_${libName}.size);
    UCTreadMutex_UnLock(listen_${libName}_mutex);
}

LIBFUNC(void, get_${libName}_action, int32 *sender_robot, void *buffer) {
    UCTreadMutex_Lock(listen_${libName}_mutex);
    listen_${libName}_updated = FALSE;
    *sender_robot = listen_${libName}.robot_id;
    memcpy(buffer, listen_${libName}.buffer, listen_${libName}.size);
    UCTreadMutex_UnLock(listen_${libName}_mutex);
}

LIBFUNC(int8, avail_${libName}_action) {
    return listen_${libName}_updated;
}

LIBFUNC(void, set_${libName}_action, void *buffer) {
    UCTreadMutex_Lock(action_${libName}_mutex);
    action_${libName}_updated = TRUE;
    memcpy(action_${libName}.buffer, buffer, action_${libName}.size);
    UCTreadMutex_UnLock(action_${libName}_mutex);
}

LIBFUNC(void, get_${variable.name}_report, void *buffer) {
    UCTreadMutex_Lock(action_${libName}_mutex);
    action_${libName}_updated = FALSE;
    memcpy(buffer, action_${libName}.buffer, action_${libName}.size);
    UCTreadMutex_UnLock(action_${libName}_mutex);
}

LIBFUNC(int8, avail_${libName}_report) {
    return  action_${libName}_updated;
}
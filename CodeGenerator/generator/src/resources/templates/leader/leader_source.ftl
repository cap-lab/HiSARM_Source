typedef struct _SEMO_LEADER {
    GROUP group_id;
    LEADER_SELECTION_STATE leader_selection_state;
    int32 leader_id;
    int64 last_time;

    int32 listen_robot_id;
    int8 listen_robot_id_refreshed;
    STreadMutex listen_robot_id_mutex;
    int32 listen_heartbeat;
    int8 listen_heartbeat_refreshed;
    STreadMutex listen_heartbeat_mutex;

    int32 leader_robot_id;
    int8 leader_robot_id_refreshed;
    STreadMutex leader_robot_id_mutex;
    int32 leader_heartbeat;
    int8 leader_heartbeat_refreshed;
    STreadMutex leader_heartbeat_mutex;

} SEMO_LEADER;

STATIC SEMO_LEADER leader_list[${groupList?size}] = {
<#list groupList as group>
    {ID_GROUP_${group}, LEADER_SELECTION_STOP, -1},
</#list>
};

LIBFUNC(void, init, void) {
    int32 i;
    for (i = 0; i < ${groupList?size}; i++) {
        UCTreadMutex_Create($leader_list[i].listen_robot_id_mutex);
        UCTreadMutex_Create($leader_list[i].listen_heartbeat_mutex);
        UCTreadMutex_Create($leader_list[i].leader_robot_id_mutex);
        UCTreadMutex_Create($leader_list[i].leader_heartbeat_mutex);
    }
}


STATIC SEMO_LEADER* find_leader_struct(int32 group_id) {
    int32 i;
    for (i = 0; i < ${groupList?size}; i++) {
        if (leader_list[i].group_id == group_id) {
            return &leader_list[i];
        }
    }
    return NULL;
}

LIBFUNC(void, set_robot_id_listen, int32 group_id, int32 robot_id, int64 time) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        UCTreadMutex_Lock(leader->listen_robot_id_mutex);
        if (leader->listen_robot_id_refreshed == FALSE) {
            leader->listen_robot_id = robot_id;
            leader->listen_robot_id_refreshed = TRUE;
        } else {
            if (leader->listen_robot_id > robot_id) {
                leader->listen_robot_id = robot_id;
            }
        }
        UCTreadMutex_Unlock(leader->listen_robot_id_mutex);
    }
}
LIBFUNC(int8, avail_robot_id_leader, int32 group_id) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        return leader->listen_robot_id_refreshed;
    }
    return FALSE;
}
LIBFUNC(int32, get_robot_id_leader, int32 group_id) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    int32 robot_id = -1;
    if (leader != NULL) {
        UCTreadMutex_Lock(leader->leader_robot_id_mutex);
        robot_id = leader->leader_robot_id;
        leader->listen_robot_id_refreshed = FALSE;
        UCTreadMutex_Unlock(leader->leader_robot_id_mutex);
    }
    return robot_id;
}
LIBFUNC(void, set_robot_id_leader, int32 group_id, int32 robot_id, int64 time) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        UCTreadMutex_Lock(leader->leader_robot_id_mutex);
        leader->leader_robot_id = robot_id;
        leader->leader_robot_id_refreshed = TRUE;
        UCTreadMutex_Unlock(leader->leader_robot_id_mutex);
    }
}
LIBFUNC(int8, avail_robot_id_report, int32 group_id) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        return leader->leader_robot_id_refreshed;
    }
    return FALSE;
}
LIBFUNC(void, get_robot_id_report, int32 group_id){
    SEMO_LEADER* leader = find_leader_struct(group_id);
    int32 robot_id = -1;
    if (leader != NULL) {
        UCTreadMutex_Lock(leader->leader_robot_id_mutex);
        robot_id = leader->leader_robot_id;
        leader->leader_robot_id_refreshed = FALSE;
        UCTreadMutex_Unlock(leader->leader_robot_id_mutex);
    }
    return robot_id;
}

LIBFUNC(void, set_heartbeat_listen, int32 group_id, int32 heartbeat, int64 time) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        UCTreadMutex_Lock(leader->listen_heartbeat_mutex);
        if (leader->listen_heartbeat_refreshed == FALSE) {
            leader->listen_heartbeat = heartbeat;
            leader->listen_heartbeat_refreshed = TRUE;
        } else {
            if (leader->listen_heartbeat > heartbeat) {
                leader->listen_heartbeat = heartbeat;
            }
        }
        UCTreadMutex_Unlock(leader->listen_heartbeat_mutex);
    }
}
LIBFUNC(int8, avail_heartbeat_leader, int32 group_id) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        return leader->listen_heartbeat_refreshed;
    }
    return FALSE;
}
LIBFUNC(int32, get_heartbeat_leader, int32 group_id) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    int32 heartbeat = -1;
    if (leader != NULL) {
        UCTreadMutex_Lock(leader->leader_heartbeat_mutex);
        heartbeat = leader->leader_heartbeat;
        leader->listen_heartbeat_refreshed = FALSE;
        UCTreadMutex_Unlock(leader->leader_heartbeat_mutex);
    }
    return heartbeat;
}
LIBFUNC(void, set_heartbeat_leader, int32 group_id, int32 heartbeat, int64 time) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        UCTreadMutex_Lock(leader->leader_heartbeat_mutex);
        leader->leader_heartbeat = heartbeat;
        leader->leader_heartbeat_refreshed = TRUE;
        UCTreadMutex_Unlock(leader->leader_heartbeat_mutex);
    }
}
LIBFUNC(int8, avail_heartbeat_report, int32 group_id) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        return leader->leader_heartbeat_refreshed;
    }
    return FALSE;
}
LIBFUNC(int32, get_heartbeat_report, int32 group_id) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    int32 heartbeat = -1;
    if (leader != NULL) {
        UCTreadMutex_Lock(leader->leader_heartbeat_mutex);
        heartbeat = leader->leader_heartbeat;
        leader->leader_heartbeat_refreshed = FALSE;
        UCTreadMutex_Unlock(leader->leader_heartbeat_mutex);
    }
    return heartbeat;
}

LIBFUNC(void, set_leader_selection_state, GROUP group_id, LEADER_SELECTION_STATE state) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        leader->leader_selection_state = state;
    }
}
LIBFUNC(LEADER_SELECTION_STATE, get_leader_selection_state, GROUP group_id) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        return leader->leader_selection_state;
    }
    return -1;
}

LIBFUNC(int32, get_leader, GROUP group_id) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        return leader->leader_id;
    }
    return -1;
}
LIBFUNC(void, set_leader, GROUP group_id, int32 robot_id) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        leader->leader_id = robot_id;
    }
}

LIBFUNC(int64, get_last_time, int32 group_id) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        return leader->last_time;
    }
    return -1;
}
LIBFUNC(void, set_last_time, int32 group_id, int64 time) {
    SEMO_LEADER* leader = find_leader_struct(group_id);
    if (leader != NULL) {
        leader->last_time = time;
    }
}
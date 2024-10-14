package com.dbmanager.datastructure.action;

import java.util.ArrayList;
import java.util.List;

public class GroupAction {
    private List<String> sharedDataList = new ArrayList<>();
    private boolean synchronization = false;

    public GroupAction() {
        sharedDataList = new ArrayList<String>();
    }

    public List<String> getSharedDataList() {
        return sharedDataList;
    }

    public void setSharedDataList(List<String> sharedDataList) {
        this.sharedDataList = sharedDataList;
    }

    public boolean isSynchronization() {
        return synchronization;
    }

    public void setSynchronization(boolean synchronization) {
        this.synchronization = synchronization;
    }
}

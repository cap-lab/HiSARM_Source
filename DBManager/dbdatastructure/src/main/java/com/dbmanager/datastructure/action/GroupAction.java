package com.dbmanager.datastructure.action;

import java.util.ArrayList;
import java.util.List;

public class GroupAction extends Action {
    private List<String> sharedDataList = new ArrayList<>();

    public GroupAction() {
        super();
        sharedDataList = new ArrayList<String>();
    }

    public List<String> getSharedDataList() {
        return sharedDataList;
    }

    public void setSharedDataList(List<String> sharedDataList) {
        this.sharedDataList = sharedDataList;
    }
}

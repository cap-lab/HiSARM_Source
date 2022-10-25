package com.metadata.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.metadata.algorithm.task.UEMTask;

public class UEMTaskGraph {
    private int level;
    private List<UEMTask> taskList = new ArrayList<>();
    private List<UEMChannel> channelList = new ArrayList<>();
    private List<UEMLibrary> libraryList = new ArrayList<>();
    private List<UEMLibraryConnection> libraryConnectionList = new ArrayList<>();

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<UEMTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<UEMTask> taskList) {
        this.taskList = taskList;
    }

    public List<UEMChannel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<UEMChannel> channelList) {
        this.channelList = channelList;
    }

    public List<UEMLibrary> getLibraryList() {
        return libraryList;
    }

    public void setLibraryList(List<UEMLibrary> libraryList) {
        this.libraryList = libraryList;
    }

    public List<UEMLibraryConnection> getLibraryConnectionList() {
        return libraryConnectionList;
    }

    public void setLibraryConnectionList(List<UEMLibraryConnection> libraryConnectionList) {
        this.libraryConnectionList = libraryConnectionList;
    }

}

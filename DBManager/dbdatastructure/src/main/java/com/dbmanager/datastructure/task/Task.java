package com.dbmanager.datastructure.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Task {
    private String taskId;
    private String CICFile;
    private Time deadline;
    private Time period;
    private int priority;
    private String language;
    private String compileFlags;
    private String linkFlags;
    private boolean hasSubGraph;
    private String RunCondition;
    private ChannelPort groupPort;
    private LibraryPort leaderPort;
    private List<ExtraSetting> extraSettings = new ArrayList<>();
    private Set<ChannelPort> channelPortSet = new HashSet<>();
    private Set<LibraryPort> libraryPortSet = new HashSet<>();
    private Set<PortMap> portMapSet = new HashSet<>();
    private Set<TaskFile> taskFiles = new HashSet<>();

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCICFile() {
        return CICFile;
    }

    public void setCICFile(String file) {
        this.CICFile = file;
    }

    public Time getDeadline() {
        return deadline;
    }

    public void setDeadline(Time deadline) {
        this.deadline = deadline;
    }

    public Time getPeriod() {
        return period;
    }

    public void setPeriod(Time period) {
        this.period = period;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCompileFlags() {
        return compileFlags;
    }

    public void setCompileFlags(String compileFlags) {
        this.compileFlags = compileFlags;
    }

    public String getLinkFlags() {
        return linkFlags;
    }

    public void setLinkFlags(String linkFlags) {
        this.linkFlags = linkFlags;
    }

    public boolean isHasSubGraph() {
        return hasSubGraph;
    }

    public void setHasSubGraph(boolean hasSubGraph) {
        this.hasSubGraph = hasSubGraph;
    }

    public String getRunCondition() {
        return RunCondition;
    }

    public void setRunCondition(String runCondition) {
        RunCondition = runCondition;
    }

    public List<ExtraSetting> getExtraSettings() {
        return extraSettings;
    }

    public void setExtraSettings(List<ExtraSetting> extraSettings) {
        this.extraSettings = extraSettings;
    }

    public Set<ChannelPort> getChannelPortSet() {
        return channelPortSet;
    }

    public void setChannelPortSet(Set<ChannelPort> channelPortSet) {
        this.channelPortSet = channelPortSet;
    }

    public Set<LibraryPort> getLibraryPortSet() {
        return libraryPortSet;
    }

    public void setLibraryPortSet(Set<LibraryPort> libraryPortSet) {
        this.libraryPortSet = libraryPortSet;
    }

    public Set<PortMap> getPortMapSet() {
        return portMapSet;
    }

    public void setPortMapSet(Set<PortMap> portMapSet) {
        this.portMapSet = portMapSet;
    }

    public Set<TaskFile> getTaskFiles() {
        return taskFiles;
    }

    public void setTaskFiles(Set<TaskFile> taskFiles) {
        this.taskFiles = taskFiles;
    }

    public ChannelPort getGroupPort() {
        return groupPort;
    }

    public void setGroupPort(ChannelPort groupPort) {
        this.groupPort = groupPort;
    }

    public LibraryPort getLeaderPort() {
        return leaderPort;
    }

    public void setLeaderPort(LibraryPort leaderPort) {
        this.leaderPort = leaderPort;
    }
}

package com.dbmanager.datastructure.groupingalgorithm;

public class GroupingAlgorithm {
    private String groupingId;
    private String compileTimeFile;
    private String compileTimeClass;
    private String runTimeTask;
    private int sharedDataSize;

    public String getGroupingId() {
        return groupingId;
    }

    public void setGroupingId(String groupingId) {
        this.groupingId = groupingId;
    }

    public String getCompileTimeFile() {
        return compileTimeFile;
    }

    public void setCompileTimeFile(String compileTimeFile) {
        this.compileTimeFile = compileTimeFile;
    }

    public String getRunTimeTask() {
        return runTimeTask;
    }

    public void setRunTimeTask(String runTimeTask) {
        this.runTimeTask = runTimeTask;
    }

    public int getSharedDataSize() {
        return sharedDataSize;
    }

    public void setSharedDataSize(int sharedDataSize) {
        this.sharedDataSize = sharedDataSize;
    }

    public String getCompileTimeClass() {
        return compileTimeClass;
    }

    public void setCompileTimeClass(String compileTimeClass) {
        this.compileTimeClass = compileTimeClass;
    }
}

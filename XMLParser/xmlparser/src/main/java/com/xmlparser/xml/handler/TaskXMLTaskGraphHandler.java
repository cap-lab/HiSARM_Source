package com.xmlparser.xml.handler;

import java.com.xmlparser.xml.TaskXMLTaskGraphTypeLoader;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import com.xmlparser.TaskXMLTaskGraphType;

public class TaskXMLTaskGraphHandler extends XMLHandler {
    private TaskXMLTaskGraphTypeLoader loader;
    private TaskXMLTaskGraphType taskgraph;

    public TaskXMLTaskGraphHandler() {
        loader = new TaskXMLTaskGraphTypeLoader();
        taskgraph = new TaskXMLTaskGraphType();
    }

    @Override
    protected void storeResource(StringWriter writer) throws Exception {
        loader.storeResource(taskgraph, writer);
    }

    @Override
    protected void loadResource(ByteArrayInputStream is) throws Exception {
        taskgraph = loader.loadResource(is);
    }

    @Override
    public void init() {

    }

    public TaskXMLTaskGraphType getTaskGraph() {
        return taskgraph;
    }

}

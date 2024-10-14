package com.xmlparser.xml.handler;

import com.xmlparser.xml.TaskXMLTaskTypeLoader;
import com.xmlparser.xml.handler.XMLHandler;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import com.xmlparser.TaskXMLTaskType;

public class TaskXMLTaskHandler extends XMLHandler {
    private TaskXMLTaskTypeLoader loader;
    private TaskXMLTaskType task;

    public TaskXMLTaskHandler() {
        loader = new TaskXMLTaskTypeLoader();
        task = new TaskXMLTaskType();
    }

    @Override
    protected void storeResource(StringWriter writer) throws Exception {
        loader.storeResource(task, writer);
    }

    @Override
    protected void loadResource(ByteArrayInputStream is) throws Exception {
        task = loader.loadResource(is);
    }

    @Override
    public void init() {

    }

}

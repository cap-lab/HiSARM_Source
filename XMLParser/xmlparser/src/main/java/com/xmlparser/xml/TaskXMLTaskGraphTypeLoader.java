package com.xmlparser.xml;

import javax.xml.bind.JAXBElement;
import com.xmlparser.TaskXMLTaskGraphType;

public class TaskXMLTaskGraphTypeLoader extends ResourceLoader<TaskXMLTaskGraphType> {

	@Override
	protected String getSchemaFile() {
		return "UEM_TaskGraph.xsd";
	}

	@Override
	protected JAXBElement<TaskXMLTaskGraphType> getJAXBElement(TaskXMLTaskGraphType resource) {
		return OBJECT_FACTORY.createCicManual(resource);
	}

	@Override
	public TaskXMLTaskGraphType createResource(String name) {
		return OBJECT_FACTORY.createTaskXMLTaskGraphType();
	}

}

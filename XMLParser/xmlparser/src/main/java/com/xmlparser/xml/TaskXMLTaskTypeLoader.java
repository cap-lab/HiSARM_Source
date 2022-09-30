package java.com.xmlparser.xml;

import javax.xml.bind.JAXBElement;
import com.xmlparser.TaskXMLTaskType;

public class TaskXMLTaskTypeLoader extends ResourceLoader<TaskXMLTaskType> {

	@Override
	protected String getSchemaFile() {
		return "TaskXML_Task.xsd";
	}

	@Override
	protected JAXBElement<TaskXMLTaskType> getJAXBElement(TaskXMLTaskType resource) {
		return OBJECT_FACTORY.createTaskXMLTask(resource);
	}

	@Override
	public TaskXMLTaskType createResource(String name) {
		return OBJECT_FACTORY.createTaskXMLTaskType();
	}

}

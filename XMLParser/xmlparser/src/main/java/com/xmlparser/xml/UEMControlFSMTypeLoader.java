package java.com.xmlparser.xml;

import javax.xml.bind.JAXBElement;
import com.xmlparser.UEMControlFSMType;

public class UEMControlFSMTypeLoader extends ResourceLoader<UEMControlFSMType> {

	@Override
	protected String getSchemaFile() {
		return "UEM_ControlFSM.xsd";
	}

	@Override
	protected JAXBElement<UEMControlFSMType> getJAXBElement(UEMControlFSMType resource) {
		return OBJECT_FACTORY.createFsms(resource);
	}

	@Override
	public UEMControlFSMType createResource(String name) {
		return OBJECT_FACTORY.createUEMControlFSMType();
	}

}

package com.dbmanager.commonlibraries.Mapper;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.architecture.Architecture;
import com.dbmanager.datastructure.architecture.EnvironmentVariable;
import com.dbmanager.datastructure.architecture.Memory;
import com.dbmanager.datastructure.architecture.Processor;

public class ArchitectureMapper {
    private static List<Processor> mapToProcessor(List<Document> documentList) {
        List<Processor> processorList = new ArrayList<Processor>();

        for (Document document : documentList) {
            Processor processor = new Processor();
            processor.setProcessorName(document.getString("name"));
            processor.setType(document.getString("type"));
            processor.setNumberOfCores(document.getInteger("numberOfCores"));
            processor.setClockFrequency(document.getInteger("clockFrequency"));
            processorList.add(processor);
        }

        return processorList;
    }

    private static Memory mapToMemory(Document document) {
        Memory memory = new Memory();

        memory.setSize(document.getInteger("size"));
        memory.setUnit(document.getString("unit"));

        return memory;
    }

    private static List<EnvironmentVariable> mapToEnvironmentVariable(List<Document> documentList) {
        List<EnvironmentVariable> environmentVariableList = new ArrayList<EnvironmentVariable>();

        for (Document document : documentList) {
            EnvironmentVariable environmentVariable = new EnvironmentVariable();
            environmentVariable.setName(document.getString("name"));
            environmentVariable.setValue(document.getString("value"));
            environmentVariableList.add(environmentVariable);
        }

        return environmentVariableList;
    }

    public static Architecture mapToArchitecture(Bson bson) {
        Architecture architecture = new Architecture();
        Document document = (Document) bson;

        try {
            architecture.setDeviceName(document.getString("DeviceName"));
            architecture.setOS(document.getString("OS"));
            architecture.setCPU(document.getString("CPU"));
            architecture
                    .setProcessors(mapToProcessor(document.getList("Processor", Document.class)));
            architecture.setMemory(mapToMemory((Document) document.get("Memory")));
            architecture.setModuleList(document.getList("Modules", String.class));
            architecture.setEnvironmentVariables(mapToEnvironmentVariable(
                    document.getList("EnvironmentVariable", Document.class)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return architecture;
    }
}

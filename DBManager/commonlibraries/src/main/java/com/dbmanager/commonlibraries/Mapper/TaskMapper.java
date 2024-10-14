package com.dbmanager.commonlibraries.Mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.common.FileItem;
import com.dbmanager.datastructure.task.ChannelPort;
import com.dbmanager.datastructure.task.CommunicationType;
import com.dbmanager.datastructure.task.ExtraSetting;
import com.dbmanager.datastructure.task.LibraryPort;
import com.dbmanager.datastructure.task.PortDirection;
import com.dbmanager.datastructure.task.PortMap;
import com.dbmanager.datastructure.task.ResourcePort;
import com.dbmanager.datastructure.task.Task;
import com.dbmanager.datastructure.task.Time;

public class TaskMapper {
    private static ResourcePort mapToResourcePort(Document document) {
        ResourcePort resourcePort = new ResourcePort();

        resourcePort.setName(document.getString("name"));
        resourcePort
                .setDirection(PortDirection.valueOf(document.getString("direction").toUpperCase()));

        return resourcePort;
    }

    private static ChannelPort mapToChannelPort(Document document) {
        ChannelPort channelPort = new ChannelPort();

        channelPort.setName(document.getString("name"));
        channelPort
                .setDirection(PortDirection.valueOf(document.getString("direction").toUpperCase()));
        channelPort.setSampleSize(document.getInteger("sampleSize"));
        channelPort.setIndex(document.getInteger("index"));

        return channelPort;
    }

    private static LibraryPort mapToLibraryPort(Document document) {
        LibraryPort libraryPort = new LibraryPort();

        libraryPort.setName(document.getString("name"));
        libraryPort.setIndex(document.getInteger("index"));

        return libraryPort;
    }

    private static Set<ChannelPort> makeChannelPortSet(List<Document> documentList) {
        Set<ChannelPort> channelPortSet = new HashSet<ChannelPort>();
        documentList.stream()
                .filter(doc -> doc.getString("type").equals(CommunicationType.CHANNEL.getValue()))
                .forEach(doc -> channelPortSet.add(mapToChannelPort(doc)));
        return channelPortSet;
    }

    private static Set<ResourcePort> makeResourcePortSet(List<Document> documentList) {
        Set<ResourcePort> resourcePortSet = new HashSet<ResourcePort>();
        documentList.stream()
                .filter(doc -> doc.getString("type").equals(CommunicationType.RESOURCE.getValue()))
                .forEach(doc -> resourcePortSet.add(mapToResourcePort(doc)));
        return resourcePortSet;
    }

    private static Set<LibraryPort> makeLibraryPortSet(List<Document> documentList) {
        Set<LibraryPort> libraryPortSet = new HashSet<LibraryPort>();
        documentList.stream()
                .filter(doc -> doc.getString("type").equals(CommunicationType.LIBRARY.getValue()))
                .forEach(doc -> libraryPortSet.add(mapToLibraryPort(doc)));
        return libraryPortSet;
    }

    private static ChannelPort makeGroupPort(List<Document> documentList) {
        ChannelPort groupPort = null;
        if (documentList.stream()
                .filter(doc -> doc.getString("type").equals(CommunicationType.GROUP.getValue()))
                .findFirst().isPresent()) {
            groupPort = new ChannelPort();
            groupPort.setDirection(PortDirection.IN);
            groupPort.setName(CommunicationType.GROUP.getValue());
            groupPort.setSampleSize(4);
        }
        return groupPort;
    }

    private static LibraryPort makeLeaderPort(List<Document> documentList) {
        LibraryPort leaderPort = null;
        if (documentList.stream()
                .filter(doc -> doc.getString("type").equals(CommunicationType.LEADER.getValue()))
                .findFirst().isPresent()) {
            leaderPort = new LibraryPort();
            leaderPort.setName(CommunicationType.LEADER.getValue());
        }
        return leaderPort;
    }

    private static LibraryPort makeSimulationPort(List<Document> documentList) {
        LibraryPort simulationPort = null;
        if (documentList.stream().filter(
                doc -> doc.getString("type").equals(CommunicationType.SIMULATION.getValue()))
                .findFirst().isPresent()) {
            simulationPort = new LibraryPort();
            simulationPort.setName(CommunicationType.SIMULATION.getValue());
        }
        return simulationPort;
    }

    private static Set<FileItem> makeTaskFileSet(List<Document> documentList) {
        Set<FileItem> taskfiles = new HashSet<FileItem>();
        documentList.forEach(doc -> {
            FileItem taskFile = new FileItem();
            taskFile.setDirectory(doc.getBoolean("isDirectory"));
            taskFile.setPath(doc.getString("path"));
            taskfiles.add(taskFile);
        });
        return taskfiles;
    }

    private static Time makeTime(Document document) {
        Time time = new Time();
        time.setUnit(document.getString("unit").toLowerCase());
        time.setTime(document.getInteger("time"));
        return time;
    }

    private static List<ExtraSetting> makeExtraSetting(List<Document> documentList) {
        List<ExtraSetting> extraSettings = new ArrayList<ExtraSetting>();
        documentList.forEach(doc -> {
            ExtraSetting extraSetting = new ExtraSetting();
            extraSetting.setType(ExtraSetting.ExtraSettingType.valueFrom(doc.getString("type")));
            extraSetting.setName(doc.getString("name"));
            extraSettings.add(extraSetting);
        });
        return extraSettings;
    }

    private static Set<PortMap> makePortMapSet(List<Document> documentList) {
        Set<PortMap> portMapSet = new HashSet<PortMap>();

        documentList.forEach(doc -> {
            PortMap portMap = new PortMap();
            portMap.setOutsidePort(doc.getString("outsidePort"));
            portMap.setInsideTask(doc.getString("insideTask"));
            portMap.setInsidePort(doc.getString("insidePort"));
            portMap.setDirection(PortDirection.valueOf(doc.getString("direction").toUpperCase()));
            portMapSet.add(portMap);
        });

        return portMapSet;
    }

    public static Task mapToTask(Bson bson) {
        Document document = (Document) bson;
        Task task = new Task();

        try {
            task.setTaskId(document.getString("TaskId"));
            task.setCICFile(document.getString("CICFile"));
            task.setDeadline(makeTime((Document) document.get("Deadline")));
            task.setPeriod(makeTime((Document) document.get("Period")));
            task.setPriority(document.getInteger("Priority"));
            task.setLanguage(document.getString("Language"));
            task.setCompileFlags(document.getString("CompileFlags"));
            task.setLinkFlags(document.getString("LinkFlags"));
            task.setHasSubGraph(document.getBoolean("HasSubGraph"));
            task.setRunCondition(document.getString("RunCondition"));
            task.setExtraSettings(makeExtraSetting((List<Document>) document.get("ExtraSetting")));
            task.setChannelPortSet(
                    makeChannelPortSet((List<Document>) document.get("Communication")));
            task.setResourcePortSet(
                    makeResourcePortSet((List<Document>) document.get("Communication")));
            task.setLibraryPortSet(
                    makeLibraryPortSet((List<Document>) document.get("Communication")));
            task.setGroupPort(makeGroupPort((List<Document>) document.get("Communication")));
            task.setLeaderPort(makeLeaderPort((List<Document>) document.get("Communication")));
            task.setSimulationPort(
                    makeSimulationPort((List<Document>) document.get("Communication")));
            task.setPortMapSet(makePortMapSet((List<Document>) document.get("PortMap")));
            task.setTaskFiles(makeTaskFileSet((List<Document>) document.get("File")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

}

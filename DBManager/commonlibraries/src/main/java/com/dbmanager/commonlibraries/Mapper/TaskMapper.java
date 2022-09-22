package com.dbmanager.commonlibraries.Mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.task.ChannelPort;
import com.dbmanager.datastructure.task.CommunicationType;
import com.dbmanager.datastructure.task.ExtraSetting;
import com.dbmanager.datastructure.task.LibraryPort;
import com.dbmanager.datastructure.task.MulticastPort;
import com.dbmanager.datastructure.task.PortDirection;
import com.dbmanager.datastructure.task.PortMap;
import com.dbmanager.datastructure.task.SysRequest;
import com.dbmanager.datastructure.task.Task;
import com.dbmanager.datastructure.task.TaskFile;
import com.dbmanager.datastructure.task.Time;

public class TaskMapper {
    private static ChannelPort mapToChannelPort(Document document) {
        ChannelPort channelPort = new ChannelPort();

        channelPort.setName(document.getString("name"));
        channelPort
                .setDirection(PortDirection.valueOf(document.getString("direction").toUpperCase()));
        channelPort.setSampleSize(document.getInteger("sampleSize"));

        return channelPort;
    }

    private static MulticastPort mapToMulticastPort(Document document) {
        MulticastPort multicastPort = new MulticastPort();

        return multicastPort;
    }

    private static LibraryPort mapToLibraryPort(Document document) {
        LibraryPort libraryPort = new LibraryPort();

        libraryPort.setName(document.getString("name"));

        return libraryPort;
    }

    private static SysRequest mapToSysRequest(Document document) {
        SysRequest sysRequest = new SysRequest();

        sysRequest.setName(document.getString("name"));
        sysRequest.setType(document.getString("type"));
        sysRequest.setValue(document.getDouble("value"));

        return sysRequest;
    }

    private static Set<ChannelPort> makeChannelPortSet(List<Document> documentList) {
        Set<ChannelPort> channelPortSet = new HashSet<ChannelPort>();
        documentList.stream()
                .filter(doc -> doc.getString("type").equals(CommunicationType.CHANNEL.getValue()))
                .forEach(doc -> channelPortSet.add(mapToChannelPort(doc)));
        return channelPortSet;
    }

    private static Set<MulticastPort> makeMulticastPortSet(List<Document> documentList) {
        Set<MulticastPort> multicastPortSet = new HashSet<MulticastPort>();
        documentList.stream()
                .filter(doc -> doc.getString("type").equals(CommunicationType.MULTICAST.getValue()))
                .forEach(doc -> multicastPortSet.add(mapToMulticastPort(doc)));
        return multicastPortSet;
    }

    private static Set<LibraryPort> makeLibraryPortSet(List<Document> documentList) {
        Set<LibraryPort> libraryPortSet = new HashSet<LibraryPort>();
        documentList.stream()
                .filter(doc -> doc.getString("type").equals(CommunicationType.LIBRARY.getValue()))
                .forEach(doc -> libraryPortSet.add(mapToLibraryPort(doc)));
        return libraryPortSet;
    }

    private static Set<SysRequest> makeSysRequestSet(List<Document> documentList) {
        Set<SysRequest> sysRequestSet = new HashSet<SysRequest>();
        documentList.stream().filter(
                doc -> doc.getString("type").equals(CommunicationType.SYSREQUEST.getValue()))
                .forEach(doc -> sysRequestSet.add(mapToSysRequest(doc)));
        return sysRequestSet;
    }

    private static Set<TaskFile> makeTaskFileSet(List<Document> documentList) {
        Set<TaskFile> taskfiles = new HashSet<TaskFile>();
        documentList.forEach(doc -> {
            TaskFile taskFile = new TaskFile();
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
            task.setMulticastPortSet(
                    makeMulticastPortSet((List<Document>) document.get("Communication")));
            task.setLibraryPortSet(
                    makeLibraryPortSet((List<Document>) document.get("Communication")));
            task.setSysRequestSet(
                    makeSysRequestSet((List<Document>) document.get("Communication")));
            task.setPortMapSet(makePortMapSet((List<Document>) document.get("PortMap")));
            task.setTaskFiles(makeTaskFileSet((List<Document>) document.get("File")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

}

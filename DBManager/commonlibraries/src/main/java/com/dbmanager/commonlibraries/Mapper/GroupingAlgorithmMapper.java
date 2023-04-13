package com.dbmanager.commonlibraries.Mapper;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.common.FileItem;
import com.dbmanager.datastructure.groupingalgorithm.GroupingAlgorithm;

public class GroupingAlgorithmMapper {
    private static List<FileItem> makeTaskFileList(List<Document> documentList) {
        List<FileItem> taskfiles = new ArrayList<FileItem>();
        documentList.forEach(doc -> {
            FileItem taskFile = new FileItem();
            taskFile.setDirectory(doc.getBoolean("isDirectory"));
            taskFile.setPath(doc.getString("path"));
            taskfiles.add(taskFile);
        });
        return taskfiles;
    }

    public static GroupingAlgorithm mapToGroupingAlgorithm(Bson bson) {
        GroupingAlgorithm groupingAlgorithm = new GroupingAlgorithm();
        Document document = (Document) bson;

        try {
            groupingAlgorithm.setGroupingId(document.getString("GroupingId"));
            groupingAlgorithm.setCompileTimeFile(document.getString("CompileTimeFile"));
            groupingAlgorithm.setCompileTimeClass(document.getString("CompileTimeClass"));
            groupingAlgorithm.setRunTimeTask(document.getString("RunTimeTask"));
            groupingAlgorithm.setSharedDataSize(document.getInteger("SharedDataSize"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return groupingAlgorithm;
    }
}

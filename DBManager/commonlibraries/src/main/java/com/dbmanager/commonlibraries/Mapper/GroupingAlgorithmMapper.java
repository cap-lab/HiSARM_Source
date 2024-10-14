package com.dbmanager.commonlibraries.Mapper;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.groupingalgorithm.GroupingAlgorithm;

public class GroupingAlgorithmMapper {
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

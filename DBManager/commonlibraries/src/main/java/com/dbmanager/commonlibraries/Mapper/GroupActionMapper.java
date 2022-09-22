package com.dbmanager.commonlibraries.Mapper;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.action.GroupAction;

public class GroupActionMapper {
    public static GroupAction mapToGroupAction(Bson bson) {
        GroupAction action = new GroupAction();
        Document document = (Document) bson;

        try {
            action.setName(document.getString("Name"));
            action.setDescription(document.getString("Description"));
            action.setInputList(document.getList("Input", String.class));
            action.setOutputList(document.getList("Output", String.class));
            action.setSharedDataList(document.getList("SharedData", String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return action;
    }
}

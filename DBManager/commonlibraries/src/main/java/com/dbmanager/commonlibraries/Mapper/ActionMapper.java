package com.dbmanager.commonlibraries.Mapper;

import java.util.ArrayList;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.action.Action;
import com.dbmanager.datastructure.action.GroupAction;

public class ActionMapper {

    private static GroupAction makeGroupAction(Document document) {
        GroupAction groupAction = new GroupAction();
        groupAction.setSharedDataList(
                document.getList("sharedDataList", String.class, new ArrayList<String>()));
        groupAction.setSynchronization(document.getBoolean("synchronization"));
        return groupAction;
    }

    public static Action mapToAction(Bson bson) {
        Action action = new Action();
        Document document = (Document) bson;

        try {
            action.setName(document.getString("Name"));
            action.setDescription(document.getString("Description"));
            action.setInputList(document.getList("Input", String.class));
            action.setOutputList(document.getList("Output", String.class));
            if (document.containsKey("Swarm") == true) {
                action.setGroupAction(makeGroupAction(document.get("Swarm", Document.class)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return action;
    }
}

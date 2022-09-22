package com.dbmanager.commonlibraries.Mapper;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.action.Action;

public class ActionMapper {
    public static Action mapToAction(Bson bson) {
        Action action = new Action();
        Document document = (Document) bson;

        try {
            action.setName(document.getString("Name"));
            action.setDescription(document.getString("Description"));
            action.setInputList(document.getList("Input", String.class));
            action.setOutputList(document.getList("Output", String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return action;
    }
}

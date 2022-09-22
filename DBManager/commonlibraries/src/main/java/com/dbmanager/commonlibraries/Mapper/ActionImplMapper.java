package com.dbmanager.commonlibraries.Mapper;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.action.ActionImpl;

public class ActionImplMapper {
    public static ActionImpl mapToActionImpl(Bson bson) {
        ActionImpl actionImpl = new ActionImpl();
        Document document = (Document) bson;

        try {
            actionImpl.setActionImplId(document.getString("ActionImplId"));
            actionImpl.setActionName(document.getString("ActionName"));
            actionImpl.setRobotClass(document.getString("RobotClass"));
            actionImpl.setTaskId(document.getString("TaskId"));
            actionImpl.setReturnImmediate(document.getBoolean("ReturnImmediate"));
            actionImpl.setNeededResource(document.getList("NeededResource", String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return actionImpl;

    }
}

package com.dbmanager.commonlibraries.Mapper;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.resource.Resource;

public class ResourceMapper {
    public static Resource mapToResource(Bson bson) {
        Resource resource = new Resource();
        Document document = (Document) bson;

        try {
            resource.setResourceId(document.getString("ResourceId"));
            resource.setRobotClass(document.getString("RobotClass"));
            resource.setTaskId(document.getString("TaskId"));
            resource.setDataSize(document.getInteger("DataSize"));
            resource.setConflict(document.getBoolean("Conflict"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resource;
    }
}

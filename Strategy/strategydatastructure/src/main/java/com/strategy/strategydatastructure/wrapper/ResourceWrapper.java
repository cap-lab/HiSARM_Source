package com.strategy.strategydatastructure.wrapper;

import com.dbmanager.datastructure.resource.Resource;
import com.dbmanager.datastructure.task.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceWrapper {
    private Resource resource;
    private Task task;
}

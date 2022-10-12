package com.strategy.strategydatastructure.wrapper;

import com.dbmanager.datastructure.task.Task;
import com.strategy.strategydatastructure.enumeration.AdditionalTaskType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdditionalTaskWrapper {
    private AdditionalTaskType type;
    private Task task;
}

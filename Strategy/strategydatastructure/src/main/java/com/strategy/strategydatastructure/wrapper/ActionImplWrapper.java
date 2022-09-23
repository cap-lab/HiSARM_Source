package com.strategy.strategydatastructure.wrapper;

import com.dbmanager.datastructure.action.ActionImpl;
import com.dbmanager.datastructure.task.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionImplWrapper {
    private ActionImpl actionImpl;
    private ActonTypeWrapper action;
    private Task task;
}

package com.strategy.strategydatastructure.wrapper;

import com.dbmanager.datastructure.groupingalgorithm.GroupingAlgorithm;
import com.dbmanager.datastructure.task.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupingAlgorithmWrapper {
    private GroupingAlgorithm groupingAlgorithm;
    private Task runTimeTask;
}

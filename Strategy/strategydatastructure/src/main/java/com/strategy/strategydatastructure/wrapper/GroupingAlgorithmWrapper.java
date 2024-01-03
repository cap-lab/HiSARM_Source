package com.strategy.strategydatastructure.wrapper;

import com.dbmanager.datastructure.groupingalgorithm.GroupingAlgorithm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupingAlgorithmWrapper extends AdditionalTaskWrapper {
    private GroupingAlgorithm groupingAlgorithm;
}

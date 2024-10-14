package com.strategy.strategydatastructure.wrapper;

import com.dbmanager.datastructure.leaderselectionalgorithm.LeaderSelectionAlgorithm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaderSelectionAlgorithmWrapper extends AdditionalTaskWrapper {
    private LeaderSelectionAlgorithm leaderSelectionAlgorithm;
}

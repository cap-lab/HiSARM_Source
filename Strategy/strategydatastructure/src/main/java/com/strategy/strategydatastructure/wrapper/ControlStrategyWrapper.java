package com.strategy.strategydatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.dbmanager.datastructure.controlstrategy.ControlStrategy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControlStrategyWrapper {
    private ControlStrategy controlStrategy;
    private List<ActionImplWrapper> actionList = new ArrayList<>();
}

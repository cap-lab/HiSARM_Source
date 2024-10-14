package com.strategy.strategydatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrategyWrapper {
    private List<RobotImplWrapper> robotList = new ArrayList<>();
    private AdditionalInfo additionalInfo;
}

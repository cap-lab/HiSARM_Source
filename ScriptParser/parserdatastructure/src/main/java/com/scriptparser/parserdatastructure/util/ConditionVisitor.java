package com.scriptparser.parserdatastructure.util;

import com.scriptparser.parserdatastructure.entity.common.Condition;

public interface ConditionVisitor {
    public void preConditionFunction(Condition condition, int conditionId);

    public void postConditionFunction(Condition condition, int conditionId);
}

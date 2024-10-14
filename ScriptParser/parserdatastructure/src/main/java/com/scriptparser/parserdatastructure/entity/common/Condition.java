package com.scriptparser.parserdatastructure.entity.common;

import java.util.List;
import com.scriptparser.parserdatastructure.enumeration.Operator;
import com.scriptparser.parserdatastructure.util.ConditionVisitor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Condition {
    private Condition left;
    private Condition right;
    private Identifier leftOperand;
    private Identifier rightOperand;
    private List<Identifier> tagList;
    private Operator operator;
    private boolean isLeaf;

    public int exploreCondition(ConditionVisitor visitor, int conditionId) {
        int currentId = conditionId;
        visitor.preConditionFunction(this, currentId);
        if (isLeaf == false) {
            conditionId = left.exploreCondition(visitor, conditionId + 1);
            conditionId = right.exploreCondition(visitor, conditionId + 1);
        }
        visitor.postConditionFunction(this, currentId);
        return conditionId;
    }
}

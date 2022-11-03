package com.scriptparser.parserdatastructure.entity.statement;

import com.scriptparser.parserdatastructure.entity.common.Condition;
import com.scriptparser.parserdatastructure.entity.common.Time;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.util.ConditionVisitor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConditionalStatement implements Statement {
    private StatementType statementType;

    private Time period;

    private Condition condition;

    public void exploreCondition(ConditionVisitor visitor) {
        condition.exploreCondition(visitor, 0);
    }
}

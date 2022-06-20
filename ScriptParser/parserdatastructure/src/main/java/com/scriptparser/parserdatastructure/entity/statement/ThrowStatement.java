package com.scriptparser.parserdatastructure.entity.statement;

import com.scriptparser.parserdatastructure.entity.common.Event;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ThrowStatement implements Statement {
    public StatementType statementType;

    public Event event;

    public boolean broadcast;
}

package com.scriptparser.parserdatastructure.entity.statement;

import com.scriptparser.parserdatastructure.enumeration.StatementType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DummyStatement implements Statement {
    private StatementType statementType;
}

package com.scriptparser.parserdatastructure.entity.statement;

import java.util.List;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.entity.common.Time;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActionStatement implements Statement {
    private StatementType statementType;

    private String actionName;

    private Time deadline;

    private final List<IdentifierSet> inputList;

    private final List<Identifier> outputList;
}

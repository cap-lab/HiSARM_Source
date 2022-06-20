package com.scriptparser.parserdatastructure.entity.statement;

import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommunicationalStatement implements Statement {
    private StatementType statementType;

    private String counterTeam;

    private Identifier message;

    private Identifier output;
}

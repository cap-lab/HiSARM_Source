package com.scriptparser.parserdatastructure.entity.common;

import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Identifier {
    private String id;
    private IdentifierType type;
}

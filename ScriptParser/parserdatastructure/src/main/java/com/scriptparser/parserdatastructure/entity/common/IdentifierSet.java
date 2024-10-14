package com.scriptparser.parserdatastructure.entity.common;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Getter
@Builder
public class IdentifierSet {
    @Singular("identifier")
    private List<Identifier> identifierSet;
}

package com.scriptparser.parserdatastructure.entity.common;

import java.util.List;
import com.scriptparser.parserdatastructure.enumeration.Operator;
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
}

package com.codegenerator.wrapper;

import com.scriptparser.parserdatastructure.entity.common.Time;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeConditionalWrapper {
    private CodeVariableWrapper leftVariable;
    private CodeVariableWrapper rightVariable;
    private Time period;
}

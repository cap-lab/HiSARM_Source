package com.codegenerator.wrapper;

import com.metadata.algorithm.UEMChannelPort;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodePortWrapper {
    private UEMChannelPort port;
    private CodeVariableWrapper variable;
}

package com.codegenerator.wrapper;

import com.metadata.algorithm.UEMChannelPort;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodePortWrapper {
    UEMChannelPort port;
    CodeVariableWrapper variable;
}

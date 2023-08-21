package com.codegenerator.wrapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeTransitionElementWrapper {
    String srcModeScopeId;
    String event;
    CodeModeWrapper dstMode;
}

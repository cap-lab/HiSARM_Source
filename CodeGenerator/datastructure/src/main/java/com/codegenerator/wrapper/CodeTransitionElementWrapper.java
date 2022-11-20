package com.codegenerator.wrapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeTransitionElementWrapper {
    CodeModeWrapper srcMode;
    String event;
    CodeModeWrapper dstMode;
}

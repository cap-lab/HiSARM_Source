package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeCommunicationWrapper {
    private List<CodePortWrapper> portList = new ArrayList<>();
    private CodeVariableWrapper team;
    private CodeVariableWrapper message;
}

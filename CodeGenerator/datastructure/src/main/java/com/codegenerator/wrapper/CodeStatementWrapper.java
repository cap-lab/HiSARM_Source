package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.metadata.algorithm.task.UEMActionTask;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeStatementWrapper {
    private StatementWrapper statement;
    private List<CodeVariableWrapper> variableList = new ArrayList<CodeVariableWrapper>();
    private UEMActionTask actionTask;
}

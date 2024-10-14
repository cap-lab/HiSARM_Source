package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.metadata.algorithm.task.UEMActionTask;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeActionWrapper {
    private List<CodePortWrapper> inputList = new ArrayList<>();
    private List<CodePortWrapper> outputList = new ArrayList<>();
    private CodePortWrapper group = null;
    private UEMActionTask actionTask;
}

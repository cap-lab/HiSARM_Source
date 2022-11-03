package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeModeWrapper {
    private String modeId;
    private String groupId;
    private ModeWrapper mode;
    private List<CodeServiceWrapper> serviceList = new ArrayList<CodeServiceWrapper>();
}

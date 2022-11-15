package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeTransitionWrapper {
    private String transitionId;
    private TransitionWrapper transition;
    private String groupId;
    private int depth;
    private List<CodeModeWrapper> modeList = new ArrayList<>();

    public Map<String, CodeModeWrapper> getTransitionMapOfMode(CodeModeWrapper mode) {
        Map<String, CodeModeWrapper> transitionMap = new HashMap<>();
        if (transition.getTransitionMap().containsKey(mode.getMode())) {
            transition.getTransitionMap().get(mode.getMode()).forEach(ce -> {
                CodeModeWrapper codeMode = modeList.stream()
                        .filter(m -> m.getMode().equals(ce.getMode().getMode())).findFirst().get();
                transitionMap.put(ce.getEvent().getName(), codeMode);
            });
        }
        return transitionMap;
    }

    public int getModeIndex(CodeModeWrapper mode) {
        return modeList.indexOf(mode);
    }
}

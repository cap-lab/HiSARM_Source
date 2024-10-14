package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private List<CodeVariableWrapper> parameterList = new ArrayList<>();
    private List<CodeVariableWrapper> variableList = new ArrayList<>();
    private List<CodeModeWrapper> modeList = new ArrayList<>();
    private Map<CodeTransitionElementWrapper, List<CodeVariableWrapper>> modeMap =
            new LinkedHashMap<>();

    public void setTransitionId(String transitionId) {
        this.transitionId = transitionId;
    }

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

    public Map<CodeVariableWrapper, CodeVariableWrapper> getArgumentMap(
            CodeTransitionElementWrapper element) {
        Map<CodeVariableWrapper, CodeVariableWrapper> argumentMap = new HashMap<>();
        CodeModeWrapper dstMode = element.getDstMode();
        for (int i = 0; i < modeMap.get(element).size(); i++) {
            if (dstMode.getParameterList().get(i).isRealVariable()) {
                argumentMap.put(modeMap.get(element).get(i), dstMode.getParameterList().get(i));
            }
        }
        return argumentMap;
    }

    public List<CodeTransitionElementWrapper> getTransitionElementList() {
        return new ArrayList<>(modeMap.keySet());
    }

    public List<CodeTransitionElementWrapper> getTransitionElementList(String srcModeScopeId) {
        List<CodeTransitionElementWrapper> elementList = new ArrayList<>();
        for (CodeTransitionElementWrapper element : modeMap.keySet()) {
            if (srcModeScopeId.equals(element.getSrcModeScopeId())) {
                elementList.add(element);
            }
        }
        return elementList;
    }

    public void addMode(CodeModeWrapper mode) {
        modeList.add(mode);
    }

    public CodeVariableWrapper getVariable(String name) {
        for (CodeVariableWrapper variable : variableList) {
            if (variable.getName().equals(name)) {
                return variable;
            }
        }
        return null;
    }

    public CodeTransitionElementWrapper getTransitionElement(String srcModeScopeId, String event) {
        for (CodeTransitionElementWrapper element : modeMap.keySet()) {
            if (srcModeScopeId == null) {
                if (element.getSrcModeScopeId() == null) {
                    return element;
                }
            } else if (srcModeScopeId.equals(element.getSrcModeScopeId())
                    && event.equals(element.getEvent())) {
                return element;
            }
        }
        return null;
    }

    public List<String> getSourceModeScopeIdList() {
        Set<String> sourceModeScopeIdSet = new HashSet<>();
        modeList.forEach(mode -> sourceModeScopeIdSet.add(mode.getScopeId()));
        return new ArrayList<>(sourceModeScopeIdSet);
    }
}

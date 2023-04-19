package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeModeWrapper {
    private String modeId;
    private String groupId;
    private ModeWrapper mode;
    private List<CodeVariableWrapper> parameterList = new ArrayList<>();
    private List<CodeVariableWrapper> variableList = new ArrayList<>();
    private List<CodeServiceWrapper> serviceList = new ArrayList<>();
    private Map<CodeTransitionWrapper, List<CodeVariableWrapper>> groupTransitionMap =
            new HashMap<>();
    private Map<CodeServiceWrapper, List<CodeVariableWrapper>> serviceMap = new HashMap<>();

    public Map<CodeVariableWrapper, List<CodeVariableWrapper>> getArgumentMap() {
        Map<CodeVariableWrapper, List<CodeVariableWrapper>> argumentMap = new HashMap<>();
        for (CodeVariableWrapper parameter : parameterList) {
            List<CodeVariableWrapper> argumentList = new ArrayList<>();
            groupTransitionMap.forEach((transition, list) -> {
                if (list.contains(parameter)) {
                    argumentList.add(transition.getParameterList().get(list.indexOf(parameter)));
                }
            });
            serviceMap.forEach((service, list) -> {
                if (list.contains(parameter)) {
                    argumentList.add(service.getParameterList().get(list.indexOf(parameter)));
                }
            });
            argumentMap.put(parameter, argumentList);
        }
        return argumentMap;
    }

    public void addTransition(CodeTransitionWrapper transition) {
        groupTransitionMap.put(transition, new ArrayList<>());
    }

    public CodeVariableWrapper getVariable(String name) {
        for (CodeVariableWrapper variable : variableList) {
            if (variable.getName().equals(name)) {
                return variable;
            }
        }
        return null;
    }

    public List<String> getGroupCandidateList() {
        List<String> groupCandidateList = new ArrayList<>();
        groupTransitionMap.forEach((transition, list) -> {
            groupCandidateList.add(transition.getGroupId());
        });
        return groupCandidateList;
    }
}

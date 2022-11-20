package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import com.scriptparser.parserdatastructure.util.ModeTransitionVisitor;
import com.scriptparser.parserdatastructure.util.VariableVisitor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupModeTransitionWrapper {
    private TransitionWrapper modeTransition;
    private List<IdentifierSet> inputList = new ArrayList<>();

    public GroupModeTransitionWrapper(TransitionWrapper modeTransitionWrapper) {
        setModeTransition(modeTransitionWrapper);
    }

    public List<String> makeArgumentList(Map<String, String> argumentMap) {
        List<String> argumentList = new ArrayList<>();
        if (inputList != null) {
            for (IdentifierSet is : inputList) {
                for (Identifier id : is.getIdentifierSet()) {
                    if (id.getType().equals(IdentifierType.CONSTANT)) {
                        argumentList.add(id.getId());
                    } else {
                        argumentList.add(argumentMap.get(id.getId()));
                    }
                }
            }
        }
        return argumentList;
    }

    public void traverseModeTransition(String lastId, String currentGroupId, ModeWrapper mode,
            List<String> visitedList, List<String> groupList, ModeTransitionVisitor visitor,
            VariableVisitor variableVisitor) {
        if (variableVisitor != null) {
            variableVisitor.visitModeToTransition(mode, this, currentGroupId);
        }
        modeTransition.traverseTransition(lastId, currentGroupId, visitedList, groupList, visitor,
                variableVisitor);
    }
}

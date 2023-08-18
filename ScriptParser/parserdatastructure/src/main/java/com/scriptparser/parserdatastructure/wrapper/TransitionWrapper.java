package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.scriptparser.parserdatastructure.entity.Transition;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.util.ModeTransitionVisitor;
import com.scriptparser.parserdatastructure.util.VariableVisitor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransitionWrapper {
    private Transition transition;
    private TransitionModeWrapper defaultMode;
    private List<Identifier> parameterList = new ArrayList<>();
    private Map<ModeWrapper, List<CatchEventWrapper>> transitionMap = new HashMap<>();

    public TransitionWrapper(Transition transition) {
        this.transition = transition;
    }

    public String makeTransitionid(String idPrefix) {
        return idPrefix + "_" + transition.getName();
    }

    public Map<String, String> makeArgumentMap(List<String> argumentList) {
        Map<String, String> argumentMap = new HashMap<>();
        if (parameterList != null) {
            for (int i = 0; i < parameterList.size(); i++) {
                argumentMap.put(parameterList.get(i).getId(), argumentList.get(i));
            }
        }
        return argumentMap;
    }

    public void traverseTransition(String lastId, String currentGroup, List<String> visitedList,
            List<String> groupList, ModeTransitionVisitor visitor,
            VariableVisitor variableVisitor) {
        String id = lastId.length() > 0 ? makeTransitionid(lastId) : transition.getName();
        if (visitedList != null) {
            if (visitedList.contains(id)) {
                return;
            } else {
                visitedList.add(id);
            }
        }
        if (visitor != null) {
            visitor.visitTransition(this, id, currentGroup);
        }
        defaultMode.visitTransitionMode(id, currentGroup, this, null, null, visitedList, groupList,
                visitor, variableVisitor);
        for (ModeWrapper key : transitionMap.keySet()) {
            for (CatchEventWrapper ce : transitionMap.get(key)) {
                ce.getMode().visitTransitionMode(id, currentGroup, this, key,
                        ce.getEvent().getName(), visitedList, groupList, visitor, variableVisitor);
            }
        }
    }
}

package com.scriptparser.parserdatastructure.wrapper;

import java.util.List;
import java.util.Map;
import com.scriptparser.parserdatastructure.entity.Transition;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.util.ModeVisitor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransitionWrapper {
    private Transition transition;
    private TransitionModeWrapper defaultMode;
    private List<Identifier> parameterList;
    private Map<ModeWrapper, List<CatchEventWrapper>> transitionMap;

    public TransitionWrapper(Transition transition) {
        this.transition = transition;
    }

    public String makeTransitionid(String idPrefix) {
        return idPrefix + "_" + transition.getName();
    }

    public void traverseTransition(String lastId, String currentGroupId, List<String> visitedList,
            List<String> groupList, ModeVisitor visitor) {
        String id = makeTransitionid(lastId);
        if (visitedList.contains(id)) {
            return;
        } else {
            visitedList.add(id);
        }
        defaultMode.getMode().visitMode(id, currentGroupId, visitedList, groupList, visitor);
        for (ModeWrapper key : transitionMap.keySet()) {
            for (CatchEventWrapper ce : transitionMap.get(key)) {
                ce.getMode().getMode().visitMode(id, currentGroupId, visitedList, groupList,
                        visitor);
            }
        }
    }
}

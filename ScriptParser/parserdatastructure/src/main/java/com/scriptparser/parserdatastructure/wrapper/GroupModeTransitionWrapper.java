package com.scriptparser.parserdatastructure.wrapper;

import java.util.List;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupModeTransitionWrapper {
    private TransitionWrapper modeTransition;
    private List<IdentifierSet> inputList;

    public GroupModeTransitionWrapper(TransitionWrapper modeTransitionWrapper) {
        setModeTransition(modeTransitionWrapper);
    }
}

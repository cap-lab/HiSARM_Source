package com.scriptparser.parserdatastructure.wrapper;

import java.util.List;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupModeTransitionWrapper {
    private TransitionWrapper modeTransition;
    private List<Identifier> inputList;

    public GroupModeTransitionWrapper(TransitionWrapper modeTransitionWrapper) {
        setModeTransition(modeTransitionWrapper);
    }
}

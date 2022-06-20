package com.scriptparser.parserdatastructure.wrapper;

import java.util.List;
import java.util.Map;
import com.scriptparser.parserdatastructure.entity.Transition;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
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
}

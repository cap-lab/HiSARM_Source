package com.scriptparser.parserdatastructure.util;

import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;

public interface ModeTransitionVisitor {
        public void visitMode(ModeWrapper mode, String modeId, String currentGroup,
                        String newGroupPrefix);

        public void visitTransition(TransitionWrapper transition, String transitionId,
                        String currentGroup);
}

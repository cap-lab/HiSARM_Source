package com.scriptparser.parserdatastructure.util;

import com.scriptparser.parserdatastructure.wrapper.GroupModeTransitionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;

public interface VariableVisitor {
    public void visitTransitionToMode(TransitionWrapper transition, ModeWrapper previousMode,
            String event, TransitionModeWrapper currentMode, String groupId);

    public void visitModeToTransition(ModeWrapper mode, GroupModeTransitionWrapper transition,
            String groupId);

    public void visitModeToService(ModeWrapper mode, ParallelServiceWrapper service,
            String groupId);
}

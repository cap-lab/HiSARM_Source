package com.scriptparser.parserdatastructure.util;

import com.scriptparser.parserdatastructure.wrapper.GroupModeTransitionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;

public interface VariableVisitor {
        public void visitTransitionToMode(TransitionWrapper transition, String transitionId,
                        String dstModePrefix, ModeWrapper previousMode, String event,
                        TransitionModeWrapper currentMode, String groupId);

        public void visitModeToTransition(ModeWrapper mode, String lastId, String modeId,
                        GroupModeTransitionWrapper transition, String groupId);

        public void visitModeToService(ModeWrapper mode, String modeId,
                        ParallelServiceWrapper service, String groupId);
}

package com.scriptparser.parserdatastructure.util;

import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;

public interface ModeVisitor {
    public void visitMode(ModeWrapper mode, String modeId, String currentGroup);
}

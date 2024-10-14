package com.scriptparser.parserdatastructure.wrapper;

import com.scriptparser.parserdatastructure.entity.Group;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupWrapper {
    private Group group;
    private GroupModeTransitionWrapper modeTransition;
}

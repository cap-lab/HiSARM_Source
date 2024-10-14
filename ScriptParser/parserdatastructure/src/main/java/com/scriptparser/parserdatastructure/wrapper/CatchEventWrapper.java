package com.scriptparser.parserdatastructure.wrapper;

import com.scriptparser.parserdatastructure.entity.common.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatchEventWrapper {
    private Event event;
    private TransitionModeWrapper mode; 
}

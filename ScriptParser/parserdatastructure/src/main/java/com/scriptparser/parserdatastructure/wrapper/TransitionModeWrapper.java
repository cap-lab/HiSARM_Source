package com.scriptparser.parserdatastructure.wrapper;

import java.util.List;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransitionModeWrapper {
   private ModeWrapper mode;
   private List<IdentifierSet> inputList;
}

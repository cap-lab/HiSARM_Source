package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import com.scriptparser.parserdatastructure.util.ModeTransitionVisitor;
import com.scriptparser.parserdatastructure.util.VariableVisitor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransitionModeWrapper {
   private ModeWrapper mode;
   private List<IdentifierSet> inputList = new ArrayList<>();

   public List<String> makeArgumentList(Map<String, String> argumentMap) {
      List<String> argumentList = new ArrayList<>();
      if (inputList != null) {
         for (IdentifierSet is : inputList) {
            for (Identifier id : is.getIdentifierSet()) {
               if (id.getType().equals(IdentifierType.CONSTANT)) {
                  argumentList.add(id.getId());
               } else {
                  argumentList.add(argumentMap.get(id.getId()));
               }
            }
         }
      }
      return argumentList;
   }

   public void visitTransitionMode(String lastId, String currentGroupId,
         TransitionWrapper transition, ModeWrapper previousMode, String event,
         List<String> visitedId, List<String> groupList, ModeTransitionVisitor visitor,
         VariableVisitor variableVisitor) {
      if (variableVisitor != null) {
         variableVisitor.visitTransitionToMode(transition, previousMode, event, this,
               currentGroupId);
      }
      mode.visitMode(lastId, currentGroupId, visitedId, groupList, visitor, variableVisitor);
   }
}

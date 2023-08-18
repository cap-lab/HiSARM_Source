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

   public void visitTransitionMode(String lastId, String currentGroup, TransitionWrapper transition,
         ModeWrapper previousMode, String event, List<String> visitedId, List<String> scopeList,
         ModeTransitionVisitor visitor, VariableVisitor variableVisitor) {
      String newId;
      String groupPrefix;
      if (previousMode != null) {
         newId = lastId + "_" + previousMode.getMode().getName();
         groupPrefix = currentGroup + "_" + previousMode.getMode().getName();
      } else {
         newId = lastId + "_default";
         groupPrefix = currentGroup + "_default";
      }
      if (variableVisitor != null) {
         variableVisitor.visitTransitionToMode(transition, lastId, newId, previousMode, event, this,
               currentGroup);
      }

      mode.visitMode(newId, currentGroup, groupPrefix, visitedId, scopeList, visitor,
            variableVisitor);
   }
}

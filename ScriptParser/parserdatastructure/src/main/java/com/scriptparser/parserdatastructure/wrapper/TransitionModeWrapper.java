package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
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
      String mergedInput = String.join(";",
            inputList.stream().map(IdentifierSet::toString).toArray(String[]::new));
      CRC32 crc32 = new CRC32();
      crc32.update(mergedInput.getBytes());
      newId = lastId + "_" + Long.toHexString(crc32.getValue());
      groupPrefix = currentGroup + "_" + Long.toHexString(crc32.getValue());
      if (variableVisitor != null) {
         variableVisitor.visitTransitionToMode(transition, lastId, newId, previousMode, event, this,
               currentGroup);
      }
      mode.visitMode(newId, currentGroup, groupPrefix, visitedId, scopeList, visitor,
            variableVisitor);
   }
}

package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.entity.common.Time;
import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import com.scriptparser.parserdatastructure.enumeration.Priority;
import com.scriptparser.parserdatastructure.util.VariableVisitor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParallelServiceWrapper {
    private ServiceWrapper service;
    private Time timeout;
    private Priority prioirty;
    private List<IdentifierSet> inputList = new ArrayList<>();

    public ParallelServiceWrapper(ServiceWrapper service) {
        setService(service);
    }

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

    public void visitModeService(ModeWrapper mode, String currentGroupId,
            VariableVisitor variableVisitor) {
        if (variableVisitor != null) {
            variableVisitor.visitModeToService(mode, this, currentGroupId);
        }
    }

}

package com.codegenerator.wrapper;

import java.util.List;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeServiceWrapper {
    ServiceWrapper service;
    String serviceId;
    List<CodeVariableWrapper> variableList;
    List<CodeStatementWrapper> statementList;

    public static String makeServiceId(String modeId, String serviceId) {
        return modeId + "_" + serviceId;
    }
}

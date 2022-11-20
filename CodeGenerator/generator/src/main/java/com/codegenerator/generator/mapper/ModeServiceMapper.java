package com.codegenerator.generator.mapper;

import java.util.ArrayList;
import java.util.List;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.codegenerator.wrapper.CodeVariableWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;

public class ModeServiceMapper {
    public List<CodeServiceWrapper> mapModeService(CodeModeWrapper mode) {
        List<CodeServiceWrapper> serviceList = new ArrayList<>();
        for (ParallelServiceWrapper service : mode.getMode().getServiceList()) {
            CodeServiceWrapper codeService = new CodeServiceWrapper();
            codeService.setService(service.getService());
            codeService.setServiceId(CodeServiceWrapper.makeServiceId(mode.getModeId(),
                    service.getService().getService().getName()));
            codeService.setGroupId(mode.getGroupId());
            if (service.getService().getParameterList() != null) {
                service.getService().getParameterList().forEach(parameter -> {
                    CodeVariableWrapper codeVariable = new CodeVariableWrapper();
                    codeVariable.getChildVariableList().add(codeVariable);
                    codeVariable.setId(CodeVariableWrapper
                            .makeVariableId(codeService.getServiceId(), parameter.getId()));
                    codeVariable.setName(parameter.getId());
                    codeService.getParameterList().add(codeVariable);
                    codeService.getVariableList().add(codeVariable);
                });
            }
            serviceList.add(codeService);
        }
        return serviceList;
    }
}

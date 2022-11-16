package com.codegenerator.generator.mapper;

import java.util.ArrayList;
import java.util.List;
import com.codegenerator.wrapper.CodeModeWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
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
            serviceList.add(codeService);
        }
        mode.setServiceList(serviceList);
        return serviceList;
    }
}

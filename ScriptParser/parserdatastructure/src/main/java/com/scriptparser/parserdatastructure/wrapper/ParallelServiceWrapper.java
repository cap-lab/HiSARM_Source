package com.scriptparser.parserdatastructure.wrapper;

import java.util.List;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.entity.common.Time;
import com.scriptparser.parserdatastructure.enumeration.Priority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParallelServiceWrapper {
    private ServiceWrapper service;
    private Time timeout;
    private Priority prioirty;
    private List<IdentifierSet> inputList;

    public ParallelServiceWrapper(ServiceWrapper service) {
        setService(service);
    }

}

package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.scriptparser.parserdatastructure.entity.Service;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceWrapper {
    private Service service;
    private List<StatementWrapper> statement = new ArrayList<>();
    private List<Identifier> parameterList = new ArrayList<>();

    public ServiceWrapper(Service service) {
        setService(service);
    }
}

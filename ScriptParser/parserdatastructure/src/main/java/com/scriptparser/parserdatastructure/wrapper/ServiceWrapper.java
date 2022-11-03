package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.scriptparser.parserdatastructure.entity.Service;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.util.StatementVisitor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceWrapper {
    private Service service;
    private List<StatementWrapper> statementList = new ArrayList<>();
    private List<Identifier> parameterList = new ArrayList<>();

    public ServiceWrapper(Service service) {
        setService(service);
    }

    public void traverseService(StatementVisitor visitor) {
        for (int i = 0; i < statementList.size(); i++) {
            statementList.get(i).visitStatement(visitor, i);
        }
    }
}

package com.scriptparser.parserdatastructure.wrapper;

import com.scriptparser.parserdatastructure.util.KeyValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariableWrapper {
    private String name;
    private KeyValue<ServiceWrapper, VariableWrapper> creator;
}

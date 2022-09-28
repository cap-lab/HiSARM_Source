package com.scriptparser.parserdatastructure.enumeration;

import lombok.Getter;

@Getter
public enum Operator {
    G("<"), L(">"), GE("<="), LE(">="), E("=="), NE("!="), AND("and"), OR("or");

    private String value;

    private Operator(String value) {
        this.value = value;
    }

    static public Operator valueFrom(String value) throws Exception {
        for (Operator op : Operator.values()) {
            if (op.getValue().equals(value)) {
                return op;
            }
        }
        throw new Exception("Strange Operator : " + value);
    }
}

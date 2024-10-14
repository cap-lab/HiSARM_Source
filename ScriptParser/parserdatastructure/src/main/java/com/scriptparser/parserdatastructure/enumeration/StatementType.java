package com.scriptparser.parserdatastructure.enumeration;

import lombok.Getter;

@Getter
public enum StatementType {
    IF("if"), LOOP("loop"), REPEAT("repeat"), SEND("send"), RECEIVE("receive"), PUBLISH(
            "publish"), SUBSCRIBE("subscribe"), THROW("throw"), ACTION(
                    "action"), ELSE("else"), COMPOUND_IN("."), COMPOUND_OUT("."), FINISH("finish");

    private final String value;

    private StatementType(String value) {
        this.value = value;
    }

    static public StatementType valueFrom(String value) throws Exception {
        for (StatementType s : StatementType.values()) {
            if (s.getValue().equals(value)) {
                return s;
            }
        }
        throw new Exception("Strange Statement Type : " + value);
    }
}

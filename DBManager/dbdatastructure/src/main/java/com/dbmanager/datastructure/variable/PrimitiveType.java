package com.dbmanager.datastructure.variable;

public enum PrimitiveType {
    INT8("int8", 1), INT16("int16", 2), INT32("int32", 4), INT64("int64", 8), FLOAT("float",
            8), ENUM("enum", 4);

    private final String value;
    private final int size;

    private PrimitiveType(String value, int size) {
        this.value = value;
        this.size = size;
    }

    public String getValue() {
        return value;
    }

    public static boolean isPrimitiveType(String type) {
        for (PrimitiveType primitiveType : PrimitiveType.values()) {
            if (primitiveType.getValue().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static PrimitiveType fromString(String text) {
        for (PrimitiveType primitiveType : PrimitiveType.values()) {
            if (primitiveType.getValue().equals(text)) {
                return primitiveType;
            }
        }
        return null;
    }

    public int getSize() {
        return size;
    }
}

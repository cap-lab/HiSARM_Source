package com.metadata.architecture;

import hopes.cic.xml.EnvironmentVariableType;

public class UEMEnvironmentVariable extends EnvironmentVariableType {
    public UEMEnvironmentVariable(String name, String value) {
        setName(name);
        setValue(value);
    }
}

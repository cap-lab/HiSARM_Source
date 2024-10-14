package com.dbmanager.datastructure.architecture;

public enum CPUArchitecture {
    X86("x86"), X86_64("x86_64"), ARM("arm"), ARM64("arm64"), AVR("arv"), SAMD("samd"), GENERIC(
            "generic");

    private String value;

    private CPUArchitecture(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

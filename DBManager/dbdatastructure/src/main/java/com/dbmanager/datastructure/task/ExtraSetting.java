package com.dbmanager.datastructure.task;

public class ExtraSetting {
    public enum ExtraSettingType {
        EXTRA_HEADER("Extra Header"), EXTRA_SOURCE("Extra Source"), EXTRA_CIC(
                "Extra CIC"), EXTRA_FILE("Extra File");

        private final String value;

        private ExtraSettingType(String value) {
            this.value = value;
        }

        static public ExtraSettingType valueFrom(String string) {
            for (ExtraSettingType type : ExtraSettingType.values()) {
                if (type.value.equals(string)) {
                    return type;
                }
            }
            return null;
        }
    }

    private ExtraSettingType type;
    private String name;

    public ExtraSettingType getType() {
        return type;
    }

    public void setType(ExtraSettingType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

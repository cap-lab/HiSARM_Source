package com.metadata.generator.algorithm;

import java.math.BigInteger;
import hopes.cic.xml.ModeTaskType;
import hopes.cic.xml.TimeMetricType;
import hopes.cic.xml.TimeType;

public class UEMModeTask extends ModeTaskType {

    public UEMModeTask() {
        setPeriod(new TimeType());
        setDeadline(new TimeType());
    }

    public void setPeriod(int time) {
        getPeriod().setValue(BigInteger.valueOf(time));
    }

    public void setPeriodUnit(String unit) {
        getPeriod().setMetric(TimeMetricType.fromValue(unit));
    }

    public void setDeadline(int time) {
        getDeadline().setValue(BigInteger.valueOf(time));
    }

    public void setDeadlineUnit(String unit) {
        getDeadline().setMetric(TimeMetricType.fromValue(unit));
    }

    public void setPriority(int priority) {
        setPriority(BigInteger.valueOf(priority));
    }
}

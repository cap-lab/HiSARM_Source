package com.scriptparser.parserdatastructure.entity.common;

import com.scriptparser.parserdatastructure.enumeration.TimeUnit;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Time {
    private int time;
    private TimeUnit timeUnit;

    public int getConvertedTime() {
        if (timeUnit == TimeUnit.DAY) {
            return time * 24;
        }
        return time;
    }

    public TimeUnit getConvertedTimeUnit() {
        if (timeUnit == TimeUnit.DAY) {
            return TimeUnit.HOUR;
        }
        return timeUnit;
    }
}

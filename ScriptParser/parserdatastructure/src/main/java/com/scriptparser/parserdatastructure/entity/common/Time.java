package com.scriptparser.parserdatastructure.entity.common;

import com.scriptparser.parserdatastructure.enumeration.TimeUnit;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Time {
    private int time;
    private TimeUnit timeUnit;
}

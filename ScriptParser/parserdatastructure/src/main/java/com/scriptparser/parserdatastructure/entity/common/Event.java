package com.scriptparser.parserdatastructure.entity.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class Event {
    @NonNull
    private String name;
}

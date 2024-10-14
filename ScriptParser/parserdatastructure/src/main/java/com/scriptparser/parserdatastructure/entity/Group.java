package com.scriptparser.parserdatastructure.entity;

import com.scriptparser.parserdatastructure.entity.common.Condition;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Group {
    private String name;
    private int min;
    private int proper;
    private boolean others;
    private Condition tagList;
}

package com.scriptparser.parserdatastructure.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Robot {
   private String name;

   private String type;

   private int count;
}

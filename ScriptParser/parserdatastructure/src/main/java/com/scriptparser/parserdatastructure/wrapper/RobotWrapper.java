package com.scriptparser.parserdatastructure.wrapper;

import com.scriptparser.parserdatastructure.entity.Robot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotWrapper {
    private Robot robot;
    private TeamWrapper team;

    public RobotWrapper(Robot robot) {
        this.robot = robot;
    }
}

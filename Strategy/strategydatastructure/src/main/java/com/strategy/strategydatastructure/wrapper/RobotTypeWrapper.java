package com.strategy.strategydatastructure.wrapper;

import java.util.List;
import com.dbmanager.datastructure.architecture.Architecture;
import com.dbmanager.datastructure.robot.Robot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotTypeWrapper {
    private Robot robotType;
    private List<Architecture> deviceList;
}

package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.scriptparser.parserdatastructure.entity.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamWrapper {
    private Team team;
    private MissionWrapper mission;
    private List<RobotWrapper> robotList = new ArrayList<>();

    public TeamWrapper(Team team) {
        this.team = team;
    }

    public RobotWrapper getRobot(String robotName) throws Exception {
        Optional<RobotWrapper> robot = getRobotList().stream()
                .filter(r -> r.getRobot().getName().equals(robotName)).findAny();
        if (robot.isPresent()) {
            return robot.get();
        } else {
            throw new Exception(
                    "No robot whose name is " + robotName + " in the team " + team.getName());
        }
    }
}

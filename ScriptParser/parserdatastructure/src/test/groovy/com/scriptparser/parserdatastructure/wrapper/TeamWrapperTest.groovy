import spock.lang.*
import java.util.List;
import java.util.ArrayList;
import com.scriptparser.parserdatastructure.entity.*;
import com.scriptparser.parserdatastructure.wrapper.*;


class TeamWrapperTest extends spock.lang.Specification {

    private TeamWrapper team;

    public void setup() {
        team = new TeamWrapper(Team.builder().name("team").build());
        // Robot
        List<RobotWrapper> robotList = new ArrayList<RobotWrapper>();
        robotList.add(new RobotWrapper(Robot.builder().type("type").name("name").count(1).build()));
        team.setRobotList(robotList);
    }

    def "Get the team name"() {
        when:
        def name = team.getTeam().getName()

        then:
        name == "team"
	}

	def "Get the robot in the team by a name"() {
        when:
        def robot = team.getRobot("name").getRobot()

        then:
        robot.getName() == "name"
        robot.getType() == "type"
        robot.getCount() == 1
	}

    def "Get exception when getting the team with a wrong name"() {
        when:
        def robot = team.getRobot("wrong").getRobot()

        then:
        def e = thrown(Exception.class)
        e.message == "No robot whose name is wrong in the team team"

    }
}
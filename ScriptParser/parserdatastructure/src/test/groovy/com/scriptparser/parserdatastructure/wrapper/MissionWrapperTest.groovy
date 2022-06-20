import spock.lang.*
import java.util.List;
import java.util.ArrayList;
import com.scriptparser.parserdatastructure.entity.*;
import com.scriptparser.parserdatastructure.wrapper.*;


class MissionWrapperTest extends spock.lang.Specification {

    private MissionWrapper mission;

    public void setup() {
        mission = new MissionWrapper();
        // Team
        List<TeamWrapper> teamList = new ArrayList<TeamWrapper>();
        teamList.add(new TeamWrapper(Team.builder().name("team").build()));
        mission.setTeamList(teamList);
        // Service
        List<ServiceWrapper> serviceList = new ArrayList<ServiceWrapper>();
        serviceList.add(new ServiceWrapper(Service.builder().name("service").build()));
        mission.setServiceList(serviceList);
        // Mode
        List<ModeWrapper> modeList = new ArrayList<ModeWrapper>();
        modeList.add(new ModeWrapper(Mode.builder().name("mode").build()));
        mission.setModeList(modeList);
        // Mode Transition
        List<TransitionWrapper> transitionList = new ArrayList<TransitionWrapper>();
        transitionList.add(new TransitionWrapper(Transition.builder().name("transition").build()));
        mission.setTransitionList(transitionList);
    }

	def "Get the team in a mission by a team name"() {
        when:
        def name = mission.getTeam("team").getTeam().getName()

        then:
        name == "team"
	}

    def "Get exception when getting the team with a wrong name"() {
        when:
        mission.getTeam("slave").getTeam().getName() 

        then:
        def e = thrown(Exception.class)
        e.message == "No team whose name is slave"

    }

    def "Get the service in a mission by a service name"() {
        when:
        def name = mission.getService("service").getService().getName()

        then:
        name == "service"
    }

    def "Get exception when getting the service with a wrong name"() {
        when:
        mission.getService("ser").getService().getName()

        then:
        def e = thrown(Exception.class)
        e.message == "No service whose name is ser"
    }

	def "Get the Mode in a mission by a mode name"() {
        when:
        def name = mission.getMode("mode").getMode().getName()

        then:
        name == "mode"
	}

    def "Get exception when getting the mode with a wrong name"() {
        when:
        mission.getMode("modem").getMode().getName()

        then:
        def e = thrown(Exception.class)
        e.message == "No mode whose name is modem"

    }

    def "Get the mode transition in a mission by a name"() {
        when:
        def name = mission.getTransition("transition").getTransition().getName()

        then:
        name == "transition"
    }

    def "Get exception when getting the mode transition with a wrong name"() {
        when:
        mission.getTransition("trans").getTransition().getName()

        then:
        def e = thrown(Exception.class)
        e.message == "No mode transition whose name is trans"
    }
}
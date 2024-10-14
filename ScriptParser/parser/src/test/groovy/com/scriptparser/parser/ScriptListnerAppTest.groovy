import spock.lang.*
import com.scriptparser.parser.Parser
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper

class ScriptListnerAppTest extends spock.lang.Specification {

    private String prefix = "src/test/resources/"

    def "basic script test"(){
        given:
        Parser parser = new Parser()

        when:
        MissionWrapper mission = parser.parseScript(prefix + "reference/simple_test.bdl")

        then:
        mission != null 
        mission.getTeam("Master") != null
        mission.getTeam("Master").getRobot("fakebot") != null
        mission.getService("service1") != null
        mission.getMode("A") != null
        mission.getTransition("Master") != null
    }

    def "cooperation script test"() {
        given:
            Parser parser = new Parser()

        expect:
            MissionWrapper mission = parser.parseScript(prefix + script)
            mission.getTeamList().size()       == teamNum
            mission.getServiceList().size()    == serviceNum
            mission.getModeList().size()       == modeNum
            mission.getTransitionList().size() == transitionNum
        
        where:
            script                      || teamNum | serviceNum | modeNum | transitionNum
            "demo_3type.bdl"            || 3       | 9          | 7       | 3
            "demo_6num.bdl"             || 1       | 4          | 4       | 2
            "reference/cooperation.bdl" || 3       | 15         | 9       | 3
            "reference/indoor.bdl"      || 1       | 8          | 18      | 4
            "reference/on_the_move.bdl" || 1       | 8          | 11      | 4
            "reference/swarm.bdl"       || 1       | 10         | 9       | 5
            "reference/single.bdl"      || 1       | 6          | 5       | 1
    }
}

import spock.lang.*
import com.scriptparser.parser.Parser
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper

class ScriptListnerAppTest extends spock.lang.Specification {

    private String prefix = "src/test/resources/"

    def "basic script test"(){
        given:
        Parser parser = new Parser()

        when:
        MissionWrapper mission = parser.parseScript(prefix + "simple_test.bdl")

        then:
        mission != null 
        mission.getTeam("Master") != null
        mission.getTeam("Master").getRobot("robot") != null
        mission.getService("service") != null
        mission.getMode("DEF") != null
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
            script            || teamNum | serviceNum | modeNum | transitionNum
            "demo_3type.bdl"  || 3       | 10         | 7       | 3
            "demo_6num.bdl"   || 1       | 4          | 4       | 2
            //"cooperation.bdl" || 3       | 16         | 9       | 3
            //"indoor.bdl"      || 1       | 9          | 18      | 4
            //"on_the_move.bdl" || 1       | 9          | 11      | 4
            //"swarm.bdl"       || 1       | 11         | 9       | 5
            //"single.bdl"      || 1       | 7          | 5       | 1
    }
}
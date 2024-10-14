import spock.lang.*
import com.dbmanager.commonlibraries.*
import com.dbmanager.dbdatastructure.*

class DBTest extends Specification {
    def db = new DBService()

    def setup() {
        db.initializeDB("127.0.0.1", 5984, "admin", null, "admin", null)
    }

    def cleanup() {
       db.close() 
    }

    def "test get action"() {
        when:
        def action = db.getAction("action_simplest") 

        then:
        println action
        action != null
    }

    def "test get actionimpl"() {
        when:
        def actionImpl = db.getActionImpl("action_simplest") 

        then:
        println actionImpl
        actionImpl != null
    }

    def "test get architecture"() {
        when:
        def architecture = db.getArchitecture("desktop") 

        then:
        println architecture
        architecture != null
    }

    def "test get strategy"() {
        when:
        def strategy = db.getStrategy("action_simplest_0") 

        then:
        println strategy
        strategy != null
    }

    
}
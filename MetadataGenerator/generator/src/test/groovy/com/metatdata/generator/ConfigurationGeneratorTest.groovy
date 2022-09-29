import spock.lang.*
import java.nio.file.Path
import java.nio.file.Paths
import com.metadata.generator.MetadataGenerator
import com.metadata.generator.ConfigurationGenerator
import com.metadata.generator.constant.MetadataConstant

class ConfigurationGeneratorTest extends spock.lang.Specification {

    def "make configuration xml"(){
        given:
        MetadataGenerator generator = new MetadataGenerator()
        Path projectDir = Paths.get(MetadataConstant.GENERATE_DIRECTORY.toString(),
                    generator.makeProjectDirName("test"));
        generator.makeProjectDirectory(projectDir)

        when:
        boolean result = ConfigurationGenerator.generateConfiguration(projectDir, "test")

        then:
        result == true
    }
}
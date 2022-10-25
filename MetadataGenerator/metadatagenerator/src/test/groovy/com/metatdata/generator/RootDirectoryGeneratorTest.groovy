import spock.lang.*
import java.nio.file.Path
import java.nio.file.Paths
import com.metadata.metadatagenerator.MetadataGenerator
import com.metadata.metadatagenerator.constant.MetadataConstant

class RootDirectoryGeneratorTest extends spock.lang.Specification {

    def "make default directory"(){
        given:
        MetadataGenerator generator = new MetadataGenerator()
        Path projectDir = Paths.get(MetadataConstant.GENERATE_DIRECTORY.toString(),
                    generator.makeProjectDirName("test"));

        when:
        boolean result = generator.makeProjectDirectory(projectDir)

        then:
        result == true
    }
}
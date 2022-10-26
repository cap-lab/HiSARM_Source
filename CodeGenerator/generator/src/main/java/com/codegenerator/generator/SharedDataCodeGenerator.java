package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.codegenerator.generator.constant.SharedDataTaskConstant;
import com.metadata.UEMRobot;
import com.metadata.algorithm.UEMLibrary;

public class SharedDataCodeGenerator {
    public void generate(Path targetDir, List<UEMRobot> robotList) {
        for (UEMRobot robot : robotList) {
            for (UEMLibrary library : robot.getRobotTask().getLibraryTaskList()) {
                makeSharedDataTaskCode(targetDir, library);
            }
        }
    }

    private void makeSharedDataTaskCode(Path targetDir, UEMLibrary library) {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(SharedDataTaskConstant.LIB_NAME, library.getName());
        rootHash.put(SharedDataTaskConstant.VARIABLE_TYPE,
                library.getVariableType().getVariableType());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.SHARED_DATA_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(), library.getHeader()), rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.SHARED_DATA_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(), library.getFile()), rootHash);
    }
}

package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codegenerator.constant.SharedDataTaskConstant;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.metadata.algorithm.library.UEMLibrary;
import com.metadata.algorithm.library.UEMSharedData;

public class SharedDataCodeGenerator {
    public void generate(Path targetDir, List<CodeRobotWrapper> robotList) {
        for (CodeRobotWrapper robot : robotList) {
            for (UEMLibrary library : robot.getRobot().getRobotTask().getSharedDataTaskList()) {
                makeSharedDataTaskCode(targetDir, (UEMSharedData) library, robot);
            }
        }
    }

    private void makeSharedDataTaskCode(Path targetDir, UEMSharedData library,
            CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(SharedDataTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(SharedDataTaskConstant.LIB_NAME, library.getName());
        rootHash.put(SharedDataTaskConstant.VARIABLE_TYPE, library.getVariableType());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.SHARED_DATA_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(), library.getHeader()), rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.SHARED_DATA_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(), library.getFile()), rootHash);
    }
}

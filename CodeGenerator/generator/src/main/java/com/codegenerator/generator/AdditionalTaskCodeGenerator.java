package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.codegenerator.constant.LeaderTaskConstant;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.codegenerator.wrapper.CodeRobotWrapper;

public class AdditionalTaskCodeGenerator {
    public void generateAdditionalCode(Path targetDir, List<CodeRobotWrapper> robotList) {
        for (CodeRobotWrapper robot : robotList) {
            generateLeaderLibraryTaskCode(targetDir, robot);
        }
    }

    public void generateLeaderLibraryTaskCode(Path targetDir, CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(LeaderTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(LeaderTaskConstant.GROUP_LIST,
                robot.getRobot().getRobotTask().getRobot().getGroupList());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.LEADER_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobot().getRobotTask().getLeaderLibraryTask().getHeader()),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.LEADER_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobot().getRobotTask().getLeaderLibraryTask().getFile()),
                rootHash);
    }
}

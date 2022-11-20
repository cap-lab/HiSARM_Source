package com.codegenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.codegenerator.constant.ControlTaskConstant;
import com.codegenerator.generator.constant.CodeGeneratorConstant;
import com.codegenerator.generator.util.LocalFileCopier;
import com.codegenerator.wrapper.CodeActionWrapper;
import com.codegenerator.wrapper.CodeRobotWrapper;
import com.codegenerator.wrapper.CodeServiceWrapper;
import com.codegenerator.wrapper.CodeStatementWrapper;
import com.metadata.algorithm.UEMChannelPort;
import com.metadata.algorithm.task.UEMActionTask;
import com.scriptparser.parserdatastructure.entity.statement.ConditionalStatement;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;

public class ControlTaskCodeGenerator {

    public void generateControlTaskCode(Path targetDir, List<CodeRobotWrapper> robotList) {
        try {
            for (CodeRobotWrapper robot : robotList) {
                generateEventCode(targetDir, robot);
                generatePortCode(targetDir, robot);
                generateResourceCode(targetDir, robot);
                generateActionCode(targetDir, robot);
                generateTimerCode(targetDir, robot);
                generateServiceCode(targetDir, robot);
                generateModeCode(targetDir, robot);
                generateTransitionCode(targetDir, robot);
                copyCodeRelatedControlTask(targetDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateEventCode(Path targetDir, CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();
        Set<String> eventList = new HashSet<String>();

        robot.getTransitionList().forEach(transition -> {
            transition.getTransition().getTransitionMap().values().forEach(ceList -> {
                ceList.forEach(ce -> eventList.add(ce.getEvent().getName()));
            });
        });

        rootHash.put(ControlTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(ControlTaskConstant.EVENT_LIST, eventList);

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.EVENT_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.EVENT_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.EVENT_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.EVENT_SOURCE_SUFFIX),
                rootHash);
    }

    private void generatePortCode(Path targetDir, CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();
        List<CodeActionWrapper> actionList = new ArrayList<>();
        List<UEMChannelPort> commPortList = new ArrayList<>();
        List<CodeStatementWrapper> commStatementList = new ArrayList<>();
        List<CodeStatementWrapper> throwStatementList = new ArrayList<>();

        robot.getServiceList().forEach(service -> {
            service.getStatementList().forEach(st -> {
                actionList.addAll(st.getActionList());
                if (st.getComm() != null) {
                    commStatementList.add(st);
                }
                if (st.getTh() != null) {
                    throwStatementList.add(st);
                }
            });
        });
        commPortList.addAll(robot.getRobot().getRobotTask().getControlTask()
                .getInputPortList(robot.getRobot().getRobotTask().getListenTask()));
        commPortList.addAll(robot.getRobot().getRobotTask().getControlTask()
                .getOutputPortList(robot.getRobot().getRobotTask().getReportTask()));

        rootHash.put(ControlTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(ControlTaskConstant.ACTION_LIST, actionList);
        rootHash.put(ControlTaskConstant.COMM_PORT_LIST, commPortList);
        rootHash.put(ControlTaskConstant.COMM_STATEMENT_LIST, commStatementList);
        rootHash.put(ControlTaskConstant.THROW_STATEMENT_LIST, throwStatementList);
        rootHash.put(ControlTaskConstant.LEADER_PORT,
                robot.getRobot().getRobotTask().getControlTask()
                        .getInputPortList(robot.getRobot().getRobotTask().getLeaderTask()).get(0));

        FTLHandler.getInstance()
                .generateCode(CodeGeneratorConstant.PORT_HEADER_TEMPLATE,
                        Paths.get(targetDir.toString(),
                                robot.getRobotName() + CodeGeneratorConstant.PORT_HEADER_SUFFIX),
                        rootHash);
        FTLHandler.getInstance()
                .generateCode(CodeGeneratorConstant.PORT_SOURCE_TEMPLATE,
                        Paths.get(targetDir.toString(),
                                robot.getRobotName() + CodeGeneratorConstant.PORT_SOURCE_SUFFIX),
                        rootHash);

    }

    private void generateResourceCode(Path targetDir, CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(ControlTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(ControlTaskConstant.RESOURCE_LIST, robot.getRobot().getRobotTask().getRobot()
                .getRobotType().getRobotType().getResourceList());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.RESOURCE_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.RESOURCE_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.RESOURCE_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.RESOURCE_SOURCE_SUFFIX),
                rootHash);
    }

    private void generateActionCode(Path targetDir, CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();
        List<UEMActionTask> actionTaskList = new ArrayList<>();
        Set<ActionTypeWrapper> actionTypeSet = new HashSet<>();

        robot.getServiceList().forEach(service -> {
            service.getStatementList().forEach(st -> {
                actionTaskList.addAll(st.getActionList().stream().map(a -> a.getActionTask())
                        .collect(Collectors.toList()));
                actionTypeSet.addAll(st.getActionList().stream()
                        .map(a -> a.getActionTask().getActionImpl().getActionType())
                        .collect(Collectors.toList()));
            });
        });

        rootHash.put(ControlTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(ControlTaskConstant.ACTION_TASK_LIST, actionTaskList);
        rootHash.put(ControlTaskConstant.ACTION_TYPE_LIST, actionTypeSet);

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.ACTION_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.ACTION_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.ACTION_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.ACTION_SOURCE_SUFFIX),
                rootHash);

    }

    private void generateTimerCode(Path targetDir, CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();
        int timerCount = 0;

        for (CodeServiceWrapper codeService : robot.getServiceList()) {
            for (CodeActionWrapper action : codeService.getActionList()) {
                if (action.getActionTask().getActionStatement().getDeadline() != null) {
                    timerCount++;
                }
            }
            for (CodeStatementWrapper statement : codeService.getConditionStatementList()) {
                ConditionalStatement cond =
                        (ConditionalStatement) statement.getStatement().getStatement();
                if (cond.getPeriod() != null) {
                    timerCount++;
                }
            }
        }

        rootHash.put(ControlTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(ControlTaskConstant.TIMER_COUNT, timerCount);

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.TIMER_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.TIMER_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.TIMER_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.TIMER_SOURCE_SUFFIX),
                rootHash);
    }

    private void generateServiceCode(Path targetDir, CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(ControlTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(ControlTaskConstant.SERVICE_LIST, robot.getServiceList());

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.SERVICE_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.SERVICE_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.SERVICE_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.SERVICE_SOURCE_SUFFIX),
                rootHash);
    }

    private void generateModeCode(Path targetDir, CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();

        rootHash.put(ControlTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(ControlTaskConstant.MODE_LIST, robot.getModeList());

        FTLHandler.getInstance()
                .generateCode(CodeGeneratorConstant.MODE_HEADER_TEMPLATE,
                        Paths.get(targetDir.toString(),
                                robot.getRobotName() + CodeGeneratorConstant.MODE_HEADER_SUFFIX),
                        rootHash);
        FTLHandler.getInstance()
                .generateCode(CodeGeneratorConstant.MODE_SOURCE_TEMPLATE,
                        Paths.get(targetDir.toString(),
                                robot.getRobotName() + CodeGeneratorConstant.MODE_SOURCE_SUFFIX),
                        rootHash);
    }

    private void generateTransitionCode(Path targetDir, CodeRobotWrapper robot) {
        Map<String, Object> rootHash = new HashMap<>();
        Set<String> eventList = new HashSet<String>();

        robot.getTransitionList().forEach(transition -> {
            transition.getTransition().getTransitionMap().values().forEach(ceList -> {
                ceList.forEach(ce -> eventList.add(ce.getEvent().getName()));
            });
        });

        rootHash.put(ControlTaskConstant.ROBOT_ID, robot.getRobotName());
        rootHash.put(ControlTaskConstant.TRANSITION_LIST, robot.getTransitionList());
        rootHash.put(ControlTaskConstant.EVENT_LIST, eventList);

        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.TRANSITION_HEADER_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.TRANSITION_HEADER_SUFFIX),
                rootHash);
        FTLHandler.getInstance().generateCode(CodeGeneratorConstant.TRANSITION_SOURCE_TEMPLATE,
                Paths.get(targetDir.toString(),
                        robot.getRobotName() + CodeGeneratorConstant.TRANSITION_SOURCE_SUFFIX),
                rootHash);
    }

    private void copyCodeRelatedControlTask(Path targetDir) {
        try {
            LocalFileCopier.copyFile(CodeGeneratorConstant.CONTROL_TASK_CODE,
                    Paths.get(targetDir.toString(), CodeGeneratorConstant.CONTROL_TASK_CIC));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

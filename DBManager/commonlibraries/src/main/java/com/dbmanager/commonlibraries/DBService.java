package com.dbmanager.commonlibraries;

import java.util.ArrayList;
import java.util.List;
import com.dbmanager.commonlibraries.Mapper.ActionImplMapper;
import com.dbmanager.commonlibraries.Mapper.ActionMapper;
import com.dbmanager.commonlibraries.Mapper.ArchitectureMapper;
import com.dbmanager.commonlibraries.Mapper.ControlStrategyMapper;
import com.dbmanager.commonlibraries.Mapper.GroupActionMapper;
import com.dbmanager.commonlibraries.Mapper.RobotImplMapper;
import com.dbmanager.commonlibraries.Mapper.RobotMapper;
import com.dbmanager.commonlibraries.Mapper.TaskMapper;
import com.dbmanager.commonlibraries.Mapper.VariableMapper;
import com.dbmanager.datastructure.action.Action;
import com.dbmanager.datastructure.action.ActionImpl;
import com.dbmanager.datastructure.action.GroupAction;
import com.dbmanager.datastructure.architecture.Architecture;
import com.dbmanager.datastructure.controlstrategy.ControlStrategy;
import com.dbmanager.datastructure.robot.Robot;
import com.dbmanager.datastructure.robot.RobotImpl;
import com.dbmanager.datastructure.task.Task;
import com.dbmanager.datastructure.variable.Variable;
import com.dbmanager.datastructure.variable.VariableDirection;

public class DBService {

    private static DAO dbDao;

    private DBService() {}

    static {
        dbDao = DAO.getInstance();
    }

    public static void initializeDB(String ip, int port, String user, String pwd, String dbName) {
        dbDao.initializeDB(ip, port, user, pwd, dbName);
    }

    public static boolean isExistentRobot(String robotClass) {
        return dbDao.getRobot(robotClass) != null;
    }

    public static boolean isExistentAction(String actionName) {
        return dbDao.getAction(actionName) != null;
    }

    public static boolean isExistentGroupAction(String groupActionName) {
        return dbDao.getGroupAction(groupActionName) != null;
    }

    public static boolean isExistentAction(String robotClass, String actionName) {
        return dbDao.getActionImpl(robotClass, actionName) != null;
    }

    public static boolean isExistentVariable(String variableName) {
        return dbDao.getVariable(variableName) != null;
    }

    public static Action getAction(String actionName) {
        return ActionMapper.mapToAction(dbDao.getAction(actionName));
    }

    public static GroupAction getGroupAction(String actionName) {
        return GroupActionMapper.mapToGroupAction(dbDao.getGroupAction(actionName));
    }

    public static ActionImpl getActionImpl(String actionImplId) {
        return ActionImplMapper.mapToActionImpl(dbDao.getActionImpl(actionImplId));
    }

    public static Task getTask(String taskId) {
        return TaskMapper.mapToTask(dbDao.getTask(taskId));
    }

    public static Variable getVariableOfAction(String actionName, int index,
            VariableDirection direction) {
        Action action = ActionMapper.mapToAction(dbDao.getAction(actionName));
        switch (direction) {
            case INPUT:
                return VariableMapper
                        .mapToVariable(dbDao.getVariable(action.getInputList().get(index)));
            case OUTPUT:
                return VariableMapper
                        .mapToVariable(dbDao.getVariable(action.getOutputList().get(index)));
            default:
                return null;
        }
    }

    private static Variable getVariableOfGroupAction(GroupAction groupAction, int index,
            VariableDirection direction) {
        switch (direction) {
            case INPUT:
                return VariableMapper
                        .mapToVariable(dbDao.getVariable(groupAction.getInputList().get(index)));
            case OUTPUT:
                return VariableMapper
                        .mapToVariable(dbDao.getVariable(groupAction.getOutputList().get(index)));
            case SHARED:
                return VariableMapper.mapToVariable(
                        dbDao.getVariable(groupAction.getSharedDataList().get(index)));
            default:
                return null;
        }
    }

    public static Variable getVariableOfGroupAction(String actionName, int index,
            VariableDirection direction) {
        GroupAction groupAction =
                GroupActionMapper.mapToGroupAction(dbDao.getGroupAction(actionName));
        return getVariableOfGroupAction(groupAction, index, direction);

    }

    public static List<Variable> getVariableListOfGroupAction(String actionName,
            VariableDirection direction) {
        GroupAction groupAction =
                GroupActionMapper.mapToGroupAction(dbDao.getGroupAction(actionName));
        List<Variable> dbVariableList = new ArrayList<Variable>();
        int length = 0;
        switch (direction) {
            case INPUT:
                length = groupAction.getInputList().size();
                break;
            case OUTPUT:
                length = groupAction.getOutputList().size();
                break;
            case SHARED:
                length = groupAction.getSharedDataList().size();
                break;
            default:
                length = 0;
        }
        for (int i = 0; i < length; i++) {
            dbVariableList.add(getVariableOfGroupAction(groupAction, i, direction));
        }
        return dbVariableList;
    }

    public static Variable getVariable(String variableName) {
        return VariableMapper.mapToVariable(dbDao.getVariable(variableName));
    }

    public static ControlStrategy getStrategy(String strategyId) {
        return ControlStrategyMapper.mapToStrategy(dbDao.getStrategy(strategyId));
    }

    public static Architecture getArchitecture(String deviceName) {
        return ArchitectureMapper.mapToArchitecture(dbDao.getArchitecture(deviceName));
    }

    public static RobotImpl getRobotImpl(String robotId) {
        return RobotImplMapper.mapToRobotImpl(dbDao.getRobotImpl(robotId));
    }

    public static Robot getRobot(String robotClass) {
        return RobotMapper.mapToRobot(dbDao.getRobot(robotClass));
    }

    public static void close() {
        dbDao.close();
    }
}

package com.dbmanager.commonlibraries;

import java.util.ArrayList;
import java.util.List;
import com.dbmanager.commonlibraries.Mapper.ActionImplMapper;
import com.dbmanager.commonlibraries.Mapper.ActionMapper;
import com.dbmanager.commonlibraries.Mapper.ArchitectureMapper;
import com.dbmanager.commonlibraries.Mapper.ControlStrategyMapper;
import com.dbmanager.commonlibraries.Mapper.GroupingAlgorithmMapper;
import com.dbmanager.commonlibraries.Mapper.LeaderSelectionAlgorithmMapper;
import com.dbmanager.commonlibraries.Mapper.ResourceMapper;
import com.dbmanager.commonlibraries.Mapper.RobotImplMapper;
import com.dbmanager.commonlibraries.Mapper.RobotMapper;
import com.dbmanager.commonlibraries.Mapper.SimulationDeviceMapper;
import com.dbmanager.commonlibraries.Mapper.TaskMapper;
import com.dbmanager.commonlibraries.Mapper.VariableMapper;
import com.dbmanager.datastructure.action.Action;
import com.dbmanager.datastructure.action.ActionImpl;
import com.dbmanager.datastructure.architecture.Architecture;
import com.dbmanager.datastructure.controlstrategy.ControlStrategy;
import com.dbmanager.datastructure.groupingalgorithm.GroupingAlgorithm;
import com.dbmanager.datastructure.leaderselectionalgorithm.LeaderSelectionAlgorithm;
import com.dbmanager.datastructure.resource.Resource;
import com.dbmanager.datastructure.robot.Robot;
import com.dbmanager.datastructure.robot.RobotImpl;
import com.dbmanager.datastructure.simulationdevice.SimulationDevice;
import com.dbmanager.datastructure.task.Task;
import com.dbmanager.datastructure.variable.Variable;
import com.dbmanager.datastructure.variable.VariableDirection;

public class DBService {

    private static HiSARMDAO dbDao;

    private DBService() {}

    static {
        dbDao = CouchDBDAO.getInstance();
    }

    public static void initializeDB(String ip, int port, String user, String epwd, String pwd,
            String dbName) {
        dbDao.initializeDB(ip, port, user, epwd, pwd, dbName);
    }

    public static boolean isExistentRobot(String robotClass) {
        return dbDao.getRobot(robotClass) != null;
    }

    public static boolean isExistentAction(String actionName) {
        return dbDao.getAction(actionName) != null;
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
            case SHARED:
                return VariableMapper.mapToVariable(
                        dbDao.getVariable(action.getGroupAction().getSharedDataList().get(index)));
            default:
                return null;
        }
    }

    public static List<Variable> getVariableListOfAction(String actionName,
            VariableDirection direction) {
        Action action = ActionMapper.mapToAction(dbDao.getAction(actionName));
        List<Variable> dbVariableList = new ArrayList<Variable>();
        int length = 0;
        switch (direction) {
            case INPUT:
                length = action.getInputList().size();
                break;
            case OUTPUT:
                length = action.getOutputList().size();
                break;
            case SHARED:
                length = action.getGroupAction().getSharedDataList().size();
                break;
            default:
                length = 0;
        }
        for (int i = 0; i < length; i++) {
            dbVariableList.add(getVariableOfAction(actionName, i, direction));
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

    public static Resource getResource(String robotClass, String resourceId) {
        return ResourceMapper.mapToResource(dbDao.getResource(robotClass, resourceId));
    }

    public static GroupingAlgorithm getGroupingAlgorithm(String groupingId) {
        return GroupingAlgorithmMapper
                .mapToGroupingAlgorithm(dbDao.getGroupingAlgorithm(groupingId));
    }

    public static LeaderSelectionAlgorithm getLeaderSelectionAlgorithm(String leaderSelectionId) {
        return LeaderSelectionAlgorithmMapper.mapToLeaderSelectionAlgorithm(
                dbDao.getLeaderSelectionAlgorithm(leaderSelectionId));
    }

    public static SimulationDevice getSimulationDevice(String deviceId) {
        return SimulationDeviceMapper.mapToSimulationDevice(dbDao.getSimulationDevice(deviceId));
    }

    public static void close() {
        dbDao.close();
    }
}

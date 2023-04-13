package com.strategy.strategymaker;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.dbmanager.commonlibraries.DBService;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import com.strategy.strategydatastructure.util.CompileTimeAllocatorInterface;
import com.strategy.strategydatastructure.wrapper.GroupingAlgorithmWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;

public class GroupAllocator {
    public static String makeGroupKey(String previousKey, String choosedGroup) {
        return previousKey + "_" + choosedGroup;
    }

    public static String makeTransitionId(String previousKey, TransitionWrapper transition) {
        return previousKey + "_" + transition.getTransition().getName();
    }

    public static String makeModeId(String previousKey, ModeWrapper mode) {
        return previousKey + "_" + mode.getMode().getName();
    }

    public static void allocateGroup(MissionWrapper mission, List<RobotImplWrapper> robotList,
            AdditionalInfo additionalInfo) {
        try {
            GroupingAlgorithmWrapper groupingAlgorithm = new GroupingAlgorithmWrapper();
            groupingAlgorithm.setGroupingAlgorithm(
                    DBService.getGroupingAlgorithm(additionalInfo.getGroupingAlgorithm()));
            groupingAlgorithm.setRunTimeTask(
                    DBService.getTask(groupingAlgorithm.getGroupingAlgorithm().getRunTimeTask()));
            robotList.forEach(r -> r.setGroupingAlgorithm(groupingAlgorithm));

            Path compileTimeAllocator = Paths.get(additionalInfo.getTaskServerPrefix(),
                    groupingAlgorithm.getGroupingAlgorithm().getCompileTimeFile());
            URL url = new URL("file:" + compileTimeAllocator.toString());
            URLClassLoader classLoader = new URLClassLoader(new URL[] {url});
            Class<?> clazz = classLoader
                    .loadClass(groupingAlgorithm.getGroupingAlgorithm().getCompileTimeClass());
            CompileTimeAllocatorInterface allocator =
                    (CompileTimeAllocatorInterface) clazz.getDeclaredConstructor().newInstance();
            robotList = allocator.allocate(robotList, mission);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

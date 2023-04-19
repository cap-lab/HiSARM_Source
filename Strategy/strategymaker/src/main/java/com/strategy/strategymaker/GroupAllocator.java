package com.strategy.strategymaker;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import com.dbmanager.commonlibraries.DBService;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import com.strategy.strategydatastructure.util.CompileTimeAllocatorInterface;
import com.strategy.strategydatastructure.wrapper.GroupingAlgorithmWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;

public class GroupAllocator {
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
            Map<String, Map<String, Integer>> groupMap = allocator.allocate(robotList, mission);
            robotList.forEach(r -> r.setGroupMap(groupMap.get(r.getRobot().getRobotId())));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

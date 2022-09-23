package com.strategy.strategymaker;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dbmanager.commonlibraries.DBService;
import com.dbmanager.datastructure.robot.RobotImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.RobotWrapper;
import com.scriptparser.parserdatastructure.wrapper.TeamWrapper;
import com.strategy.strategydatastructure.wrapper.RobotImplWrapper;
import com.strategy.strategydatastructure.wrapper.RobotTypeWrapper;
import com.strategy.strategydatastructure.wrapper.StrategyWrapper;
import com.strategy.strategymaker.additionalinfo.AdditionalInfo;

public class StrategyMaker {
    private StrategyWrapper strategy = new StrategyWrapper();


    private AdditionalInfo parsingAdditionalInfo(String additionalInfoFile) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(Paths.get(additionalInfoFile).toFile(), AdditionalInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void strategyMake(MissionWrapper mission, String additionalInfoFile) {
        AdditionalInfo additionalInfo = parsingAdditionalInfo(additionalInfoFile);

        strategy.setRobotList(RobotInfoMaker.makeRobotImplList(mission, additionalInfo));
    }
}

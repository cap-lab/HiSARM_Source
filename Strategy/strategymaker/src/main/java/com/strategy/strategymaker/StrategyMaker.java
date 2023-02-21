package com.strategy.strategymaker;

import java.nio.file.Paths;
import com.dbmanager.commonlibraries.DBService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.additionalinfo.AdditionalInfo;
import com.strategy.strategydatastructure.additionalinfo.DatabaseInfo;
import com.strategy.strategydatastructure.wrapper.StrategyWrapper;

public class StrategyMaker {


    private AdditionalInfo parsingAdditionalInfo(String additionalInfoFile) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(Paths.get(additionalInfoFile).toFile(), AdditionalInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public StrategyWrapper strategyMake(MissionWrapper mission, String additionalInfoFile) {
        StrategyWrapper strategy = new StrategyWrapper();
        AdditionalInfo additionalInfo = parsingAdditionalInfo(additionalInfoFile);
        strategy.setAdditionalInfo(additionalInfo);
        DatabaseInfo dbInfo = additionalInfo.getDbInfo().get(0);
        DBService.initializeDB(dbInfo.getIp(), dbInfo.getPort(), dbInfo.getUserName(),
                dbInfo.getEncryptedPassword(), dbInfo.getPassword(), dbInfo.getDbName());
        strategy.setRobotList(RobotInfoMaker.makeRobotImplList(mission, additionalInfo));
        GroupAllocator.allocateGroup(mission, strategy.getRobotList());
        ActionTypeInfoMaker.makeActionTypeList(mission, strategy.getRobotList());
        ControlStrategyInfoMaker.makeControlStrategyList(additionalInfo, strategy.getRobotList());
        VariableInfoMaker.makeVariableInfoList(mission, additionalInfo, strategy.getRobotList());
        AdditionalTaskInfoMaker.makeAddtionalTaskInfo(strategy.getRobotList());
        return strategy;
    }
}

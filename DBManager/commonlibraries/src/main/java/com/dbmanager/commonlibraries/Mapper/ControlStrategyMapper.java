package com.dbmanager.commonlibraries.Mapper;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.controlstrategy.ControlStrategy;
import com.dbmanager.datastructure.controlstrategy.ControlStrategyElement;

public class ControlStrategyMapper {
    private static List<ControlStrategyElement> mapToStrategy(List<Document> documentList) {
        List<ControlStrategyElement> strategyImplList = new ArrayList<ControlStrategyElement>();

        for (Document document : documentList) {
            ControlStrategyElement strategyImpl = new ControlStrategyElement();

            strategyImpl.setCondition(document.getString("condition"));
            strategyImpl.setActionImplId(document.getString("actionImplId"));

            strategyImplList.add(strategyImpl);
        }

        return strategyImplList;
    }

    public static ControlStrategy mapToStrategy(Bson bson) {
        ControlStrategy strategy = new ControlStrategy();
        Document document = (Document) bson;
        try {
            strategy.setActionName(document.getString("ActionName"));
            strategy.setRobotClass(document.getString("RobotClass"));
            strategy.setStrategyImplList(mapToStrategy((List<Document>) document.get("Impl")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strategy;
    }
}

package com.strategy.strategymaker;

import com.scriptparser.parser.Parser;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;

public class Main {

    public static void main(String[] args) {
        String scriptFile = args[0];
        Parser executer = new Parser();
        MissionWrapper mission = executer.parseScript(scriptFile);
        String additionalInfoFile = args[1];
        StrategyMaker strategyMaker = new StrategyMaker();
        strategyMaker.strategyMake(mission, additionalInfoFile);
    }
}

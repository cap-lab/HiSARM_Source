package com.metadata.metadatagenerator;

import com.scriptparser.parser.Parser;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.strategy.strategydatastructure.wrapper.StrategyWrapper;
import com.strategy.strategymaker.StrategyMaker;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        MissionWrapper mission = parser.parseScript(args[0]);
        StrategyMaker strategyMaker = new StrategyMaker();
        StrategyWrapper strategy = strategyMaker.strategyMake(mission, args[1]);
        MetadataGenerator generator = new MetadataGenerator();
        generator.metadataGenerate(mission, strategy, strategy.getAdditionalInfo(), null);
    }
}

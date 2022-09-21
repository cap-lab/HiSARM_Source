package com.scriptparser.parser;

import com.scriptparser.connector.StatementConnector;
import com.scriptparser.grammar.ScriptListenerApp;
import com.scriptparser.grammar.generated.ScriptLexer;
import com.scriptparser.grammar.generated.ScriptParser;
import com.scriptparser.parserdatastructure.wrapper.CatchEventWrapper;
import com.scriptparser.parserdatastructure.wrapper.GroupWrapper;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Parser {

    private ScriptListenerApp walkthrough(String fileName, Boolean isScript) throws IOException {
        ScriptLexer lex = new ScriptLexer(CharStreams.fromFileName(fileName));
        CommonTokenStream ctx = new CommonTokenStream(lex);
        ScriptParser parser = new ScriptParser(ctx);
        ParseTree tree;
        if (isScript == true) {
            tree = parser.script();
        } else {
            tree = parser.service_define();
        }
        ParseTreeWalker walker = new ParseTreeWalker();
        ScriptListenerApp listener = new ScriptListenerApp();
        walker.walk(listener, tree);
        return listener;
    }

    private void connectStatements(MissionWrapper mission) {
        for (ServiceWrapper service : mission.getServiceList()) {
            StatementConnector.ConnectStatement(service);
        }
    }

    private void connectServiceToMode(MissionWrapper mission) throws Exception {
        for (ModeWrapper mode : mission.getModeList()) {
            for (ParallelServiceWrapper pService : mode.getServiceList()) {
                pService.setService(
                        mission.getService(pService.getService().getService().getName()));
            }
        }
    }

    private void connectTransitionToMode(MissionWrapper mission) throws Exception {
        for (ModeWrapper mode : mission.getModeList()) {
            for (GroupWrapper group : mode.getGroupList()) {
                group.getModeTransition().setModeTransition(mission.getTransition(
                        group.getModeTransition().getModeTransition().getTransition().getName()));
            }
        }
    }

    private void connectModeToTransition(MissionWrapper mission) throws Exception {
        for (TransitionWrapper transition : mission.getTransitionList()) {
            transition.getDefaultMode().setMode(
                    mission.getMode(transition.getDefaultMode().getMode().getMode().getName()));
            Map<ModeWrapper, List<CatchEventWrapper>> replace = new HashMap<>();
            for (ModeWrapper bMode : transition.getTransitionMap().keySet()) {
                ModeWrapper key = mission.getMode(bMode.getMode().getName());
                for (CatchEventWrapper ce : transition.getTransitionMap().get(bMode)) {
                    if (ce.getMode().getMode().getMode().getName().equals("FINISH")
                            || ce.getMode().getMode().getMode().getName().equals("PREVIOUS_MODE")) {
                        continue;
                    }
                    ce.getMode()
                            .setMode(mission.getMode(ce.getMode().getMode().getMode().getName()));
                }
                replace.put(key, transition.getTransitionMap().get(bMode));
            }
            transition.setTransitionMap(replace);
        }
    }

    public MissionWrapper parseScript(String fileName) {
        try {
            MissionWrapper mission = walkthrough(fileName, true).returnMission();
            connectStatements(mission);
            connectServiceToMode(mission);
            connectTransitionToMode(mission);
            connectModeToTransition(mission);
            return mission;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MissionWrapper parseService(String fileName) {
        try {
            MissionWrapper mission = walkthrough(fileName, false).returnMission();
            connectStatements(mission);
            return mission;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

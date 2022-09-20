package com.scriptparser.parser;

import com.scriptparser.connector.StatementConnector;
import com.scriptparser.grammar.ScriptListenerApp;
import com.scriptparser.grammar.generated.ScriptLexer;
import com.scriptparser.grammar.generated.ScriptParser;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import java.io.IOException;
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

    public MissionWrapper ParseScript(String fileName) {
        try {
            MissionWrapper mission = walkthrough(fileName, true).returnMission();
            for (ServiceWrapper service : mission.getServiceList()) {
                StatementConnector.ConnectStatement(service);
            }
            return mission;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MissionWrapper ParseService(String fileName) {
        try {
            MissionWrapper mission = walkthrough(fileName, false).returnMission();
            for (ServiceWrapper service : mission.getServiceList()) {
                StatementConnector.ConnectStatement(service);
            }
            return mission;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

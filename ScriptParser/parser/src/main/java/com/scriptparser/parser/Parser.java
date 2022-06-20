package com.scriptparser.parser;

import com.scriptparser.grammar.ScriptListenerApp;
import com.scriptparser.grammar.generated.ScriptLexer;
import com.scriptparser.grammar.generated.ScriptParser;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Parser {
    public MissionWrapper ParseScript(String fileName) {
        try {
            ScriptLexer lex = new ScriptLexer(CharStreams.fromFileName(fileName));
            CommonTokenStream ctx = new CommonTokenStream(lex);
            ScriptParser parser = new ScriptParser(ctx);
            ParseTree tree = parser.script();

            ParseTreeWalker walker = new ParseTreeWalker();
            ScriptListenerApp listener = new ScriptListenerApp();
            walker.walk(listener, tree);
            return listener.returnMission();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MissionWrapper ParseService(String fileName) {
        try {
            ScriptLexer lex = new ScriptLexer(CharStreams.fromFileName(fileName));
            CommonTokenStream ctx = new CommonTokenStream(lex);
            ScriptParser parser = new ScriptParser(ctx);
            ParseTree tree = parser.service_define();

            ParseTreeWalker walker = new ParseTreeWalker();
            ScriptListenerApp listener = new ScriptListenerApp();
            walker.walk(listener, tree);
            return listener.returnMission();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

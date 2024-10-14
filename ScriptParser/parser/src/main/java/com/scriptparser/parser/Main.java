package com.scriptparser.parser;

public class Main {

    private static void parsingScript(String scriptFile) {
        Parser executer = new Parser();
        executer.parseScript(scriptFile);
    }

    public static void main(String[] args) {
        parsingScript(args[0]);
    }
}

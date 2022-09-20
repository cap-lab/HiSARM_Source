package com.scriptparser.grammar;

import java.util.List;
import com.scriptparser.grammar.generated.ScriptParser;
import com.scriptparser.parserdatastructure.entity.common.Condition;
import com.scriptparser.parserdatastructure.entity.common.Event;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.entity.common.Time;
import com.scriptparser.parserdatastructure.entity.statement.ActionStatement;
import com.scriptparser.parserdatastructure.entity.statement.CommunicationalStatement;
import com.scriptparser.parserdatastructure.entity.statement.ConditionalStatement;
import com.scriptparser.parserdatastructure.entity.statement.DummyStatement;
import com.scriptparser.parserdatastructure.entity.statement.ThrowStatement;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.scriptparser.parserdatastructure.util.KeyValueList;

public class StatementFactory {

    private static ThrowStatement throwStatement(KeyValueList<Integer, Object> infoList) {
        Event event = infoList.findFirst(ScriptParser.RULE_event, Event.class);
        Boolean broadcast = infoList.findFirst(ScriptParser.RULE_broadcast_flag, Boolean.class);

        return ThrowStatement.builder().event(event)
                .broadcast(broadcast == null ? Boolean.FALSE : broadcast)
                .statementType(StatementType.THROW).build();
    }

    private static ConditionalStatement ifStatement(KeyValueList<Integer, Object> infoList) {
        Condition condition = infoList.findFirst(ScriptParser.RULE_condition_list, Condition.class);

        return ConditionalStatement.builder().condition(condition).statementType(StatementType.IF)
                .build();
    }

    private static DummyStatement elseStatement(KeyValueList<Integer, Object> infoList) {
        return DummyStatement.builder().statementType(StatementType.ELSE).build();
    }

    private static ConditionalStatement loopStatement(KeyValueList<Integer, Object> infoList) {
        Condition condition = infoList.findFirst(ScriptParser.RULE_condition_list, Condition.class);
        Time time = infoList.findFirst(ScriptParser.RULE_time, Time.class);

        return ConditionalStatement.builder().period(time).condition(condition)
                .statementType(StatementType.LOOP).build();
    }

    private static ConditionalStatement repeatStatement(KeyValueList<Integer, Object> infoList) {
        Condition condition = infoList.findFirst(ScriptParser.RULE_condition_list, Condition.class);
        Time time = infoList.findFirst(ScriptParser.RULE_time, Time.class);

        return ConditionalStatement.builder().period(time).condition(condition)
                .statementType(StatementType.REPEAT).build();
    }

    private static CommunicationalStatement sendStatement(KeyValueList<Integer, Object> infoList) {
        String targetTeam = infoList.findFirst(ScriptParser.RULE_target_team, String.class);
        Identifier variable = infoList.findFirst(ScriptParser.RULE_variable, Identifier.class);

        return CommunicationalStatement.builder().counterTeam(targetTeam).message(variable)
                .statementType(StatementType.SEND).build();
    }

    private static CommunicationalStatement publishStatement(
            KeyValueList<Integer, Object> infoList) {
        String targetTeam = infoList.findFirst(ScriptParser.RULE_target_team, String.class);
        Identifier variable = infoList.findFirst(ScriptParser.RULE_variable, Identifier.class);

        return CommunicationalStatement.builder().counterTeam(targetTeam).message(variable)
                .statementType(StatementType.PUBLISH).build();
    }

    private static CommunicationalStatement receiveStatement(
            KeyValueList<Integer, Object> infoList) {
        String targetTeam = infoList.findFirst(ScriptParser.RULE_target_team, String.class);
        Identifier variable = infoList.findFirst(ScriptParser.RULE_variable, Identifier.class);
        Identifier output = infoList.findFirst(ScriptParser.RULE_output, Identifier.class);;

        return CommunicationalStatement.builder().counterTeam(targetTeam).message(variable)
                .output(output).statementType(StatementType.PUBLISH).build();
    }

    private static CommunicationalStatement subscribeStatement(
            KeyValueList<Integer, Object> infoList) {
        String targetTeam = infoList.findFirst(ScriptParser.RULE_target_team, String.class);
        Identifier variable = infoList.findFirst(ScriptParser.RULE_variable, Identifier.class);
        Identifier output = infoList.findFirst(ScriptParser.RULE_output, Identifier.class);;

        return CommunicationalStatement.builder().counterTeam(targetTeam).message(variable)
                .output(output).statementType(StatementType.PUBLISH).build();
    }

    private static ActionStatement actionStatement(KeyValueList<Integer, Object> infoMap) {
        List<Identifier> outputList =
                infoMap.findFirst(ScriptParser.RULE_identifier_list, List.class);
        String actionName = infoMap.findFirst(ScriptParser.RULE_action_name, String.class);
        List<IdentifierSet> inputList =
                infoMap.findFirst(ScriptParser.RULE_item_set_list, List.class);
        Time deadline = infoMap.findFirst(ScriptParser.RULE_time, Time.class);

        return ActionStatement.builder().outputList(outputList).actionName(actionName)
                .inputList(inputList).deadline(deadline).build();
    }

    private static DummyStatement compoundInStatement(KeyValueList<Integer, Object> infoMap) {
        return DummyStatement.builder().statementType(StatementType.COMPOUND_IN).build();
    }

    private static DummyStatement compoundOutStatement(KeyValueList<Integer, Object> infoMap) {
        return DummyStatement.builder().statementType(StatementType.COMPOUND_OUT).build();
    }

    private static DummyStatement finishStatement(KeyValueList<Integer, Object> infoMap) {
        return DummyStatement.builder().statementType(StatementType.FINISH).build();
    }

    public static StatementWrapper makeStatement(StatementType type,
            KeyValueList<Integer, Object> infoList) {
        StatementWrapper statement = new StatementWrapper();
        switch (type) {
            case ACTION:
                statement.setStatement(actionStatement(infoList));
                break;
            case IF:
                statement.setStatement(ifStatement(infoList));
                break;
            case ELSE:
                statement.setStatement(elseStatement(infoList));
                break;
            case LOOP:
                statement.setStatement(loopStatement(infoList));
                break;
            case REPEAT:
                statement.setStatement(repeatStatement(infoList));
                break;
            case PUBLISH:
                statement.setStatement(publishStatement(infoList));
                break;
            case RECEIVE:
                statement.setStatement(receiveStatement(infoList));
                break;
            case SEND:
                statement.setStatement(sendStatement(infoList));
                break;
            case SUBSCRIBE:
                statement.setStatement(subscribeStatement(infoList));
                break;
            case THROW:
                statement.setStatement(throwStatement(infoList));
                break;
            case COMPOUND_IN:
                statement.setStatement(compoundInStatement(infoList));
                break;
            case COMPOUND_OUT:
                statement.setStatement(compoundOutStatement(infoList));
                break;
            case FINISH:
                statement.setStatement(finishStatement(infoList));
                break;
            default:
                statement.setStatement(null);
        }
        return statement;
    }

}

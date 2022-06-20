package com.scriptparser.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import com.scriptparser.grammar.generated.ScriptBaseListener;
import com.scriptparser.grammar.generated.ScriptParser;
import com.scriptparser.grammar.generated.ScriptParser.Mode_nameContext;
import com.scriptparser.parserdatastructure.entity.Group;
import com.scriptparser.parserdatastructure.entity.Mode;
import com.scriptparser.parserdatastructure.entity.Transition;
import com.scriptparser.parserdatastructure.entity.Robot;
import com.scriptparser.parserdatastructure.entity.Service;
import com.scriptparser.parserdatastructure.entity.Team;
import com.scriptparser.parserdatastructure.entity.common.Condition;
import com.scriptparser.parserdatastructure.entity.common.Event;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.entity.common.IdentifierSet;
import com.scriptparser.parserdatastructure.entity.common.Time;
import com.scriptparser.parserdatastructure.enumeration.IdentifierType;
import com.scriptparser.parserdatastructure.enumeration.Operator;
import com.scriptparser.parserdatastructure.enumeration.Priority;
import com.scriptparser.parserdatastructure.enumeration.StatementType;
import com.scriptparser.parserdatastructure.enumeration.TimeUnit;
import com.scriptparser.parserdatastructure.wrapper.CatchEventWrapper;
import com.scriptparser.parserdatastructure.wrapper.GroupModeTransitionWrapper;
import com.scriptparser.parserdatastructure.wrapper.GroupWrapper;
import com.scriptparser.parserdatastructure.wrapper.MissionWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionWrapper;
import com.scriptparser.parserdatastructure.wrapper.ModeWrapper;
import com.scriptparser.parserdatastructure.wrapper.ParallelServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.RobotWrapper;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.scriptparser.parserdatastructure.wrapper.StatementWrapper;
import com.scriptparser.parserdatastructure.wrapper.TeamWrapper;
import com.scriptparser.parserdatastructure.wrapper.TransitionModeWrapper;
import com.scriptparser.parserdatastructure.util.KeyValue;
import com.scriptparser.parserdatastructure.util.KeyValueList;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ScriptListenerApp extends ScriptBaseListener {

    private Stack<Object> objectStack = new Stack<>();
    private List<String> errorList = new ArrayList<>();
    private List<ServiceWrapper> serviceList = new ArrayList<>();
    private List<TeamWrapper> teamList = new ArrayList<>();
    private List<ModeWrapper> modeList = new ArrayList<>();
    private List<TransitionWrapper> transitionList = new ArrayList<>();

    public ScriptListenerApp() {
        super();
    }

    private KeyValueList<Integer, Object> extractInfoUntilNull() {
        KeyValue<Integer, Object> info = null;
        KeyValueList<Integer, Object> infoList = new KeyValueList<>();
        do {
            if (!objectStack.empty()) {
                info = (KeyValue<Integer, Object>) objectStack.pop();
            } else {
                info = null;
            }
            if (info != null) {
                infoList.add(0, info);
            }
        } while (info != null);
        return infoList;
    }

    private KeyValue<Integer, Object> makeInfo(Integer key, Object value) {
        return new KeyValue<>(key, value);
    }

    private KeyValue<Integer, Object> makeInfo(ParserRuleContext ctx, Object value) {
        return makeInfo(ctx.getRuleIndex(), value);
    }

    @Override
    public void enterTeam_composition(ScriptParser.Team_compositionContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void exitRobot(ScriptParser.RobotContext ctx) {
        String robotName = ctx.robot_name().getText();
        String robotType = ctx.robot_type().getText();
        int robotCount = ctx.INTEGER() == null ? 1 : Integer.parseInt(ctx.INTEGER().getText());
        RobotWrapper robot = new RobotWrapper(
                Robot.builder().name(robotName).type(robotType).count(robotCount).build());
        objectStack.push(makeInfo(ctx, robot));
    }

    @Override
    public void exitTeam_composition(ScriptParser.Team_compositionContext ctx) {
        TeamWrapper team = new TeamWrapper(Team.builder().name(ctx.team_name().getText()).build());
        KeyValueList<Integer, Object> robots = extractInfoUntilNull();
        team.setRobotList(robots.values(RobotWrapper.class));
        teamList.add(team);
    }

    @Override
    public void enterService_define(ScriptParser.Service_defineContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void exitIdentifier_list(ScriptParser.Identifier_listContext ctx) {
        String[] strings =
                ctx.IDENTIFIER().stream().map(TerminalNode::getText).toArray(String[]::new);
        Identifier[] identifiers = Arrays.asList(strings).stream()
                .map(p -> Identifier.builder().id(p).type(IdentifierType.VARIABLE).build())
                .toArray(Identifier[]::new);
        objectStack.push(makeInfo(ctx, new ArrayList<Identifier>(Arrays.asList(identifiers))));
    }

    @Override
    public void exitParameter_list(ScriptParser.Parameter_listContext ctx) {
        KeyValue<Integer, List<Identifier>> identifierList =
                (KeyValue<Integer, List<Identifier>>) objectStack.pop();
        objectStack.push(makeInfo(ctx, identifierList.value));
    }

    private void pushStatement(StatementType type) {
        objectStack.push(makeInfo(ScriptParser.RULE_statement,
                StatementFactory.makeStatement(type, extractInfoUntilNull())));
    }

    @Override
    public void enterStatement(ScriptParser.StatementContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void exitEvent(ScriptParser.EventContext ctx) {
        objectStack.push(makeInfo(ctx, Event.builder().name(ctx.getText()).build()));
    }

    @Override
    public void exitBroadcast_flag(ScriptParser.Broadcast_flagContext ctx) {
        objectStack.push(makeInfo(ctx, Boolean.TRUE));
    }

    @Override
    public void exitThrow_statement(ScriptParser.Throw_statementContext ctx) {
        pushStatement(StatementType.THROW);
    }

    @Override
    public void enterCondition(ScriptParser.ConditionContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void exitVariable(ScriptParser.VariableContext ctx) {
        Identifier variable =
                Identifier.builder().id(ctx.getText()).type(IdentifierType.VARIABLE).build();
        objectStack.push(makeInfo(ctx, variable));
    }

    private boolean isConstant(String item) {
        return item.endsWith("\"") && item.startsWith("\"");
    }

    @Override
    public void exitItem(ScriptParser.ItemContext ctx) {
        Identifier identifier = null;
        if (isConstant(ctx.getText())) {
            identifier = Identifier.builder().id(ctx.getText().replaceAll("\"", ""))
                    .type(IdentifierType.CONSTANT).build();
        } else {
            identifier =
                    Identifier.builder().id(ctx.getText()).type(IdentifierType.VARIABLE).build();
        }
        objectStack.push(makeInfo(ctx, identifier));
    }

    @Override
    public void exitCondition(ScriptParser.ConditionContext ctx) {
        KeyValueList<Integer, Object> info = extractInfoUntilNull();
        try {
            Condition condition = Condition.builder()
                    .leftOperand(info.findFirst(ScriptParser.RULE_variable, Identifier.class))
                    .rightOperand(info.findFirst(ScriptParser.RULE_item, Identifier.class))
                    .operator(Operator.valueFrom(ctx.BIOP().getText())).isLeaf(true).build();
            objectStack.push(makeInfo(ctx, condition));
        } catch (Exception e) {
            errorList.add(e.getMessage());
        }
    }

    private Condition conditionList(ScriptParser.Condition_listContext ctx) {
        try {
            KeyValue<Integer, Condition> leftCon = (KeyValue<Integer, Condition>) objectStack.pop();
            KeyValue<Integer, Condition> rightCon =
                    (KeyValue<Integer, Condition>) objectStack.pop();
            return Condition.builder().left(leftCon.value).right(rightCon.value)
                    .operator(Operator.valueFrom(ctx.condition_operator().getText())).isLeaf(false)
                    .build();
        } catch (Exception e) {
            errorList.add(e.getMessage());
            return null;
        }
    }

    @Override
    public void exitCondition_list(ScriptParser.Condition_listContext ctx) {
        Condition condition = null;
        if (ctx.condition().isEmpty()) {
            condition = conditionList(ctx);
        } else {
            KeyValue<Integer, Condition> conditionList =
                    (KeyValue<Integer, Condition>) objectStack.pop();
            condition = conditionList.value;
        }
        objectStack.push(makeInfo(ctx, condition));
    }

    @Override
    public void exitIf_statement(ScriptParser.If_statementContext ctx) {
        pushStatement(StatementType.IF);
    }

    @Override
    public void exitElse_statement(ScriptParser.Else_statementContext ctx) {
        pushStatement(StatementType.ELSE);
    }

    @Override
    public void exitTime(ScriptParser.TimeContext ctx) {
        Time time = Time.builder().time(Integer.valueOf(ctx.INTEGER().getText()))
                .timeUnit(TimeUnit.valueOf(ctx.TIMEUNIT().getText())).build();
        objectStack.push(makeInfo(ctx, time));
    }

    @Override
    public void exitLoop_statement(ScriptParser.Loop_statementContext ctx) {
        pushStatement(StatementType.LOOP);
    }

    @Override
    public void exitTarget_team(ScriptParser.Target_teamContext ctx) {
        String targetTeam = ctx.getText();
        objectStack.push(makeInfo(ctx, targetTeam));
    }

    @Override
    public void exitSend_statement(ScriptParser.Send_statementContext ctx) {
        pushStatement(StatementType.SEND);
    }

    @Override
    public void exitPublish_statement(ScriptParser.Publish_statementContext ctx) {
        pushStatement(StatementType.PUBLISH);
    }

    @Override
    public void exitOutput(ScriptParser.OutputContext ctx) {
        Identifier output =
                Identifier.builder().id(ctx.getText()).type(IdentifierType.VARIABLE).build();
        objectStack.push(makeInfo(ctx, output));
    }

    @Override
    public void exitReceive_statement(ScriptParser.Receive_statementContext ctx) {
        pushStatement(StatementType.RECEIVE);
    }

    @Override
    public void exitSubscribe_statement(ScriptParser.Subscribe_statementContext ctx) {
        pushStatement(StatementType.SUBSCRIBE);
    }

    @Override
    public void exitAction_name(ScriptParser.Action_nameContext ctx) {
        String name = ctx.getText();
        objectStack.push(makeInfo(ctx, name));
    }

    @Override
    public void enterItem_set_list(ScriptParser.Item_set_listContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void enterItem_list(ScriptParser.Item_listContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void exitItem_list(ScriptParser.Item_listContext ctx) {
        KeyValueList<Integer, Object> items = extractInfoUntilNull();
        objectStack.push(makeInfo(ctx, items.values(Identifier.class)));
    }

    @Override
    public void exitItem_set(ScriptParser.Item_setContext ctx) {
        KeyValue<Integer, List<Identifier>> itemList =
                (KeyValue<Integer, List<Identifier>>) objectStack.pop();
        IdentifierSet itemSet = IdentifierSet.builder().identifierSet(itemList.value).build();
        objectStack.push(makeInfo(ctx, itemSet));
    }

    @Override
    public void exitItem_set_list(ScriptParser.Item_set_listContext ctx) {
        KeyValueList<Integer, Object> itemSets = extractInfoUntilNull();
        Iterator<KeyValue<Integer, Object>> iterator = itemSets.iterator();
        while (iterator.hasNext()) {
            KeyValue<Integer, Object> item = iterator.next();
            if (item.key.equals(ScriptParser.RULE_item)) {
                item.value = IdentifierSet.builder().identifier(Identifier.class.cast(item.value))
                        .build();
            }
        }
        objectStack.push(makeInfo(ctx, itemSets.values(IdentifierSet.class)));
    }

    @Override
    public void exitAction_statement(ScriptParser.Action_statementContext ctx) {
        pushStatement(StatementType.ACTION);
    }

    @Override
    public void enterCompound_statement(ScriptParser.Compound_statementContext ctx) {
        pushStatement(StatementType.COMPOUND_IN);
    }

    @Override
    public void exitCompound_statement(ScriptParser.Compound_statementContext ctx) {
        pushStatement(StatementType.COMPOUND_OUT);
    }

    @Override
    public void exitRepeat_statement(ScriptParser.Repeat_statementContext ctx) {
        pushStatement(StatementType.REPEAT);
    }

    @Override
    public void exitService_define(ScriptParser.Service_defineContext ctx) {
        ServiceWrapper service =
                new ServiceWrapper(Service.builder().name(ctx.service_name().getText()).build());
        KeyValueList<Integer, Object> infoList = extractInfoUntilNull();
        service.setParameterList(infoList.popFirst(ScriptParser.RULE_parameter_list, List.class));
        service.setStatement(infoList.values(ScriptParser.RULE_statement, StatementWrapper.class));
        serviceList.add(service);
    }

    @Override
    public void enterMode_define(ScriptParser.Mode_defineContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void enterGroup_behavior(ScriptParser.Group_behaviorContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void exitMin_count(ScriptParser.Min_countContext ctx) {
        int min = Integer.parseInt(ctx.INTEGER().getText());
        objectStack.push(makeInfo(ctx, min));
    }

    @Override
    public void exitProper_count(ScriptParser.Proper_countContext ctx) {
        int proper = Integer.parseInt(ctx.INTEGER().getText());
        objectStack.push(makeInfo(ctx, proper));
    }

    @Override
    public void exitTag_list(ScriptParser.Tag_listContext ctx) {
        String[] strings =
                ctx.IDENTIFIER().stream().map(TerminalNode::getText).toArray(String[]::new);
        Identifier[] identifiers = (Identifier[]) Arrays.asList(strings).stream()
                .map(p -> Identifier.builder().id(p).type(IdentifierType.TAG).build()).toArray();
        objectStack.push(makeInfo(ctx, new ArrayList<Identifier>(Arrays.asList(identifiers))));
    }

    @Override
    public void exitTag_condition(ScriptParser.Tag_conditionContext ctx) {
        Condition condition = null;
        if (ctx.tag_list() == null) {
            KeyValue<Integer, Condition> leftCon = (KeyValue<Integer, Condition>) objectStack.pop();
            KeyValue<Integer, Condition> rightCon =
                    (KeyValue<Integer, Condition>) objectStack.pop();
            condition = Condition.builder().left(leftCon.value).right(rightCon.value)
                    .operator(Operator.OR).build();
        } else {
            KeyValue<Integer, List<Identifier>> tagList =
                    (KeyValue<Integer, List<Identifier>>) objectStack.pop();
            condition = Condition.builder().tagList(tagList.value).build();
        }
        objectStack.push(makeInfo(ctx, condition));
    }

    @Override
    public void exitMode_transition(ScriptParser.Mode_transitionContext ctx) {
        Transition modeTransition =
                Transition.builder().name(ctx.mode_transition_name().getText()).build();
        TransitionWrapper modeTransitionWrapper = new TransitionWrapper(modeTransition);
        GroupModeTransitionWrapper groupModeTransition =
                new GroupModeTransitionWrapper(modeTransitionWrapper);
        if (ctx.item_set_list() != null) {
            groupModeTransition.setInputList(((KeyValue<Integer, List>) objectStack.pop()).value);
        }
        objectStack.push(makeInfo(ctx, groupModeTransition));
    }

    @Override
    public void exitGroup_behavior(ScriptParser.Group_behaviorContext ctx) {
        KeyValueList<Integer, Object> infoList = extractInfoUntilNull();
        GroupModeTransitionWrapper modeTransition = infoList
                .popFirst(ScriptParser.RULE_mode_transition, GroupModeTransitionWrapper.class);
        int min = 1;
        if (infoList.containKey(ScriptParser.RULE_min_count)) {
            min = infoList.popFirst(ScriptParser.RULE_min_count, Integer.class);
        }
        int proper = min;
        if (infoList.containKey(ScriptParser.RULE_proper_count)) {
            proper = infoList.popFirst(ScriptParser.RULE_proper_count, Integer.class);
        }
        Condition tagList = infoList.popFirst(ScriptParser.RULE_tag_condition, Condition.class);
        Group group = Group.builder().name(ctx.group_name().getText()).min(min).proper(proper)
                .others(false).tagList(tagList).build();
        GroupWrapper groupWrapper = new GroupWrapper();
        groupWrapper.setGroup(group);
        groupWrapper.setModeTransition(modeTransition);
        objectStack.push(makeInfo(ScriptParser.RULE_group_behavior, groupWrapper));
    }

    @Override
    public void exitOthers_behavior(ScriptParser.Others_behaviorContext ctx) {
        KeyValue<Integer, GroupModeTransitionWrapper> modeTransition =
                (KeyValue<Integer, GroupModeTransitionWrapper>) objectStack.pop();
        Group group = Group.builder().name(ctx.group_name().getText()).min(0).proper(0).others(true)
                .tagList(null).build();
        GroupWrapper groupWrapper = new GroupWrapper();
        groupWrapper.setGroup(group);
        groupWrapper.setModeTransition(modeTransition.value);
        objectStack.push(makeInfo(ScriptParser.RULE_group_behavior, groupWrapper));

    }

    @Override
    public void enterParallel_behavior(ScriptParser.Parallel_behaviorContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void enterService_mapping(ScriptParser.Service_mappingContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void exitPriority(ScriptParser.PriorityContext ctx) {
        Priority priority = Priority.valueOf(ctx.PRIORITY_ENUM().getText());
        objectStack.push(makeInfo(ctx, priority));
    }

    @Override
    public void exitService_mapping(ScriptParser.Service_mappingContext ctx) {
        KeyValueList<Integer, Object> infoList = extractInfoUntilNull();
        ParallelServiceWrapper service = new ParallelServiceWrapper(
                new ServiceWrapper(Service.builder().name(ctx.service_name().getText()).build()));
        service.setInputList(infoList.popFirst(ScriptParser.RULE_item_set_list, List.class));
        service.setPrioirty(infoList.popFirst(ScriptParser.RULE_priority, Priority.class));
        service.setTimeout(infoList.popFirst(ScriptParser.RULE_time, Time.class));
        objectStack.push(makeInfo(ctx, service));
    }

    @Override
    public void exitMode_define(ScriptParser.Mode_defineContext ctx) {
        KeyValueList<Integer, Object> infoList = extractInfoUntilNull();
        ModeWrapper mode = new ModeWrapper(Mode.builder().name(ctx.mode_name().getText()).build());
        mode.setParameterList(infoList.popFirst(ScriptParser.RULE_parameter_list, List.class));
        mode.setGroupList(infoList.popValues(ScriptParser.RULE_group_behavior, GroupWrapper.class));
        mode.setServiceList(infoList.popValues(ScriptParser.RULE_service_mapping,
                ParallelServiceWrapper.class));
        modeList.add(mode);
    }

    @Override
    public void exitMode_copy(ScriptParser.Mode_copyContext ctx) {
        Optional<ModeWrapper> parentMode = modeList.stream()
                .filter(m -> m.getMode().getName().equals(ctx.mode_name(0).getText())).findAny();
        if (parentMode.isPresent()) {
            Iterator<Mode_nameContext> iter = ctx.mode_name().iterator();
            iter.next();
            while (iter.hasNext()) {
                Mode_nameContext modeName = iter.next();
                ModeWrapper mode = new ModeWrapper(Mode.builder().name(modeName.getText()).build());
                mode.setGroupList(parentMode.get().getGroupList());
                mode.setParameterList(parentMode.get().getParameterList());
                mode.setServiceList(parentMode.get().getServiceList());
                modeList.add(mode);
            }
        } else {
            errorList.add(ctx.mode_name(0).getText() + " is not declared");
        }
    }

    @Override
    public void enterMode_transition_define(ScriptParser.Mode_transition_defineContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void enterTransition(ScriptParser.TransitionContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void exitMode_assign(ScriptParser.Mode_assignContext ctx) {
        TransitionModeWrapper mode = new TransitionModeWrapper();
        if (ctx.item_set_list() != null) {
            KeyValue<Integer, List<IdentifierSet>> inputList =
                    (KeyValue<Integer, List<IdentifierSet>>) objectStack.pop();
            mode.setInputList(inputList.value);
        }
        mode.setMode(new ModeWrapper(Mode.builder().name(ctx.mode_name().getText()).build()));
        objectStack.push(makeInfo(ctx, mode));
    }

    @Override
    public void exitCatch_statement(ScriptParser.Catch_statementContext ctx) {
        KeyValue<Integer, TransitionModeWrapper> mode =
                (KeyValue<Integer, TransitionModeWrapper>) objectStack.pop();
        CatchEventWrapper transition = new CatchEventWrapper();
        transition.setEvent(Event.builder().name(ctx.event().getText()).build());
        transition.setMode(mode.value);
        objectStack.push(makeInfo(ctx, transition));
    }

    @Override
    public void exitTransition(ScriptParser.TransitionContext ctx) {
        KeyValueList<Integer, Object> catchList = extractInfoUntilNull();
        KeyValue<ModeWrapper, List<CatchEventWrapper>> transition =
                new KeyValue<ModeWrapper, List<CatchEventWrapper>>(
                        new ModeWrapper(Mode.builder().name(ctx.mode_name().getText()).build()),
                        catchList.values(ScriptParser.RULE_catch_statement,
                                CatchEventWrapper.class));
        objectStack.push(makeInfo(ctx, transition));
    }

    @Override
    public void exitDefault_mode(ScriptParser.Default_modeContext ctx) {
        KeyValue<Integer, TransitionModeWrapper> mode =
                (KeyValue<Integer, TransitionModeWrapper>) objectStack.pop();
        objectStack.push(makeInfo(ctx, mode.value));
    }

    private TransitionWrapper makeTransition(String transitionName,
            KeyValueList<Integer, Object> infoList) {
        TransitionWrapper transition =
                new TransitionWrapper(Transition.builder().name(transitionName).build());
        transition.setDefaultMode(
                infoList.popFirst(ScriptParser.RULE_default_mode, TransitionModeWrapper.class));
        List<KeyValue> transitionList =
                infoList.values(ScriptParser.RULE_transition, KeyValue.class);
        Map<ModeWrapper, List<CatchEventWrapper>> transitionMap = new HashMap<>();
        for (KeyValue<ModeWrapper, List> tmp : transitionList) {
            transitionMap.put(tmp.key, tmp.value);
        }
        transition.setTransitionMap(transitionMap);
        return transition;
    }

    @Override
    public void exitMode_transition_define(ScriptParser.Mode_transition_defineContext ctx) {
        KeyValueList<Integer, Object> infoList = extractInfoUntilNull();
        TransitionWrapper transition =
                makeTransition(ctx.mode_transition_name().getText(), infoList);
        transition
                .setParameterList(infoList.popFirst(ScriptParser.RULE_parameter_list, List.class));
        transitionList.add(transition);
    }

    @Override
    public void enterMain_define(ScriptParser.Main_defineContext ctx) {
        objectStack.push(null);
    }

    @Override
    public void exitMain_define(ScriptParser.Main_defineContext ctx) {
        KeyValueList<Integer, Object> infoList = extractInfoUntilNull();
        TransitionWrapper transition = makeTransition(ctx.team_name().getText(), infoList);
        transitionList.add(transition);
    }

    public MissionWrapper returnMission() {
        MissionWrapper mission = new MissionWrapper();
        mission.setTeamList(teamList);
        mission.setModeList(modeList);
        mission.setTransitionList(transitionList);
        mission.setServiceList(serviceList);
        return mission;
    }
}

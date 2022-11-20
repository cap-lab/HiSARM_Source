package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.scriptparser.parserdatastructure.wrapper.ServiceWrapper;
import com.strategy.strategydatastructure.wrapper.ActionTypeWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeServiceWrapper {
    private ServiceWrapper service;
    private String serviceId;
    private String groupId;
    private List<CodeVariableWrapper> parameterList = new ArrayList<>();
    private List<CodeVariableWrapper> variableList = new ArrayList<>();
    private List<CodeStatementWrapper> statementList = new ArrayList<>();

    public static String makeServiceId(String modeId, String serviceId) {
        return modeId + "_" + serviceId;
    }

    public List<CodeActionWrapper> getActionList() {
        List<CodeActionWrapper> actionList = new ArrayList<>();
        statementList.forEach(st -> actionList.addAll(st.getActionList()));
        return actionList;
    }

    public Map<ActionTypeWrapper, List<CodeActionWrapper>> getActionMap() {
        Map<ActionTypeWrapper, List<CodeActionWrapper>> actionMap = new HashMap<>();
        List<CodeActionWrapper> actionList = getActionList();
        actionList.forEach(action -> {
            if (actionMap.containsKey(action.getActionTask().getActionImpl().getActionType())) {
                actionMap.get(action.getActionTask().getActionImpl().getActionType()).add(action);
            } else {
                List<CodeActionWrapper> list = new ArrayList<>();
                list.add(action);
                actionMap.put(action.getActionTask().getActionImpl().getActionType(), list);
            }
        });
        return actionMap;
    }

    public List<CodeStatementWrapper> getConditionStatementList() {
        List<CodeStatementWrapper> conditionStatementList = new ArrayList<>();
        statementList.forEach(st -> {
            if (st.getCondition() != null) {
                conditionStatementList.add(st);
            }
        });
        return conditionStatementList;
    }
}

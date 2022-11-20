package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.scriptparser.parserdatastructure.entity.Mode;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.util.ModeTransitionVisitor;
import com.scriptparser.parserdatastructure.util.VariableVisitor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModeWrapper {
    private Mode mode;
    private List<Identifier> parameterList = new ArrayList<>();
    private List<GroupWrapper> groupList = new ArrayList<>();
    private List<ParallelServiceWrapper> serviceList = new ArrayList<>();

    public ModeWrapper(Mode mode) {
        this.mode = mode;
    }

    public GroupWrapper getGroup(String groupName) throws Exception {
        Optional<GroupWrapper> group = getGroupList().stream()
                .filter(g -> g.getGroup().getName().equals(groupName)).findAny();
        if (group.isPresent()) {
            return group.get();
        } else {
            throw new Exception(
                    "No group whose name is " + groupName + " in the mode " + getMode().getName());
        }
    }

    public String makeModeId(String lastId) {
        return lastId + "_" + mode.getName();
    }

    public String makeGroupId(String lastGroupId, String currentGroupId) {
        return lastGroupId + "_" + currentGroupId;
    }

    public Map<String, String> makeArgumentMap(List<String> argumentList) {
        Map<String, String> argumentMap = new java.util.HashMap<>();
        if (parameterList != null) {
            for (int i = 0; i < parameterList.size(); i++) {
                argumentMap.put(parameterList.get(i).getId(), argumentList.get(i));
            }
        }
        return argumentMap;
    }

    public void visitMode(String lastId, String currentGroupId, List<String> visitedList,
            List<String> groupList, ModeTransitionVisitor visitor,
            VariableVisitor variableVisitor) {
        String id = makeModeId(lastId);
        if (visitedList != null) {
            if (visitedList.contains(id)) {
                return;
            } else {
                visitedList.add(id);
            }
        }
        if (visitor != null) {
            visitor.visitMode(this, id, currentGroupId);
        }
        for (ParallelServiceWrapper service : this.serviceList) {
            service.visitModeService(this, currentGroupId, variableVisitor);
        }
        for (GroupWrapper group : this.groupList) {
            String newGroupId = makeGroupId(currentGroupId, group.getGroup().getName());
            if (groupList != null) {
                if (!groupList.contains(newGroupId)) {
                    continue;
                }
            }
            group.getModeTransition().traverseModeTransition(id, newGroupId, this, visitedList,
                    groupList, visitor, variableVisitor);
        }
    }
}

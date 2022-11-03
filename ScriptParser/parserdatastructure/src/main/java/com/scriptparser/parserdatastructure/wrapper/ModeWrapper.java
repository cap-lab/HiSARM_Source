package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.scriptparser.parserdatastructure.entity.Mode;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
import com.scriptparser.parserdatastructure.util.ModeVisitor;
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

    public void visitMode(String lastId, String currentGroupId, List<String> visitedId,
            List<String> groupList, ModeVisitor visitor) {
        String id = makeModeId(lastId);
        if (visitedId.contains(id)) {
            return;
        } else {
            visitedId.add(id);
        }
        visitor.visitMode(this, id, currentGroupId);
        for (GroupWrapper group : this.groupList) {
            String newGroupId = makeGroupId(currentGroupId, group.getGroup().getName());
            if (groupList != null) {
                if (!groupList.contains(newGroupId)) {
                    continue;
                }
            }
            group.getModeTransition().getModeTransition().traverseTransition(id, newGroupId,
                    visitedId, groupList, visitor);
        }
    }
}

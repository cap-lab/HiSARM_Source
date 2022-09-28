package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.scriptparser.parserdatastructure.entity.Mode;
import com.scriptparser.parserdatastructure.entity.common.Identifier;
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
}

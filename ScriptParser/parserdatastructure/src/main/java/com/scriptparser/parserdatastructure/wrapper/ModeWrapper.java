package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
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
}

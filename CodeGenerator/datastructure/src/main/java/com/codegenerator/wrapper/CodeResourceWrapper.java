package com.codegenerator.wrapper;

import java.util.ArrayList;
import java.util.List;
import com.metadata.algorithm.task.UEMResourceTask;

public class CodeResourceWrapper {
    private UEMResourceTask resource;
    private List<CodeActionWrapper> relatedActionList = new ArrayList<>();

    public UEMResourceTask getResource() {
        return resource;
    }

    public void setResource(UEMResourceTask resource) {
        this.resource = resource;
    }

    public List<CodeActionWrapper> getRelatedActionList() {
        return relatedActionList;
    }

    public void setRelatedActionList(List<CodeActionWrapper> relatedActionList) {
        this.relatedActionList = relatedActionList;
    }

    public CodeActionWrapper getRelatedAction(String actionId) {
        for (CodeActionWrapper action : relatedActionList) {
            if (action.getActionTask().getActionName().equals(actionId)) {
                return action;
            }
        }
        return null;
    }

    public boolean isConflict() {
        return resource.getResource().getResource().isConflict();
    }

    public boolean isExistAction(String actionId) {
        if (getRelatedAction(actionId) != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getResourceId() {
        return resource.getResource().getResource().getResourceId();
    }

}

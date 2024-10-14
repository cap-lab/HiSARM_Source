package com.scriptparser.parserdatastructure.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MissionWrapper {
    private String name;
    private List<TeamWrapper> teamList = new ArrayList<>();
    private List<ServiceWrapper> serviceList = new ArrayList<>();
    private List<ModeWrapper> modeList = new ArrayList<>();
    private List<TransitionWrapper> transitionList = new ArrayList<>();

    public TeamWrapper getTeam(String teamName) throws Exception {
        Optional<TeamWrapper> team = getTeamList().stream()
                .filter(t -> t.getTeam().getName().equals(teamName)).findAny();
        if (team.isPresent()) {
            return team.get();
        } else {
            throw new Exception("No team whose name is " + teamName);
        }
    }

    public ServiceWrapper getService(String serviceName) throws Exception {
        Optional<ServiceWrapper> service = getServiceList().stream()
                .filter(s -> s.getService().getName().equals(serviceName)).findAny();
        if (service.isPresent()) {
            return service.get();
        } else {
            throw new Exception("No service whose name is " + serviceName);
        }
    }

    public ModeWrapper getMode(String modeName) throws Exception {
        Optional<ModeWrapper> mode = getModeList().stream()
                .filter(m -> m.getMode().getName().equals(modeName)).findAny();
        if (mode.isPresent()) {
            return mode.get();
        } else {
            throw new Exception("No mode whose name is " + modeName);
        }
    }

    public TransitionWrapper getTransition(String transitionName) throws Exception {
        Optional<TransitionWrapper> transition = getTransitionList().stream()
                .filter(m -> m.getTransition().getName().equals(transitionName)).findAny();
        if (transition.isPresent()) {
            return transition.get();
        } else {
            throw new Exception("No mode transition whose name is " + transitionName);
        }
    }
}

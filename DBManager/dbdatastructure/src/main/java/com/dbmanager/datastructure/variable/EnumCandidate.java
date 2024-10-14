package com.dbmanager.datastructure.variable;

import java.util.ArrayList;
import java.util.List;

public class EnumCandidate implements Candidate {
    private List<String> candidates = new ArrayList<>();

    public EnumCandidate() {
        candidates = new ArrayList<String>();
    }

    public List<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<String> candidiates) {
        this.candidates = candidiates;
    }

    public void addCandidate(String candidate) {
        candidates.add(candidate);
    }
}

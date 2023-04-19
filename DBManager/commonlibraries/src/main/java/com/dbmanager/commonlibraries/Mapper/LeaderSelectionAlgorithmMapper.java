package com.dbmanager.commonlibraries.Mapper;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.dbmanager.datastructure.leaderselectionalgorithm.LeaderSelectionAlgorithm;

public class LeaderSelectionAlgorithmMapper {
    public static LeaderSelectionAlgorithm mapToLeaderSelectionAlgorithm(Bson bson) {
        LeaderSelectionAlgorithm leaderSelectionAlgorithm = new LeaderSelectionAlgorithm();
        Document document = (Document) bson;

        try {
            leaderSelectionAlgorithm.setLeaderSelectionId(document.getString("LeaderSelectionId"));
            leaderSelectionAlgorithm.setTaskId(document.getString("TaskId"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return leaderSelectionAlgorithm;
    }
}

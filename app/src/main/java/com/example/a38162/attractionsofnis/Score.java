package com.example.a38162.attractionsofnis;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by 38162 on 8/10/2018.
 */

@IgnoreExtraProperties
public class Score {
    String userId;
    String placeId;

    @Exclude
    String scoreId;
    public Score() {}

    public Score(String scoreId, String userId, String placeId) {
        this.userId = userId;
        this.placeId = placeId;
        this.scoreId = scoreId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getScoreId() {
        return scoreId;
    }
}

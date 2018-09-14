package com.example.a38162.attractionsofnis;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Friend {
    String userId;
    String userIdFriend;

    @Exclude
    String friendId;
    public Friend() {}

    public Friend(String friendId, String userId, String userIdFriend) {
        this.friendId = friendId;
        this.userId = userId;
        this.userIdFriend = userIdFriend;
    }

    public String getFriendId() {
        return friendId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserIdFriend() {
        return userIdFriend;
    }
}

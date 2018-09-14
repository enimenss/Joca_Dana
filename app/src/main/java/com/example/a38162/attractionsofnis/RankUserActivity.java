package com.example.a38162.attractionsofnis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RankUserActivity extends AppCompatActivity {
    String loginUserId;
    ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<>();
        setContentView(R.layout.activity_rank_user);
        Intent intent = getIntent();
        loginUserId = intent.getStringExtra("userId");              //ubaci kad se setas kroz Activitije !!!!!!
        Ranking();
    }

    private void Ranking(){
        FirebaseDatabase firebaseDatabase=null;
        DatabaseReference databaseReference, databaseReferenceFriends;
        databaseReference = firebaseDatabase.getInstance().getReference().child("users");
        databaseReferenceFriends = firebaseDatabase.getInstance().getReference().child("friends");
        databaseReferenceFriends.addListenerForSingleValueEvent(valueEventListenerFriends);
        databaseReference.addListenerForSingleValueEvent(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }
            }

            for (int i = 0; i < users.size() - 1; i++) {
                for (int j = i; j < users.size(); j++) {
                    if (Integer.parseInt(users.get(j).getScore()) > Integer.parseInt(users.get(i).getScore())) {
                        User user = users.get(i);
                        users.set(i, users.get(j));
                        users.set(j, user);
                    }
                }
            }

            ListView listViewPlayers = (ListView) findViewById(R.id.list_rankPlayers);
            List<String> array = new ArrayList<>();
            for(User user : users) {
                String string = user.getName() + " " + user.getSurname() + " score: " + user.getScore();
                array.add(string);
            }

            listViewPlayers.setAdapter(new ArrayAdapter<String>(RankUserActivity.this, android.R.layout.simple_list_item_1, array));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };


    ValueEventListener valueEventListenerFriends = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<Friend> friends = new ArrayList<>();
            List<User> usersBest = new ArrayList<>();
            if(dataSnapshot.exists()) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Friend friend = snapshot.getValue(Friend.class);
                    friends.add(friend);
                }
            }

            for (int i=0; i < friends.size()-1; i++) {
                if(friends.get(i).userId.equals(loginUserId)) {
                    boolean find = false;
                    int j = 0;
                    while(!find) {
                        if(users.get(j).getUserId().equals(friends.get(i).getFriendId())) {
                            usersBest.add(users.get(j));
                            find = true;
                        } else j++;
                    }
                }
            }


            ListView listViewFriends = (ListView) findViewById(R.id.list_rankFriends);
            List<String> array = new ArrayList<>();
            for(User user : usersBest) {
                String string = user.getName() + " " + user.getSurname() + " score: " + user.getScore();
                array.add(string);
            }

            listViewFriends.setAdapter(new ArrayAdapter<String>(RankUserActivity.this, android.R.layout.simple_list_item_1, array));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
}

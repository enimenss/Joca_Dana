package com.example.a38162.attractionsofnis;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;


public class UsersData {
    private ArrayList<User> users;
    private HashMap<String, Integer> usersKeyIndexMapping;
    private DatabaseReference database;
    private static final String FIREBASE_CHILD = "users";


  private UsersData() {
      users = new ArrayList<>();
      usersKeyIndexMapping = new HashMap<String, Integer>();
      database = FirebaseDatabase.getInstance().getReference();
      database.child(FIREBASE_CHILD).addChildEventListener(chieldEventListener);
      database.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);
  }

  ValueEventListener parentEventListener = new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
            //users = (ArrayList<User>) dataSnapshot.getValue();
          users.add(dataSnapshot.getValue(User.class));
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
  };

  ChildEventListener chieldEventListener = new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String userId = dataSnapshot.getKey();

            if(!usersKeyIndexMapping.containsKey(userId)) {
                User user = dataSnapshot.getValue(User.class);
                user.userId = userId;
                users.add(user);
                usersKeyIndexMapping.put(userId, users.size()-1);
            }
      }

      @Override
      public void onChildChanged(DataSnapshot dataSnapshot, String s) {
          String userId = dataSnapshot.getKey();
          User user = dataSnapshot.getValue(User.class);
          user.userId = userId;

          if(usersKeyIndexMapping.containsKey(userId)) {
              int index = usersKeyIndexMapping.get(userId);
              users.set(index, user);
          }
          else {
              users.add(user);
              usersKeyIndexMapping.put(userId, users.size()-1);
          }
      }

      @Override
      public void onChildRemoved(DataSnapshot dataSnapshot) {
          String userId = dataSnapshot.getKey();

          if(usersKeyIndexMapping.containsKey(userId)) {
              int index = usersKeyIndexMapping.get(userId);
              users.remove(index);
              //recreateKeyIndexMapping();
          }
      }

      @Override
      public void onChildMoved(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
  };

  public void AddNewUser(User user) {
      String id = database.push().getKey();
      users.add(user);
      usersKeyIndexMapping.put(id, users.size()-1);
      database.child(FIREBASE_CHILD).child(id).setValue(user);
      user.userId = id;
  }

  private void recreateKeyIndexMapping() {
      usersKeyIndexMapping.clear();
      for (int i=0;i<users.size();i++) {
          usersKeyIndexMapping.put(users.get(i).userId, i);
      }
  };

  ListUpdateEventListener updateEventListener;
  public void setEventListener(ListUpdateEventListener listener) {
      updateEventListener = listener;
  };


  public interface ListUpdateEventListener {
      void onListUpdated();
  };
}

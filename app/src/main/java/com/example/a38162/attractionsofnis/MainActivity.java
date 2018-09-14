package com.example.a38162.attractionsofnis;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        places = new ArrayList<>();
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("Name");
        String surnameUser = intent.getStringExtra("Surname");
        final String idUser = intent.getStringExtra("UserId");

        ((TextView) findViewById(R.id.textView)).setText("Welcome " + nameUser + " " + surnameUser + ", what do you want to explore? You can pick more than one:");

        Button ranking = (Button) findViewById(R.id.button_Ranking);
        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, RankUserActivity.class);
                intent1.putExtra("userId", idUser);
                startActivity(intent1);
            }
        });

        Button button = (Button) findViewById(R.id.button_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean landmarks = ((CheckBox) findViewById(R.id.checkBox_landmarks)).isChecked();
                boolean museums = ((CheckBox) findViewById(R.id.checkBox_museums)).isChecked();
                boolean restaurants = ((CheckBox) findViewById(R.id.checkBox_night_life)).isChecked();
                boolean other = ((CheckBox) findViewById(R.id.checkBox_interesting)).isChecked();

                if(! (landmarks || museums || restaurants || other)) {
                    Toast.makeText(MainActivity.this, "You have to check something!", Toast.LENGTH_LONG).show();
                    return;
                }

                FirebaseDatabase firebaseDatabase = null;
                DatabaseReference databaseReference, databaseReferencePlaces;
                databaseReference = firebaseDatabase.getInstance().getReference().child("scores");
                databaseReferencePlaces = firebaseDatabase.getInstance().getReference().child("places");
                databaseReferencePlaces.addListenerForSingleValueEvent(valueEventListener);

                for (Place place : places) {
                    if(landmarks) {
                        if (place.category.equals("landmark")) {
                            String id = databaseReference.push().getKey();
                            Score score = new Score(id, idUser, place.placeId);
                            databaseReference.child(id).setValue(score);
                        }
                    }

                    if(museums) {
                        if (place.category.equals("museum")) {
                            String id = databaseReference.push().getKey();
                            Score score = new Score(id, idUser, place.placeId);
                            databaseReference.child(id).setValue(score);
                        }
                    }

                    if(restaurants) {
                        if (place.category.equals("restaurant")) {
                            String id = databaseReference.push().getKey();
                            Score score = new Score(id, idUser, place.placeId);
                            databaseReference.child(id).setValue(score);
                        }
                    }

                    if(other) {
                        if (place.category.equals("other")) {
                            String id = databaseReference.push().getKey();
                            Score score = new Score(id, idUser, place.placeId);
                            databaseReference.child(id).setValue(score);
                        }
                    }
                }

                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            places.clear();
            if(dataSnapshot.exists()) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    places.add(place);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
}

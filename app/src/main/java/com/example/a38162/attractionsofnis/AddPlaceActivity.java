package com.example.a38162.attractionsofnis;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class AddPlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button button_map = (Button) findViewById(R.id.button_getLocationPlace);
        button_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddPlaceActivity.this, AddPlaceMapActivity.class);
                startActivityForResult(i, 1);
            }
        });

        Button button_add = (Button) findViewById(R.id.button_addPlace);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.edit_namePlace)).getText().toString();
                String description = ((EditText) findViewById(R.id.edit_descriptionPlace)).getText().toString();
                String category = ((EditText) findViewById(R.id.edit_categoryPlace)).getText().toString();
                String lon = ((EditText) findViewById(R.id.edit_longitudePlace)).getText().toString();
                String lat = ((EditText) findViewById(R.id.edit_latitudePlace)).getText().toString();
                Random r = new Random();
                int i1 = r.nextInt(100 - 5) + 10;
                String points = Integer.toString(i1);

                FirebaseDatabase firebaseDatabase = null;
                DatabaseReference databaseReference;
                databaseReference = firebaseDatabase.getInstance().getReference("places");
                String id = databaseReference.push().getKey();
                Place place = new Place(name, description,category,lat,lon,points,id);
                databaseReference.child(id).setValue(place);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String lon = data.getExtras().getString("lon");
            ((EditText) findViewById(R.id.edit_longitudePlace)).setText(lon);

            String lat = data.getExtras().getString("lat");
            ((EditText) findViewById(R.id.edit_latitudePlace)).setText(lat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.example.a38162.attractionsofnis;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import java.io.File;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegistrationActivity extends AppCompatActivity {
    DatabaseReference databaseReferences;
    FirebaseDatabase firebaseDatabase;
    Integer REQUEST_CAMERA=0, SELECT_FILE=1;
    File imageFile;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUser();
            }
        });

        databaseReferences = firebaseDatabase.getInstance().getReference().child("users");

        Button button = (Button) findViewById(R.id.button_picture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });


        storageReference = firebaseStorage.getInstance().getReference();
    }

    private void SelectImage() {
        final CharSequence[] items= {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(items[i].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                else if(items[i].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_FILE);
                }
                else if(items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ImageView image = (ImageView) findViewById(R.id.image_user);
            image.setImageBitmap(bitmap);

            if(requestCode == REQUEST_CAMERA) {
                Uri uri = data.getData();           //zastooooo neceeeeee ???????????
                StorageReference filePath = storageReference.child("Photos").child(uri.getLastPathSegment());
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(RegistrationActivity.this, "Upload done!", Toast.LENGTH_SHORT).show();
                    }
                });
                }
            else if(requestCode == SELECT_FILE) {
                Uri uri = data.getData();
                StorageReference filePath = storageReference.child("Photos").child(uri.getLastPathSegment());
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(RegistrationActivity.this, "Upload done!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void AddUser() {
        EditText name = findViewById(R.id.edit_name);
        EditText surname = findViewById(R.id.edit_surname);
        EditText phone = findViewById(R.id.edit_phone);
        EditText username = findViewById(R.id.edit_username);
        EditText password = findViewById(R.id.edit_password);

        String Name = name.getText().toString();
        String Surname = surname.getText().toString();
        String Phone = phone.getText().toString();
        String Username = username.getText().toString();
        String Password = password.getText().toString();

        if(TextUtils.isEmpty(Name)) {
            Toast.makeText(this, "You have to enter a name", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(Surname)) {
            Toast.makeText(this, "You have to enter a surname", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(Phone)) {
            Toast.makeText(this, "You have to enter a phone number", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(Username)) {
            Toast.makeText(this, "You have to enter a username", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(Password)) {
            Toast.makeText(this, "You have to enter a password", Toast.LENGTH_LONG).show();
            return;
        }

        String id = databaseReferences.push().getKey();
        User user = new User(id, Name, Surname, Username, Password, Phone, "Slika", "0");

        databaseReferences.child(id).setValue(user);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Name", user.getName());
        intent.putExtra("Surname", user.getSurname());
        intent.putExtra("UserId", user.getUserId());
        //intent.putExtra("Picture", user.getPicture());               //otkomentarisati kada zapamtimo i sliku
        startActivity(intent);
    }

}

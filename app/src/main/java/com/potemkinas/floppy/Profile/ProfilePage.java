package com.potemkinas.floppy.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.potemkinas.floppy.AddedPhoto;
import com.potemkinas.floppy.AddedVideo;
import com.potemkinas.floppy.MainActivity;
import com.potemkinas.floppy.PhotoCameraPage;
import com.potemkinas.floppy.R;
import com.potemkinas.floppy.settings;


public class ProfilePage extends AppCompatActivity {
    private TextView name,photoNum;

    ConstraintLayout root;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    private String Uname;

    ImageView avatar;

    private int PhotoCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        name = (TextView) findViewById(R.id.username);
        photoNum=(TextView) findViewById(R.id.NumberOfAddedPhotos);
        PhotoCount=(PhotoCameraPage.counter);

        avatar= findViewById(R.id.ImageView);


        photoNum.setText("Фото добавлено: "+PhotoCount);



    }



    public void onClickHome(View view) {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onClickSettings(View view) {
        Intent intent=new Intent(this, settings.class);
        startActivity(intent);
    }

    public void onClickAddedPhoto(View view) {
        Intent intent=new Intent(this, AddedPhoto.class);
        startActivity(intent);
    }
    public void onClickAddedVideo(View view) {
        Intent intent=new Intent(this, AddedVideo.class);
        startActivity(intent);
    }
}
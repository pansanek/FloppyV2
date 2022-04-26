package com.potemkinas.floppy.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.potemkinas.floppy.AddedPhoto;
import com.potemkinas.floppy.AddedVideo;
import com.potemkinas.floppy.MainActivity;
import com.potemkinas.floppy.PhotoCameraPage;
import com.potemkinas.floppy.R;
import com.potemkinas.floppy.Upload;
import com.potemkinas.floppy.models.User;
import com.potemkinas.floppy.settings;

import java.util.List;


public class ProfilePage extends AppCompatActivity {
    private TextView name,email,phone,photoNum;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    FirebaseAuth auth;
    private String Uname;

    ImageView avatar;

    private int PhotoCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        name = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.Email);
        phone = (TextView) findViewById(R.id.phone);
        photoNum=(TextView) findViewById(R.id.NumberOfAddedPhotos);
        PhotoCount=(PhotoCameraPage.counter);
        photoNum.setText("Фото добавлено: "+PhotoCount);
        avatar= findViewById(R.id.ImageView);
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");

        mDatabaseRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User myname = snapshot.getValue(User.class);
                name.setText("Имя: " + myname.getName());
                email.setText("Эл.Почта: " + myname.getEmail());
                phone.setText("Телефон: " + myname.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfilePage.this, "Данные пользователя не получены.", Toast.LENGTH_SHORT).show();
            }
        });



    }



    public void onClickHome(View view) {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void onClickSettings(View view) {
        Intent intent=new Intent(this, settings.class);
        startActivity(intent);
        finish();
    }

    public void onClickAddedPhoto(View view) {
        Intent intent=new Intent(this, AddedPhoto.class);
        startActivity(intent);
        finish();
    }
    public void onClickAddedVideo(View view) {
        Intent intent=new Intent(this, AddedVideo.class);
        startActivity(intent);
        finish();
    }
}
package com.potemkinas.floppy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.potemkinas.floppy.Profile.ProfilePage;
import com.potemkinas.floppy.models.ProfilePics;
import com.potemkinas.floppy.models.User;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

public class settings extends AppCompatActivity {
    private FirebaseStorage mPPStorage;
    ConstraintLayout root;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    private DatabaseReference mPPDatabaseRef;
    ImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        avatar= findViewById(R.id.profile_image);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        mPPDatabaseRef = FirebaseDatabase.getInstance().getReference("ProfilePics");
        root = findViewById(R.id.root_elem);
        mPPDatabaseRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfilePics userPP = snapshot.getValue(ProfilePics.class);
                loadpicturebyurl(userPP.getFileUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(settings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
    }
    private void loadpicturebyurl(String url){
        Picasso.with(this)
                .load(url)
                .into(avatar, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }
    public void onClickProfile(View view) {
        Intent intent=new Intent(this, ProfilePage.class);
        startActivity(intent);
        finish();
    }
    public void onClickHome(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void onClickDevInfo(View view) {
        TextView textView = (TextView) findViewById(R.id.DevInfoText);
        textView.setText("Данная программа является курсовой работой студента РТУ МИРЭА Потемкина Александра.");
        textView.setAlpha(1);
    }
    public void onClickAppInfo(View view) {
        TextView textView = (TextView) findViewById(R.id.DevInfoText);
        textView.setText("Floppy - это домашняя медиатека для создания, загрузки и хранения ваших фотографий и видео.");
        textView.setAlpha(1);
    }
    public void onClickRules(View view) {
        showWindow();
    }

    private void showWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Правила использования приложения 'Floppy'");

        LayoutInflater inflater = LayoutInflater.from(this);
        View signin_window=inflater.inflate(R.layout.rules_window,null);
        dialog.setView(signin_window);

        dialog.setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();

            }
        });
        dialog.show();
    }

    public void onClickChangeProfile(View view) {
        showInfoChangeWindow();
    }

    private void showInfoChangeWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Изменение профиля");
        dialog.setMessage("Введите все данные для изменения профиля");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window=inflater.inflate(R.layout.changeprofile_window,null);
        dialog.setView(register_window);

        MaterialEditText name = register_window.findViewById(R.id.nameField);
        MaterialEditText phone = register_window.findViewById(R.id.phoneField);


        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();

            }
        });
        dialog.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(name.getText().toString())){
                    Snackbar.make(root,"Введите ваше имя",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phone.getText().toString())){
                    Snackbar.make(root,"Введите ваш телефон",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //Изменение пользователя
                User user = new User();
                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User myname = snapshot.getValue(User.class);
                        user.setEmail(myname.getEmail());
                        user.setPass(myname.getPass());
                        user.setName(name.getText().toString());
                        user.setPhone(phone.getText().toString());
                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnSuccessListener(unused -> Snackbar.make(root,"Пользователь изменен!",Snackbar.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        dialog.show();
    }
}
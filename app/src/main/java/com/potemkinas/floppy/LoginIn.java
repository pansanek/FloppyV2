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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.potemkinas.floppy.Profile.ProfilePage;
import com.potemkinas.floppy.models.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class LoginIn extends AppCompatActivity {
    Button btnSignIn, btnRegister;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    ConstraintLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        root = findViewById(R.id.root_el);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");


        btnRegister.setOnClickListener(v -> showRegisterWindow());
        btnSignIn.setOnClickListener(v -> showSignInWindow());
    }

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите все данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View signin_window=inflater.inflate(R.layout.singin_window,null);
        dialog.setView(signin_window);

        MaterialEditText pass = signin_window.findViewById(R.id.passField);
        MaterialEditText email = signin_window.findViewById(R.id.emailField);


        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();

            }
        });
        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root,"Введите вашу почту",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(pass.getText().toString().length()<8){
                    Snackbar.make(root,"Введите ваш пароль",Snackbar.LENGTH_SHORT).show();
                    return;
                }
            auth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        startActivity(new Intent(LoginIn.this, MainActivity.class));
                        finish();
                    }).addOnFailureListener(e -> Snackbar.make(root,"Ошибка авторизации: " +e.getMessage(), Snackbar.LENGTH_SHORT).show());
            }
        });
        dialog.show();
    }

    private void showRegisterWindow() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Зарегестрироваться");
            dialog.setMessage("Введите все данные для регистрации");

            LayoutInflater inflater = LayoutInflater.from(this);
            View register_window=inflater.inflate(R.layout.reg_window,null);
            dialog.setView(register_window);

            MaterialEditText name = register_window.findViewById(R.id.nameField);
            MaterialEditText pass = register_window.findViewById(R.id.passField);
            MaterialEditText email = register_window.findViewById(R.id.emailField);
            MaterialEditText phone = register_window.findViewById(R.id.phoneField);


            dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();

                }
            });
            dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    if(TextUtils.isEmpty(email.getText().toString())){
                        Snackbar.make(root,"Введите вашу почту",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(name.getText().toString())){
                        Snackbar.make(root,"Введите ваше имя",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(phone.getText().toString())){
                        Snackbar.make(root,"Введите ваш телефон",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    if(pass.getText().toString().length()<8){
                        Snackbar.make(root,"Введите ваш пароль",Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    //Регистрация пользователя

                    auth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                            .addOnCompleteListener(authResult -> {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPass(pass.getText().toString());
                                user.setPhone(phone.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnSuccessListener(unused -> Snackbar.make(root,"Пользователь добавлен!",Snackbar.LENGTH_SHORT).show());
                            });

                }
            });

            dialog.show();
        }

}


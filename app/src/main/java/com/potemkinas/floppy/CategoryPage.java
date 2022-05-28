package com.potemkinas.floppy;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.potemkinas.floppy.Profile.ProfilePage;
import com.rengwuxian.materialedittext.MaterialEditText;

public class CategoryPage extends AppCompatActivity {
    Button btnUploadFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

        ImageView categoryImage = findViewById(R.id.categoryPageImage);
        TextView categoryTitle = findViewById(R.id.categoryPageTitle);

        btnUploadFile = findViewById(R.id.AddFromFilesPage);
        categoryImage.setImageResource(getIntent().getIntExtra("categoryImage",0));
        categoryTitle.setText(getIntent().getStringExtra("categoryTitle"));
        if(getIntent().getStringExtra("categoryTitle").equals("Фото")) {
            btnUploadFile.setOnClickListener(v -> showFileAddWindow());
        }
        else{
            btnUploadFile.setOnClickListener(v -> showVideoAddWindow());
        }
    }

    private void showVideoAddWindow() {
        Intent intent=new Intent(this, add_video_from_device.class);
        startActivity(intent);
        finish();}


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


    public void onClickSettings(View view) {
        Intent intent=new Intent(this, settings.class);
        startActivity(intent);
        finish();
    }

    private void showFileAddWindow() {
        Intent intent=new Intent(this, add_from_device.class);
        startActivity(intent);
        finish();}

}




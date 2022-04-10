package com.potemkinas.floppy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.potemkinas.floppy.Profile.ProfilePage;

public class CategoryPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

        ImageView categoryImage = findViewById(R.id.categoryPageImage);
        TextView categoryTitle = findViewById(R.id.categoryPageTitle);


        categoryImage.setImageResource(getIntent().getIntExtra("categoryImage",0));
        categoryTitle.setText(getIntent().getStringExtra("categoryTitle"));
    }
    public void onClickProfile(View view) {
        Intent intent=new Intent(this, ProfilePage.class);
        startActivity(intent);
    }
    public void onClickHome(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void onClickAddFromCamera(View view){
        Intent intent = new Intent(this,PhotoCameraPage.class);
        startActivity(intent);
    }
    public void onClickSettings(View view) {
        Intent intent=new Intent(this, settings.class);
        startActivity(intent);
    }
}



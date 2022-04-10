package com.potemkinas.floppy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.potemkinas.floppy.Profile.ProfilePage;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    public void onClickProfile(View view) {
        Intent intent=new Intent(this, ProfilePage.class);
        startActivity(intent);
    }
    public void onClickHome(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
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

}
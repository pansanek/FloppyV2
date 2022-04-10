package com.potemkinas.floppy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.potemkinas.floppy.Profile.ProfilePage;

import java.util.Random;

public class PhotoCameraPage extends AppCompatActivity {
    public static ImageView imageView;
    public static int counter=0;
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 5;
    private static final String NOTIFICATION_CHANNEL_ID = "1";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_camera_page);

        Button CameraButton= (Button) findViewById(R.id.Camerabutton);
        imageView = (ImageView) findViewById(R.id.ImageView);
        Button button = findViewById(R.id.photoadd);
        createNotificationChannel();
        button.setOnClickListener(v->onClickAdd());
        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        ActivityCompat.requestPermissions(PhotoCameraPage.this, new String[]
                        {Manifest.permission.CAMERA},
                REQUEST_CODE_PERMISSION_CAMERA);

        CameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap=(Bitmap)data.getExtras().get("data");
        Button button = findViewById(R.id.photoadd);
        imageView.setImageBitmap(bitmap);
        counter++;
        button.setAlpha(1);
        }

    public void onClickProfile(View view) {
        Intent intent=new Intent(this, ProfilePage.class);
        startActivity(intent);
    }
    public void onClickHome(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void onClickSettings(View view) {
        Intent intent=new Intent(this, settings.class);
        startActivity(intent);
    }
    public void onClickAdd(){
        showNotification();
        Intent intent=new Intent(this,ProfilePage.class);
        intent.putExtra("counter",counter);
        startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence name = "Notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    private void showNotification() {
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_caracal_wildcat_head_silhouette_by_vetherie)
                .setContentTitle("Добавление фото")
                .setContentText("Фото было успешно добавлено")
                .setStyle(new NotificationCompat.BigTextStyle());
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        Random random = new Random();
        notificationManager.notify(random.nextInt(), builder.build());
    }


}
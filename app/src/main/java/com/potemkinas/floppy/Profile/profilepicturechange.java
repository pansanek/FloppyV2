package com.potemkinas.floppy.Profile;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import com.potemkinas.floppy.AddedPhoto;
import com.potemkinas.floppy.MainActivity;
import com.potemkinas.floppy.R;
import com.potemkinas.floppy.models.ProfilePics;
import com.potemkinas.floppy.settings;
import com.squareup.picasso.Picasso;

public class profilepicturechange extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST =1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private Button mTextViewShowUploads;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;
    private int position =0;
    private String Username;
    private StorageReference mStorageRef,mStorageUserRef;
    private DatabaseReference mDatabaseRef,mDatabaseUserRef;
    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepicturechange);
    mButtonChooseImage = findViewById(R.id.choosefilebuttonpp);
    mButtonUpload = findViewById(R.id.uploadfilebuttonpp);
    mTextViewShowUploads = findViewById(R.id.showfilebuttonpp);
    mImageView = findViewById(R.id.chosenFilepp);
    mProgressBar = findViewById(R.id.progress_barpp);
        mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
    mStorageRef = FirebaseStorage.getInstance().getReference("ProfilePics");
    mDatabaseRef = FirebaseDatabase.getInstance().getReference("ProfilePics");
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openFileChooser();
        }
    });
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(profilepicturechange.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
            }
        }
    });

        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openImagesActivity();
        }
    });
}

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            Toast.makeText(profilepicturechange.this, "Upload successful", Toast.LENGTH_LONG).show();
                            ProfilePics profilePics = new ProfilePics(downloadUrl.toString(),FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                            String uploadId = mDatabaseRef.push().getKey();

                            mDatabaseRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).setValue(profilePics);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(profilepicturechange.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImagesActivity() {
        Intent intent = new Intent(this, ProfilePage.class);
        startActivity(intent);
    }

    public void onClickProfile(View view) {
        Intent intent=new Intent(this, ProfilePage.class);
        startActivity(intent);
        finish();
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
}

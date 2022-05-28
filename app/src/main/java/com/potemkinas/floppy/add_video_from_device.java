package com.potemkinas.floppy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import com.potemkinas.floppy.Profile.ProfilePage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class add_video_from_device extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST =1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private Button mTextViewShowUploads;
    private EditText mEditTextFileName;
    private VideoView mVideoView;
    private ProgressBar mProgressBar;
    private String PhoneModel;
    private Uri mVideoUri;
    private int position =0;
    private String Username;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_from_device);
        mButtonChooseImage = findViewById(R.id.choosefilebutton);
        mButtonUpload = findViewById(R.id.uploadfilebutton);
        mTextViewShowUploads = findViewById(R.id.showfilebutton);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mVideoView = findViewById(R.id.chosenFile);
        PhoneModel = android.os.Build.MODEL;
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        mStorageRef = FirebaseStorage.getInstance().getReference("VdUploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("VdUploads");
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
                    Toast.makeText(add_video_from_device.this, "Upload in progress", Toast.LENGTH_SHORT).show();
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
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(add_video_from_device.this, "Убедитесь, что вы ввели уникальное название.", Toast.LENGTH_LONG).show();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mVideoUri = data.getData();

            mVideoView.setVideoURI(mVideoUri);
            MediaController mediaController = new MediaController(this);
            mVideoView.setMediaController(mediaController);
            mediaController.setAnchorView(mVideoView);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile() {
        if (mVideoUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mVideoUri));

            mUploadTask = fileReference.putFile(mVideoUri)
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
                            Toast.makeText(add_video_from_device.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),downloadUrl.toString(),FirebaseAuth.getInstance().getCurrentUser().getUid().toString(),PhoneModel);
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()+mEditTextFileName.getText().toString().trim()).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(add_video_from_device.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, AddedVideo.class);
        startActivity(intent);
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
    public void onClickSettings(View view) {
        Intent intent=new Intent(this, settings.class);
        startActivity(intent);
        finish();
    }
}
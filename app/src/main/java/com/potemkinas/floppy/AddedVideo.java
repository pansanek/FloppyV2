package com.potemkinas.floppy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.potemkinas.floppy.Profile.ProfilePage;
import com.potemkinas.floppy.ImageAdapter;
import com.potemkinas.floppy.models.Admin;
import com.potemkinas.floppy.models.ProfilePics;
import com.potemkinas.floppy.models.User;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

public class AddedVideo extends AppCompatActivity implements VideoAdapter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    VideoAdapter mAdapter;
    FirebaseAuth auth;
    private ProgressBar mProgressCircle;
    private Uri mVideoUri;
    private VideoView mVideoView;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mAdminRef;
    public boolean isItAdmin;
    private String AUID;
    private List<Upload> mUploads;
    private FirebaseStorage mPPStorage;
    private DatabaseReference mPPDatabaseRef;
    private String mName;
    private String mVideoUrl;
    private String mUID;
    private String PhoneModel;
    private String mUserId;

    ProgressDialog progressDialog;
    private String Id;
    ImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isItAdmin=false;
        setContentView(R.layout.activity_added_video);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        avatar= findViewById(R.id.profile_image);
        mProgressCircle = findViewById(R.id.progress_circle);
        mProgressCircle.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.darkest), PorterDuff.Mode.SRC_IN );
        mUploads = new ArrayList<>();

        mAdapter = new VideoAdapter(AddedVideo.this, mUploads);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(AddedVideo.this);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        Id = user.getUid().toString();

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("VdUploads");
        mAdminRef = FirebaseDatabase.getInstance().getReference("Admin");
        mAdminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot :snapshot.getChildren()) {
                    Admin admin = postSnapshot.getValue(Admin.class);
                    AUID = admin.getUID();
                    if(AUID.equals(Id)){
                        isItAdmin = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mName = upload.getName();
                    mVideoUrl = upload.getFileUrl();
                    mUID = upload.getmUID();
                    PhoneModel = upload.getDeviceName();
                    if(mUID.equals(Id)) {
                        mUploads.add(new Upload(mName, mVideoUrl, mUID,PhoneModel));
                    }
                    if(isItAdmin){
                        PhoneModel = "UID ????????????????????????: "+upload.getmUID();
                        mUploads.add(new Upload(mName, mVideoUrl, mUID,PhoneModel));
                    }


                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddedVideo.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

        mPPDatabaseRef = FirebaseDatabase.getInstance().getReference("ProfilePics");
        mPPDatabaseRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfilePics userPP = snapshot.getValue(ProfilePics.class);
                loadpicturebyurl(userPP.getFileUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddedVideo.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
    public void onClickSettings(View view) {
        Intent intent=new Intent(this, settings.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "?????????? ?????????????????????? ?????????? ?????????????? ?????????????????? ????????????????????", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onWhatEverClick(int position) {
        Upload selectedItem = mUploads.get(position);
        String vidUrl = mUploads.get(position).getFileUrl();
        showVideo(vidUrl);
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = mUploads.get(position);
        String picname = mUploads.get(position).getName();
        String username = mUploads.get(position).getmUID();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getFileUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(username+picname).removeValue();
                mUploads.remove(position);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(AddedVideo.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showVideo(String vidUrl) {
        mVideoView = findViewById(R.id.vidview);
        mVideoUri = Uri.parse(vidUrl);
        mVideoView.setVideoURI(mVideoUri);
        MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        mediaController.setAnchorView(mVideoView);


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
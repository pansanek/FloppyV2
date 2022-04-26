package com.potemkinas.floppy;

import com.google.firebase.database.Exclude;
import com.potemkinas.floppy.models.User;

public class Upload {
    private String mName;
    private String mFileUrl;
    private String mKey;
    private String mUID;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String FileUrl,String UID) {

        mName = name;
        mFileUrl = FileUrl;
        mUID = UID;

    }

    public String getmUID() {
        return mUID;
    }

    public void setmUID(String mUID) {
        this.mUID = mUID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getFileUrl() {
        return mFileUrl;
    }

    public void setFileUrl(String imageUrl) {
        mFileUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
package com.potemkinas.floppy.models;

public class ProfilePics {
    private String mFileUrl;
    private String mUID;
    public ProfilePics(){}
    public ProfilePics(String FileUrl,String UID) {

        mFileUrl = FileUrl;
        mUID = UID;

    }

    public String getmUID() {
        return mUID;
    }

    public void setmUID(String mUID) {
        this.mUID = mUID;
    }

    public String getFileUrl() {
        return mFileUrl;
    }

    public void setFileUrl(String imageUrl) {
        mFileUrl = imageUrl;
    }
}

package com.potemkinas.floppy;

import android.bluetooth.BluetoothClass;

import com.google.firebase.database.Exclude;
import com.potemkinas.floppy.models.User;

public class Upload {
    private String mName;
    private String mFileUrl;
    private String mUID;
    private String DeviceName;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String FileUrl,String UID,String DeviceNam) {

        mName = name;
        mFileUrl = FileUrl;
        mUID = UID;
        DeviceName = DeviceNam;

    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
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

}
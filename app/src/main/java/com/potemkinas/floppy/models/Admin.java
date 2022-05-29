package com.potemkinas.floppy.models;

public class Admin {
    private String UID;
    public Admin(){}

    public Admin(String uid) {
        UID = uid;

    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }


}

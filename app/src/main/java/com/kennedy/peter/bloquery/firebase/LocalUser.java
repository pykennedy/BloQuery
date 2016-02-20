package com.kennedy.peter.bloquery.firebase;

public class LocalUser {
    public String UID;
    public String userName;
    public String email;
    public void setUserDetails(String UID, String userName, String email) {
        this.UID = UID;
        this.userName = userName;
        this.email = email;
    }

    public void updateUsername(String userName) {
        // TODO change username in shared preferences
        this.userName = userName;
    }
    public void nullifyUser() {
        this.UID = null;
        this.userName = null;
        this.email = null;
    }
}
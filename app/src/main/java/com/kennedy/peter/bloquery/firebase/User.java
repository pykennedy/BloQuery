package com.kennedy.peter.bloquery.firebase;

public class User {
    public String UID, userName, email;
    public void updateUserDetails(String UID, String userName, String email) {
        this.UID = (UID == null) ? this.UID : UID;
        this.userName = (userName == null) ? this.userName : userName;
        this.email = (email == null) ? this.email : email;
    }
    public void nullifyUser() {
        this.UID = null;
        this.userName = null;
        this.email = null;
    }
}

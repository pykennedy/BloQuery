package com.kennedy.peter.bloquery.firebase;

import com.firebase.client.Firebase;

public class FirebaseManager {
    Firebase firebase = new Firebase("https://flickering-torch-9808.firebaseio.com/");

    public void logIn(String username, String password) {
        firebase.authWithPassword();
    }

    public void createAccount(String email, String password, Firebase.ResultHandler handler) {
        firebase.createUser(email, password, handler);
    }
}

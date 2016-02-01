package com.kennedy.peter.bloquery;

import android.app.Application;

import com.firebase.client.Firebase;

public class BloQueryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}

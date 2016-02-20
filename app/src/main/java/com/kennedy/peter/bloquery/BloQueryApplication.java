package com.kennedy.peter.bloquery;

import android.app.Application;

import com.firebase.client.Firebase;
import com.kennedy.peter.bloquery.api.DataSource;
import com.kennedy.peter.bloquery.firebase.LocalUser;

public class BloQueryApplication extends Application {

    private static BloQueryApplication sharedInstance;
    private DataSource dataSource;
    private LocalUser localUser;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        dataSource = new DataSource();
        sharedInstance = this;
        localUser = new LocalUser();
    }

    public static BloQueryApplication getSharedInstance() {return sharedInstance;}
    public DataSource getDataSource() {return dataSource;}
    public static LocalUser getSharedUser() {return BloQueryApplication.getSharedInstance().getLocalUser();}
    public LocalUser getLocalUser() {return localUser;}
}

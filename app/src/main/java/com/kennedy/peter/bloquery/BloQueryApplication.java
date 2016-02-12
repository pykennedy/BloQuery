package com.kennedy.peter.bloquery;

import android.app.Application;

import com.firebase.client.Firebase;
import com.kennedy.peter.bloquery.api.DataSource;
import com.kennedy.peter.bloquery.firebase.User;

public class BloQueryApplication extends Application {

    private static BloQueryApplication sharedInstance;
    private DataSource dataSource;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        dataSource = new DataSource();
        sharedInstance = this;
        user = new User();
    }

    public static BloQueryApplication getSharedInstance() {return sharedInstance;}
    public DataSource getDataSource() {return dataSource;}
    public static User getSharedUser() {return BloQueryApplication.getSharedInstance().getUser();}
    public User getUser() {return user;}
}

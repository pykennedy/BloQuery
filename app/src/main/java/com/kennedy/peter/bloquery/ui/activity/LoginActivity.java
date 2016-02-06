package com.kennedy.peter.bloquery.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;
import com.kennedy.peter.bloquery.ui.animations.BloQueryAnimator;

public class LoginActivity extends Activity {
    private RelativeLayout loginWindow, createAccountWindow;
    private boolean createAccountWindowIsOpen = false;
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseManager = new FirebaseManager();

        final EditText emailET = (EditText)findViewById(R.id.create_email);
        //EditText usernameET = (EditText)findViewById(R.id.login_username);
        final EditText passwordET = (EditText)findViewById(R.id.create_password);
        Button loginButton = (Button)findViewById(R.id.login_loginButton);
        Button createAccountButton = (Button)findViewById(R.id.create_createButton);
        TextView createAccount = (TextView)findViewById(R.id.login_createAccountText);
        loginWindow = (RelativeLayout)findViewById(R.id.login_window);
        createAccountWindow = (RelativeLayout)findViewById(R.id.create_account_window);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.AuthResultHandler handler = new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Toast.makeText(LoginActivity.this, "UID: " + authData.getUid(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(LoginActivity.this, "Error: " + firebaseError, Toast.LENGTH_SHORT).show();
                    }
                };
                firebaseManager.logIn("fakestuff420@gmail.com", "q1w2e3r4", handler);
            }
        });
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                Firebase.ResultHandler handler = new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(LoginActivity.this, "User created!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(LoginActivity.this, "Error: " + firebaseError, Toast.LENGTH_SHORT).show();
                    }
                };
                // fakestuff420@gmail.com    q1w2e3r4
                firebaseManager.createAccount(email, password, handler);
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BloQueryAnimator.fadeOutThenInViews(loginWindow, createAccountWindow);
                createAccountWindowIsOpen = true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(createAccountWindowIsOpen) {
            BloQueryAnimator.fadeOutThenInViews(createAccountWindow, loginWindow);
            createAccountWindowIsOpen = false;
        }
    }
}

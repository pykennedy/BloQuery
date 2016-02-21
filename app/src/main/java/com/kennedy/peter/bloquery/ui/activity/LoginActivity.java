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
import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;
import com.kennedy.peter.bloquery.ui.animations.BloQueryAnimator;

public class LoginActivity extends Activity {
    //TODO   store the users username with SharedPreference when they create their account in case
    //TODO   they close the app before they login and succesfuly store their user info in the firebase

    private RelativeLayout loginWindow, createAccountWindow;
    private boolean createAccountWindowIsOpen = false;
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseManager = new FirebaseManager();

        final EditText emailET = (EditText)findViewById(R.id.create_email);
        final EditText usernameET = (EditText)findViewById(R.id.create_username);
        final EditText passwordET = (EditText)findViewById(R.id.create_password);
        Button loginButton = (Button)findViewById(R.id.login_loginButton);
        Button createAccountButton = (Button)findViewById(R.id.create_createButton);
        TextView createAccount = (TextView)findViewById(R.id.login_createAccountText);
        loginWindow = (RelativeLayout)findViewById(R.id.login_window);
        createAccountWindow = (RelativeLayout)findViewById(R.id.create_account_window);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptingLogin();
                Firebase.AuthResultHandler handler = new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        System.out.print("AUTHDATA "+ authData.getAuth());
                        BloQueryApplication.getSharedUser().setUserDetails(authData.getUid(),
                                "jimmy", emailET.getText().toString());
                        Firebase.CompletionListener listener = new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if(firebaseError != null) {
                                    Toast.makeText(LoginActivity.this, "Error: " + firebaseError, Toast.LENGTH_SHORT).show();
                                    resetLogin();
                                    return;
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            }
                        };
                        firebaseManager.addUser(listener);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(LoginActivity.this, "Error: " + firebaseError, Toast.LENGTH_SHORT).show();
                        resetLogin();
                    }
                };
                firebaseManager.logIn("fakestuff422@gmail.com", "q1w2e3r4", handler);
                firebaseManager.userScanner();
            }
        });
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String username = usernameET.getText().toString();

                if(username.length() < 5) {
                    //TODO check for non-numerical/letter characters
                    //TODO check to make sure username is unique in firebase
                    Toast.makeText(LoginActivity.this, "Invalid Username! Try a longer name.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                BloQueryApplication.getSharedUser().updateUsername(username);

                Firebase.ResultHandler handler = new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(LoginActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
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

    private void attemptingLogin() {
        BloQueryAnimator.fadeOutView(loginWindow);
        findViewById(R.id.login_progress_spinner).setVisibility(View.VISIBLE);
    }

    private void resetLogin() {
        BloQueryAnimator.fadeInView(loginWindow);
        findViewById(R.id.login_progress_spinner).setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if(createAccountWindowIsOpen) {
            BloQueryAnimator.fadeOutThenInViews(createAccountWindow, loginWindow);
            createAccountWindowIsOpen = false;
        }
    }
}

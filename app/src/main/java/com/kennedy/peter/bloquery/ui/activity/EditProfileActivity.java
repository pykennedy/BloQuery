package com.kennedy.peter.bloquery.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.customViews.EditTextWithDone;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;

public class EditProfileActivity extends DrawerActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String UID = BloQueryApplication.getSharedUser().UID;
        setContentView(R.layout.edit_profile_activity);
        final EditTextWithDone editDescription = (EditTextWithDone)findViewById(R.id.edit_profile_description_text);
        Button saveButton = (Button)findViewById(R.id.edit_profile_save_button);
        Button cancelButton = (Button)findViewById(R.id.edit_profile_cancel_button);

        String descriptionText = BloQueryApplication.getSharedInstance().getDataSource().getUserFromUID(UID).getDescription();
        if(descriptionText == null || descriptionText.equals("")) {
            editDescription.setText("");
            descriptionText = "Write something about yourself!";
        }
        editDescription.setHint(descriptionText);

        getProfilePicture().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfileActivity.this, "Profile pic clicked", Toast.LENGTH_LONG).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseManager firebaseManager = new FirebaseManager();
                Firebase.CompletionListener listener = new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if(firebaseError != null) {
                            Toast.makeText(EditProfileActivity.this, "Error: " + firebaseError,
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class)
                                    .putExtra("UID", BloQueryApplication.getSharedUser().UID);
                            startActivity(intent);
                        }
                    }
                };
                firebaseManager.updateUser(listener, editDescription.getText().toString(), "picture");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class)
                        .putExtra("UID", BloQueryApplication.getSharedUser().UID);
                startActivity(intent);
            }
        });


    }
}

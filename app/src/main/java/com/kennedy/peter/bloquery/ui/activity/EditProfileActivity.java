package com.kennedy.peter.bloquery.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kennedy.peter.bloquery.R;

public class EditProfileActivity extends DrawerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        getProfilePicture().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfileActivity.this, "Profile pic clicked", Toast.LENGTH_LONG).show();
            }
        });
    }
}

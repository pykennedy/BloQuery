package com.kennedy.peter.bloquery.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.api.model.User;
import com.kennedy.peter.bloquery.customViews.EditTextWithDone;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;
import com.kennedy.peter.bloquery.helpers.PhotoManipulation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends DrawerActivity {
    private Uri outputFileUri;
    private User profileUser;
    private String profilePicB64;
    private Bitmap profilePicBM;

    private void openImageIntent() {

        final String fname = "img_" + System.currentTimeMillis() + "_";
        final File sdImageMainDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(fname, ".jpg", sdImageMainDirectory);
            outputFileUri = Uri.fromFile(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;

                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }

                try {
                    profilePicBM = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    PhotoManipulation pm = new PhotoManipulation();
                    profilePicBM = pm.scaleDownBitmap(profilePicBM);
                    //bitmap = pm.rotateBitmapIfNeeded(selectedImageUri.getPath(), bitmap);
                    profilePicB64 = pm.bitmapToBase64(profilePicBM);
                    ((ImageView)getProfilePicture()).setImageBitmap(profilePicBM);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String UID = BloQueryApplication.getSharedUser().UID;
        setContentView(R.layout.edit_profile_activity);
        final EditTextWithDone editDescription = (EditTextWithDone)findViewById(R.id.edit_profile_description_text);
        Button saveButton = (Button)findViewById(R.id.edit_profile_save_button);
        Button cancelButton = (Button)findViewById(R.id.edit_profile_cancel_button);

        Intent intent = getIntent();
        String uidToSend = intent.getStringExtra("UID");

        setTitleText(BloQueryApplication.getSharedInstance().getDataSource().getUserFromUID(uidToSend).getUserName());

        profileUser = BloQueryApplication.getSharedInstance().getDataSource().getUserFromUID(UID);

        profilePicB64 = profileUser.getProfilePic();
        if(profilePicB64 != null && profilePicB64.length() <= 50) {
            profilePicB64 = null;
        }
        String descriptionText = profileUser.getDescription();
        if(descriptionText == null || descriptionText.equals("")) {
            editDescription.setText("");
            descriptionText = "Write something about yourself!";
            editDescription.setHint(descriptionText);
        } else {
            editDescription.setText(descriptionText);
        }


        getProfilePicture().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfileActivity.this, "Profile pic clicked", Toast.LENGTH_LONG).show();
                openImageIntent();
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
                            SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(BloQueryApplication.getSharedUser().UID + "profilePic",
                                    profilePicB64);
                            editor.commit();
                            Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class)
                                    .putExtra("UID", BloQueryApplication.getSharedUser().UID);
                            startActivity(intent);
                        }
                    }
                };

                firebaseManager.updateUser(listener, editDescription.getText().toString(), profilePicB64);
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

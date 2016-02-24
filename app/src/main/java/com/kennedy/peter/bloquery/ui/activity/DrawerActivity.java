package com.kennedy.peter.bloquery.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.dialogs.AskQuestionDialog;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;

public abstract class DrawerActivity extends AppCompatActivity implements AskQuestionDialog.NoticeDialogListener {
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Menu menu;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_drawer);
        ViewGroup contentRoot = (ViewGroup) findViewById(R.id.drawer_subclass);
        getLayoutInflater().inflate(layoutResID, contentRoot);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setMenuButtonsEnabled(true);
            }
            @Override
            public  void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setMenuButtonsEnabled(false);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        TextView askQuestion = (TextView) findViewById(R.id.drawer_ask_question);

        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestionDialog();
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
                    drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    private void setMenuButtonsEnabled(boolean enabled) {
        if(menu == null)
            return;
        for(int i = 0; i < menu.size(); i++)
            menu.getItem(i).setEnabled(enabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item != null && item.getItemId() == R.id.menu_drawer) {
            if(drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
            else {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void showQuestionDialog() {
        DialogFragment dialogFragment = new AskQuestionDialog();
        dialogFragment.show(getSupportFragmentManager(), "AskQuestionDialog");
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, DialogInterface dialogInterface) {
        Dialog f = (Dialog) dialogInterface;
        EditText dialogQuestion = (EditText) f.findViewById(R.id.dialog_text_input);
        String question = dialogQuestion.getText().toString();
        if(question.length() < 5)
            Toast.makeText(DrawerActivity.this, "Invalid Question", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(DrawerActivity.this, question, Toast.LENGTH_SHORT).show();
            Firebase.CompletionListener listener = new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if(firebaseError != null) {
                        Toast.makeText(DrawerActivity.this, "Error: " + firebaseError, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        // TODO start intent to launch activity for a single Q/A stream
                    }
                }
            };
            FirebaseManager firebaseManager = new FirebaseManager();
            firebaseManager.addQuestion(listener, question, BloQueryApplication.getSharedUser().UID);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment, DialogInterface dialogInterface) {

    }
}

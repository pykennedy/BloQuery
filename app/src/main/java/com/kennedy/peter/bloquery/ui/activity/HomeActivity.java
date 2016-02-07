package com.kennedy.peter.bloquery.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.api.DataSource;
import com.kennedy.peter.bloquery.api.model.Question;
import com.kennedy.peter.bloquery.dialogs.AskQuestionDialog;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;
import com.kennedy.peter.bloquery.ui.adapter.ItemAdapter;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements AskQuestionDialog.NoticeDialogListener {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private EditText dialogQuestion;
    private Menu menu;

    private ItemAdapter itemAdapter;

    private String question = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
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

        // this feels bad, i should have all the onclick listeners for the drawer in one place instead
        // of repeating this code 5 times for each activity
        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestionDialog();
                if(drawerLayout.isDrawerOpen(Gravity.RIGHT))
                    drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        final FirebaseManager firebaseManager = new FirebaseManager();
        DataSource dataSource = BloQueryApplication.getSharedInstance().getDataSource();
        //dataSource.getQuestionList() =
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Question> qList = firebaseManager.getAllQuestionList();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        itemAdapter = new ItemAdapter();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter);
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

    private void setMenuButtonsEnabled(boolean enabled) {
        if(menu == null)
            return;
        for(int i = 0; i < menu.size(); i++)
            menu.getItem(i).setEnabled(enabled);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
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
        dialogQuestion = (EditText)f.findViewById(R.id.dialog_question);
        question = dialogQuestion.getText().toString();
        if(question.length() < 5)
            Toast.makeText(HomeActivity.this, "Invalid Question", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(HomeActivity.this, question, Toast.LENGTH_SHORT).show();
            Firebase.CompletionListener listener = new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if(firebaseError != null) {
                        Toast.makeText(HomeActivity.this, "Error: " + firebaseError, Toast.LENGTH_SHORT).show();
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

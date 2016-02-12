package com.kennedy.peter.bloquery.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.api.DataSource;
import com.kennedy.peter.bloquery.dialogs.AskQuestionDialog;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;
import com.kennedy.peter.bloquery.ui.adapter.ItemAdapter;

public class HomeActivity extends DrawerActivity implements AskQuestionDialog.NoticeDialogListener {




    private ItemAdapter itemAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);



        final FirebaseManager firebaseManager = new FirebaseManager();
        DataSource dataSource = BloQueryApplication.getSharedInstance().getDataSource();

        itemAdapter = new ItemAdapter();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter);
        final View progressSpinner = findViewById(R.id.home_progress_spinner);
        progressSpinner.setVisibility(View.VISIBLE);

        firebaseManager.questionScanner(new FirebaseManager.Listener() {
            @Override
            public void onDataLoaded() {
                progressSpinner.setVisibility(View.GONE);
                itemAdapter.notifyDataSetChanged();
            }
        });
    }

}

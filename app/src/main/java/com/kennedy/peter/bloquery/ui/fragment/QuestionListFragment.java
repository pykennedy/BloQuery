package com.kennedy.peter.bloquery.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.api.DataSource;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;
import com.kennedy.peter.bloquery.ui.OnQuestionClickListener;
import com.kennedy.peter.bloquery.ui.adapter.ItemAdapterHome;

public class QuestionListFragment extends Fragment {
    private ItemAdapterHome itemAdapterHome;
    private OnQuestionClickListener listener;
    private RecyclerView recyclerView;
    private View progressSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.home_question_list_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.home_recycler_view);
        progressSpinner = rootView.findViewById(R.id.home_progress_spinner);

        final FirebaseManager firebaseManager = new FirebaseManager();
        DataSource dataSource = BloQueryApplication.getSharedInstance().getDataSource();

        itemAdapterHome = new ItemAdapterHome(listener);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapterHome);
        progressSpinner.setVisibility(View.VISIBLE);

        firebaseManager.questionScanner(new FirebaseManager.Listener() {
            @Override
            public void onDataLoaded() {
                progressSpinner.setVisibility(View.GONE);
                itemAdapterHome.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnQuestionClickListener) {
            this.listener = (OnQuestionClickListener) activity;
        } else {
            throw new IllegalArgumentException("In order to use QuestionListFragment, activity must implement OnQuestionClickListener");
        }
    }
}
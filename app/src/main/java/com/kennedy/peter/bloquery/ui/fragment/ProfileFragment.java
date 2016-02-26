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
import com.kennedy.peter.bloquery.ui.OnQAClickListener;
import com.kennedy.peter.bloquery.ui.adapter.ItemAdapterProfile;
import com.kennedy.peter.bloquery.ui.animations.HeaderDecoration;

public class ProfileFragment extends Fragment {
    private ItemAdapterProfile itemAdapterProfile;
    private Listener listener;
    private RecyclerView recyclerView;
    private View progressSpinner;
    private String UID;
    private DataSource dataSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.profile_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.profile_recycler_view);
        progressSpinner = rootView.findViewById(R.id.profile_progress_spinner);

        final FirebaseManager firebaseManager = new FirebaseManager();
        dataSource = BloQueryApplication.getSharedInstance().getDataSource();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //HeaderDecoration.Builder builder = new HeaderDecoration.Builder();
        View description = getLayoutInflater(savedInstanceState).inflate(R.layout.profile_description, null);
        HeaderDecoration headerDecoration = new HeaderDecoration(
                description, false, 1f, 20f, 1);
        recyclerView.addItemDecoration(HeaderDecoration.with(recyclerView)
                .inflate(R.layout.profile_description)
                .parallax(0.2f)
                .dropShadowDp(6)
                .build());

        progressSpinner.setVisibility(View.VISIBLE);

        firebaseManager.specificUserScanner(UID, new FirebaseManager.Listener() {
            @Override
            public void onDataLoaded() {
                refresh();
            }

            @Override
            public void onDataChanged() {
                listener.onQAAdded();
                listener.onAnswerAdded();
            }
        });

        return rootView;
    }

    public void refresh() {
        progressSpinner.setVisibility(View.GONE);
        itemAdapterProfile = new ItemAdapterProfile(dataSource.getSortedQAFromUID(UID), new OnQAClickListener() {
            @Override
            public void onQAClick(String qaPushID) {
                listener.onQAClick(qaPushID);
            }
        });
        recyclerView.setAdapter(itemAdapterProfile);
        itemAdapterProfile.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Listener) {
            this.listener = (Listener) activity;
        } else {
            throw new IllegalArgumentException("In order to use ProfileFragment, activity must " +
                    "implement OnQAClickListener");
        }
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public interface Listener {
        void onQAClick(String questionPushID);
        void onAnswerAdded();
        void onQAAdded();
    }
}
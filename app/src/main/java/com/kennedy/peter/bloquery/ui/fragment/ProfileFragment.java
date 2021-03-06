package com.kennedy.peter.bloquery.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.api.DataSource;
import com.kennedy.peter.bloquery.api.model.QA;
import com.kennedy.peter.bloquery.api.model.User;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;
import com.kennedy.peter.bloquery.helpers.PhotoManipulation;
import com.kennedy.peter.bloquery.ui.OnQAClickListener;
import com.kennedy.peter.bloquery.ui.activity.ProfileActivity;
import com.kennedy.peter.bloquery.ui.adapter.ItemAdapterProfile;
import com.kennedy.peter.bloquery.ui.animations.HeaderDecoration;

import java.util.List;

public class ProfileFragment extends Fragment {
    private ItemAdapterProfile itemAdapterProfile;
    private Listener listener;
    private RecyclerView recyclerView;
    private View progressSpinner;
    private String UID;
    private DataSource dataSource;
    private TextView description;
    private String descriptionText;
    private User profileUser;
    private Activity activity;

    private static final String EXTRA_UID = "EXTRA_UID";

    ;@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState!= null && savedInstanceState.containsKey(EXTRA_UID)) {
            UID = savedInstanceState.getString(EXTRA_UID);
        }
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.profile_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.profile_recycler_view);
        progressSpinner = rootView.findViewById(R.id.profile_progress_spinner);
        description = (TextView)rootView.findViewById(R.id.profile_default_description_tv);

        final FirebaseManager firebaseManager = new FirebaseManager();
        dataSource = BloQueryApplication.getSharedInstance().getDataSource();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        profileUser = dataSource.getUserFromUID(UID);

        descriptionText = profileUser.getDescription();
        if(descriptionText == null || descriptionText.equals("")) {
            descriptionText = "No description written.";
        }

        String profilePicB64 = profileUser.getProfilePic();
        if(profilePicB64 != null && profilePicB64.length() >= 50) {
            PhotoManipulation pm = new PhotoManipulation();
            Bitmap profilePicBM = pm.base64ToBitmap(profilePicB64);
            ((ImageView) ((ProfileActivity) getActivity()).getProfilePicture()).setImageBitmap(profilePicBM);
        } else {
            ((ImageView) ((ProfileActivity) getActivity()).getProfilePicture()).setImageResource(R.drawable.default_picture);
        }
        recyclerView.addItemDecoration(HeaderDecoration.with(recyclerView,
                descriptionText)
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
        firebaseManager.questionScanner(new FirebaseManager.Listener() {
            @Override
            public void onDataLoaded() {
                progressSpinner.setVisibility(View.GONE);
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
        List<QA> qaList = dataSource.getSortedQAFromUID(UID);
        if(qaList != null) {
            description.setVisibility(View.GONE);
            itemAdapterProfile = new ItemAdapterProfile(qaList, new OnQAClickListener() {
                @Override
                public void onQAClick(String qaPushID) {
                    listener.onQAClick(qaPushID);
                }
            });
            recyclerView.setAdapter(itemAdapterProfile);
            itemAdapterProfile.notifyDataSetChanged();
        } else {
            description.setVisibility(View.VISIBLE);
            description.setText(descriptionText);
            if(descriptionText.equals("No description written."))
                description.setTextColor(getResources().getColor(R.color.text_hint));
            else
                description.setTextColor(getResources().getColor(R.color.text_primary));
        }
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
    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState); outState.putString(EXTRA_UID, UID);
    }
}
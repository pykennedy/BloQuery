package com.kennedy.peter.bloquery.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kennedy.peter.bloquery.R;

public class QuestionWithAnswersFragment extends Fragment {
    public static String questionPushID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.full_qa_fragment, container, false);
        //Toast.makeText(getContext(), "pie", Toast.LENGTH_SHORT).show();
        return rootView;
    }

    public void refreshQuestion(String questionPushID) {
        //TODO refresh data for this pushID
        Toast.makeText(getContext(), "PushID: " + questionPushID, Toast.LENGTH_LONG).show();
    }
}

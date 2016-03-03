package com.kennedy.peter.bloquery.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.api.DataSource;
import com.kennedy.peter.bloquery.api.model.Answer;
import com.kennedy.peter.bloquery.dialogs.AnswerQuestionDialog;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;
import com.kennedy.peter.bloquery.ui.adapter.ItemAdapterFullQA;

import java.util.List;

public class HomeQAFragment extends Fragment implements AnswerQuestionDialog.NoticeDialogListener {
    private String questionPushID = "-KBsnLC5xIycvRnnn8f2";
    private View progressSpinner;
    private RecyclerView recyclerView;
    private ItemAdapterFullQA itemAdapterFullQA;
    private DataSource dataSource;
    //TODO   the progress spinner is not spinning since its like android is reusing the previous spinner
    //TODO   so its just stuck in place in the same position it was when login activity ended

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Fragments", "Tag: " + getTag());
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.full_qa_fragment, container, false);
        Button answerButton = (Button)rootView.findViewById(R.id.full_qa_answer_button);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.full_qa_recycler_view);
        progressSpinner = rootView.findViewById(R.id.full_qa_progress_spinner);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestionDialog();
            }
        });

        final FirebaseManager firebaseManager = new FirebaseManager();
        dataSource = BloQueryApplication.getSharedInstance().getDataSource();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        progressSpinner.setVisibility(View.VISIBLE);

        firebaseManager.answerScanner(new FirebaseManager.Listener() {
            @Override
            public void onDataLoaded() {
                        refresh();
                    }

            @Override
            public void onDataChanged() {
                refresh();
            }
        });

        return rootView;
    }

    public void showQuestionDialog() {
        DialogFragment dialogFragment = new AnswerQuestionDialog();
        dialogFragment.show(getFragmentManager(), "AnswerQuestionDialog");
    }

    public void refreshQuestion(String pushID) {
        //TODO refresh data for this pushID
        this.questionPushID = pushID;
        refresh();
        Toast.makeText(getContext(), "PushID: " + questionPushID, Toast.LENGTH_SHORT).show();
    }

    public void refresh() {
        Log.e("Fragments", "Tag: " + getTag());

        progressSpinner.setVisibility(View.GONE);
        List<Answer> answersFromQuestionID = dataSource.getSortedAnswersFromQuestionID(questionPushID);
        itemAdapterFullQA = new ItemAdapterFullQA(answersFromQuestionID,
                dataSource.getQuestionFromQuestionID(questionPushID));
        recyclerView.setAdapter(itemAdapterFullQA);
        itemAdapterFullQA.notifyDataSetChanged();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, DialogInterface dialogInterface) {
        Dialog f = (Dialog) dialogInterface;
        EditText dialogAnswer = (EditText) f.findViewById(R.id.dialog_text_input);
        String answer = dialogAnswer.getText().toString();
        if (answer.equals(""))
            Toast.makeText(getContext(), "Invalid Answer", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(getContext(), answer, Toast.LENGTH_SHORT).show();
            Firebase.CompletionListener listener = new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError != null) {
                        Toast.makeText(getContext(), "Error: " + firebaseError, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        // dunno if i need anything here yet
                    }
                }
            };
            FirebaseManager firebaseManager = new FirebaseManager();
            firebaseManager.addAnswer(listener, answer, BloQueryApplication.getSharedUser().UID, questionPushID);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment, DialogInterface dialogInterface) {}


}

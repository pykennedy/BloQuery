package com.kennedy.peter.bloquery.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import com.kennedy.peter.bloquery.api.model.Question;
import com.kennedy.peter.bloquery.dialogs.AnswerQuestionDialog;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;

import java.util.List;

public class QuestionWithAnswersFragment extends Fragment implements AnswerQuestionDialog.NoticeDialogListener {
    private String questionPushID;

    public String getQuestionPushID() { return questionPushID; }
    public void setQuestionPushID() {
        List<Question> questionList= BloQueryApplication.getSharedInstance().getDataSource().getQuestionList();
        if(questionList != null)
            this.questionPushID = questionList.get(questionList.size()-1).getPushID();
        Toast.makeText(getContext(), "PushID: " + questionPushID, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.full_qa_fragment, container, false);
        Button answerButton = (Button)rootView.findViewById(R.id.full_qa_answer_button);

        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestionDialog();

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
        Toast.makeText(getContext(), "PushID: " + questionPushID, Toast.LENGTH_SHORT).show();
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
            firebaseManager.addAnswer(listener, answer, BloQueryApplication.getSharedUser().UID,
                    BloQueryApplication.getSharedUser().userName, questionPushID);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment, DialogInterface dialogInterface) {

    }
}

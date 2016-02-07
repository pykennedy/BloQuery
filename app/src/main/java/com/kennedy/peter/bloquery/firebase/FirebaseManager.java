package com.kennedy.peter.bloquery.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.api.model.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    https://docs.google.com/spreadsheets/d/1eo6aBG3pLgFaFlRKfZJU8JUYiYLOKzchLjYi89TCoe8/edit?usp=sharing
    ^^ spreadsheet detailing firebase structure
 */

public class FirebaseManager {
    Firebase firebase = new Firebase("https://flickering-torch-9808.firebaseio.com/");

    public void logIn(String email, String password, Firebase.AuthResultHandler handler) {
        firebase.authWithPassword(email, password, handler);
    }

    public void createAccount(String email, String password, Firebase.ResultHandler handler) {
        firebase.createUser(email, password, handler);
    }

    public void addUser(Firebase.CompletionListener listener) {
        Firebase fbUsers = firebase.child("users/" + BloQueryApplication.getSharedUser().UID);
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userName", BloQueryApplication.getSharedUser().userName);
        fbUsers.setValue(userInfo, listener);
    }

    public void addQuestion(Firebase.CompletionListener listener, String questionText, String userID) {
        //TODO   if failure to add pushID to the users database, store pushID and userID together in
        //TODO   SharedPreferences and attempt to add it again when user logs in or during 5minute update
        Firebase fbQuestions = firebase.child("QuestionsAnswers/questions");
        Firebase fullPath = fbQuestions.push();
        Map<String, String> questionInfo = new HashMap<>();
        questionInfo.put("questionText", questionText);
        questionInfo.put("askingUserID", userID);
        questionInfo.put("dateAsked", Long.toString(System.currentTimeMillis()));
        fullPath.setValue(questionInfo, listener);
    }

    public List<Question> getAllQuestionList() {
        Firebase fbQuestions = firebase.child("QuestionsAnswers/questions");
        Query fbQuery = fbQuestions.orderByChild("dateAsked");
        final List<Question> questionList = BloQueryApplication.getSharedInstance().getDataSource().getQuestionList();
        fbQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    Question question = questionSnapshot.getValue(Question.class);
                    System.out.println("QUERY " + question.getQuestionText());
                    questionList.add(question);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return questionList;
    }
}

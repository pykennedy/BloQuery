package com.kennedy.peter.bloquery.firebase;

import com.firebase.client.ChildEventListener;
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

    public void addQuestion(Firebase.CompletionListener listener, String questionText, String userID, String userName) {
        //TODO   if failure to add pushID to the users database, store pushID and userID together in
        //TODO   SharedPreferences and attempt to add it again when user logs in or during 5minute update
        Firebase fbQuestions = firebase.child("QuestionsAnswers/questions");
        Firebase fullPath = fbQuestions.push();
        String[] splitPath = fullPath.toString().split("/");
        String pushID = splitPath[splitPath.length-1];

        Map<String, String> questionInfo = new HashMap<>();
        questionInfo.put("questionText", questionText);
        questionInfo.put("askingUserID", userID);
        questionInfo.put("askingUserName", userName);
        questionInfo.put("dateAsked", Long.toString(System.currentTimeMillis()));
        fullPath.setValue(questionInfo, listener);

        Firebase fbUsers = firebase.child("users/" + BloQueryApplication.getSharedUser().UID);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("questions", pushID);
        fbUsers.updateChildren(userInfo);
    }

    public void questionScanner(final Listener dataListener) {
        final Firebase fbQuestions = firebase.child("QuestionsAnswers/questions");
        final List<Question> questionList = BloQueryApplication.getSharedInstance().getDataSource().getQuestionList();
        fbQuestions.orderByChild("dateAsked").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Question question = dataSnapshot.getValue(Question.class);
                    System.out.println("QUERY " + question.getQuestionText());
                    questionList.add(question);

                dataListener.onDataLoaded();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void initAllQuestionList(final Listener dataListener) {
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
                dataListener.onDataLoaded();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public interface Listener {
        void onDataLoaded();
    }
}

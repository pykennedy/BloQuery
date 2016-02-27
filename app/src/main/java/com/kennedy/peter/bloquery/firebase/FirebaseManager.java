package com.kennedy.peter.bloquery.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.api.model.Answer;
import com.kennedy.peter.bloquery.api.model.Question;
import com.kennedy.peter.bloquery.api.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    https://docs.google.com/spreadsheets/d/1eo6aBG3pLgFaFlRKfZJU8JUYiYLOKzchLjYi89TCoe8/edit?usp=sharing
    ^^ spreadsheet detailing firebase structure
 */
// TODO handle errors from shaky network connection


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
        Map<String, Object> userInfo = new HashMap<>();
        LocalUser localUser = BloQueryApplication.getSharedUser();
        userInfo.put("userName", localUser.userName);
        userInfo.put("email", localUser.email);
        // manage these with SharedPreferences
        //userInfo.put("description", "No Description Set");
        //userInfo.put("profilePic", "dunno how im gonna do this i read it's possible");
        fbUsers.updateChildren(userInfo, listener);
    }

    public void addQuestion(Firebase.CompletionListener listener, String questionText, String userID) {
        //TODO   if failure to add pushID to the users database, store pushID and userID together in
        //TODO   SharedPreferences and attempt to add it again when user logs in or during 5minute update
        Firebase fbQuestions = firebase.child("QuestionsAnswers/questions");
        Firebase fullPath = fbQuestions.push();
        String[] splitPath = fullPath.toString().split("/");
        String pushID = splitPath[splitPath.length-1];

        Map<String, String> questionInfo = new HashMap<>();
        questionInfo.put("questionText", questionText);
        questionInfo.put("askingUserID", userID);
        questionInfo.put("dateAsked", Long.toString(System.currentTimeMillis()));
        fullPath.setValue(questionInfo, listener);

        Firebase fbUsers = firebase.child("users/" + userID + "/questions");
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(pushID, pushID);
        fbUsers.updateChildren(userInfo);
    }

    public void addAnswer(Firebase.CompletionListener listener, String answerText, String userID,
                          String userName, String questionPushID) {
        //TODO   if failure to add pushID to the users and questions database, store pushID, userID, and questionID
        //TODO   together in SharedPreferences and attempt to add it again when user logs in or during 5minute update
        Firebase fbAnswers = firebase.child("QuestionsAnswers/answers");
        Firebase fullPath = fbAnswers.push();
        String[] splitPath = fullPath.toString().split("/");
        String pushID = splitPath[splitPath.length-1];

        Map<String, String> answerInfo = new HashMap<>();
        answerInfo.put("questionPushID", questionPushID);
        answerInfo.put("answerText", answerText);
        answerInfo.put("answeringUserID", userID);
        answerInfo.put("dateAnswered", Long.toString(System.currentTimeMillis()));
        answerInfo.put("upVotes", "0");
        fullPath.setValue(answerInfo, listener);

        // add pushID into users table
        Firebase fbUsers = firebase.child("users/" + userID + "/answers");
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(pushID, pushID);
        fbUsers.updateChildren(userInfo);

        // add pushID into questions table
        Firebase fbQuestionAnswers = firebase.child("QuestionsAnswers/questions/" + questionPushID + "/answers");
        Map<String, Object> questionAnswersInfo = new HashMap<>();
        questionAnswersInfo.put(pushID, pushID);
        fbQuestionAnswers.updateChildren(questionAnswersInfo);
    }

    public void questionScanner(final Listener dataListener) {
        final Firebase fbQuestions = firebase.child("QuestionsAnswers/questions");
        final List<Question> questionList = BloQueryApplication.getSharedInstance().getDataSource().getQuestionList();
        fbQuestions.orderByChild("dateAsked").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Question question = dataSnapshot.getValue(Question.class);
                question.setPushID(dataSnapshot.getKey());
                questionList.add(question);

                dataListener.onDataLoaded();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Question question = dataSnapshot.getValue(Question.class);
                question.setPushID(dataSnapshot.getKey());
                BloQueryApplication.getSharedInstance().getDataSource().updateQuestionInList(question);

                dataListener.onDataChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    public void answerScanner(final Listener dataListener) {
        final Firebase fbAnswers = firebase.child("QuestionsAnswers/answers");
        final List<Answer> answerList = BloQueryApplication.getSharedInstance().getDataSource().getAnswerList();
        fbAnswers.orderByChild("upVotes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Answer answer = dataSnapshot.getValue(Answer.class);
                answer.setAnswerPushID(dataSnapshot.getKey());
                answerList.add(answer);

                dataListener.onDataLoaded();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    public void userScanner(final Listener dataListener) {
        final Firebase fbUsers = firebase.child("users");
        final Map<String, User> userMap = BloQueryApplication.getSharedInstance().getDataSource().getUserMap();
        //.orderByChild("userName")
        fbUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                user.setUID(dataSnapshot.getKey());
                userMap.put(user.getUID(), user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                user.setUID(dataSnapshot.getKey());
                userMap.put(user.getUID(), user);
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

    public void specificUserScanner(final String UID, final Listener dataLisener) {
        final Firebase fbUser = firebase.child("users");
        final Map<String, User> userMap = BloQueryApplication.getSharedInstance().getDataSource().getUserMap();
        fbUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey() != null) {
                    if (UID.equals(dataSnapshot.getKey())) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setUID(dataSnapshot.getKey());
                        userMap.put(user.getUID(), user);

                        dataLisener.onDataLoaded();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(UID.equals(dataSnapshot.getKey())) {
                    User user = dataSnapshot.getValue(User.class);
                    BloQueryApplication.getSharedInstance().getDataSource().updateUserInMap(user);

                    dataLisener.onDataChanged();
                }
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

    public interface Listener {
        void onDataLoaded();

        void onDataChanged();
    }
}

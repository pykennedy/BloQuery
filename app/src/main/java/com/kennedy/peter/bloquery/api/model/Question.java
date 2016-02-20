package com.kennedy.peter.bloquery.api.model;

import com.kennedy.peter.bloquery.BloQueryApplication;

import java.util.Map;

public class Question {
    String pushID;
    String questionText;
    String askingUserID;
    String dateAsked;
    Map<String, String> answers;

    public String getQuestionText() {
        return questionText;
    }
    public String getAskingUserID() {
        return askingUserID;
    }
    public String getDateAsked() {
        return dateAsked;
    }
    public String getAskingUserNameFomID(String UID) {
        return BloQueryApplication.getSharedInstance().getDataSource().getUserMap().get(UID).getUserName();
    }
    public String getPushID() { return pushID; }
    public Map<String, String> getAnswers() { return answers; }

    public void setPushID(String pushID) {
        this.pushID = pushID;
    }

    @Override
    public String toString() {
        return "questions{askingUserID='"+askingUserID+"', dateAsked='"+
                dateAsked+"', questionText='"+questionText+"\'}";
    }
}

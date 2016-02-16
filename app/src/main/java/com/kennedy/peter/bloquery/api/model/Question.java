package com.kennedy.peter.bloquery.api.model;

import java.util.Map;

public class Question {
    String askingUserID;
    String askingUserName;
    String dateAsked;
    String questionText;
    String pushID;
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
    public String getAskingUserName() {
        return askingUserName;
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

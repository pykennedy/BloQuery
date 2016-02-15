package com.kennedy.peter.bloquery.api.model;

public class Question {
    String askingUserID;
    String askingUserName;
    String dateAsked;
    String questionText;
    String pushID;
    //private List<Answer> answerList;

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

    public void setPushID(String pushID) {
        this.pushID = pushID;
    }

    @Override
    public String toString() {
        return "questions{askingUserID='"+askingUserID+"', dateAsked='"+
                dateAsked+"', questionText='"+questionText+"\'}";
    }
}

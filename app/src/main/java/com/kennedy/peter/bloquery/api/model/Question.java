package com.kennedy.peter.bloquery.api.model;

public class Question {
    String askingUserID;
    String dateAsked;
    String questionText;

    //private List<Answer> answerList;

    public Question() {

    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAskingUserID() {
        return askingUserID;
    }

    public String getDateAsked() {
        return dateAsked;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setAskingUserID(String askingUserID) {
        this.askingUserID = askingUserID;
    }

    public void setDateAsked(String dateAsked) {
        this.dateAsked = dateAsked;
    }

    @Override
    public String toString() {
        return "questions{askingUserID='"+askingUserID+"', dateAsked='"+
                dateAsked+"', questionText='"+questionText+"\'}";
    }
}

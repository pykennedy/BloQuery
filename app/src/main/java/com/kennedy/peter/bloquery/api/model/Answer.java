package com.kennedy.peter.bloquery.api.model;

import com.kennedy.peter.bloquery.BloQueryApplication;

import java.util.Map;

public class Answer extends QA {
    String answerPushID;
    String questionPushID;
    String answerText;
    String answeringUserID;
    String dateAnswered;
    Map<String, String> upVoters;

    public String getAnswerPushID() {
        return answerPushID;
    }
    public void setAnswerPushID(String pushID) { this.answerPushID = pushID; }
    public String getQuestionPushID() {
        return questionPushID;
    }
    public String getAnswerText() {
        return answerText;
    }
    public String getAnsweringUserID() {
        return answeringUserID;
    }
    public String getAnsweringUserNameFromUID(String UID) {
        return BloQueryApplication.getSharedInstance().getDataSource().getUserMap().get(UID).getUserName();
    }
    public String getDateAnswered() {
        return dateAnswered;
    }
    public Map<String, String> getUpVoters() {
        return upVoters;
    }
    public String getUpVotes() {
        if(upVoters == null) {
            return "0";
        }
        return String.valueOf(upVoters.size());
    }
}

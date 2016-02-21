package com.kennedy.peter.bloquery.api;

import com.kennedy.peter.bloquery.api.model.Answer;
import com.kennedy.peter.bloquery.api.model.QA;
import com.kennedy.peter.bloquery.api.model.Question;
import com.kennedy.peter.bloquery.api.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSource {
    private List<Question> questionList;
    private List<Answer> answerList;
    private Map<String, User> userMap;

    public DataSource() {
        questionList = new ArrayList<>();
        answerList = new ArrayList<>();
        userMap = new HashMap<>();
    }

    public List<Question> getQuestionList() {
        return questionList;
    }
    public List<Answer> getAnswerList() {
        return answerList;
    }
    public Map<String, User> getUserMap() {
        return userMap;
    }
    public int getQuestionIndexFromQuestionID(String pushID) {
        for(int i = 0; i < questionList.size(); i++) {
            if(questionList.get(i).getPushID() == pushID) {
                return i;
            }
        }
        return -1;
    }
    public void updateQuestionInList(Question question) {
        int i = getQuestionIndexFromQuestionID(question.getPushID());
        if(i>=0)
            questionList.set(i, question);
    }
    public Question getQuestionFromQuestionID(String pushID) {
        return questionList.get(getQuestionIndexFromQuestionID(pushID));
    }
    public List<Answer> getAnswersFromQuestionID(String pushID) {
        Question question = getQuestionFromQuestionID(pushID);
        Map<String, String> answerPushIDMap = question.getAnswers();
        List<Answer> answers = new ArrayList<>();
        for(String answerPushID : answerPushIDMap.values()) {
            for(Answer answer : answerList) {
                if(answer.getAnswerPushID().equals(answerPushID)) {
                    answers.add(answer);
                    break;
                }
            }
        }
        return answers;
    }
    public List<QA> getQAFromUID(String UID) {
        return null;
    }
}

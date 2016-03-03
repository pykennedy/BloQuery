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
            if(questionList.get(i).getPushID().equals(pushID)) {
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
    public void updateAnswerInList(Answer answer) {
        int i = getAnswerIndexFromAnswerID(answer.getAnswerPushID());
        if(i>=0)
            answerList.set(i, answer);
    }
    public void updateUserInMap(User user) {
        userMap.put(user.getUID(), user);
    }
    public Question getQuestionFromQuestionID(String pushID) {
        Question question = questionList.get(getQuestionIndexFromQuestionID(pushID));
        return question;
    }
    public int getAnswerIndexFromAnswerID(String pushID) {
        for(int i = 0; i < answerList.size(); i++) {
            if(answerList.get(i).getAnswerPushID().equals(pushID)) {
                return i;
            }
        }
        return -1;
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
    public List<Answer> getSortedAnswersFromQuestionID(String pushID) {
        List<Answer> list = getAnswersFromQuestionID(pushID);

        for(int i = list.size()-1; i > 0; i--) {
            int baseUV = Integer.parseInt(list.get(i).getUpVotes());
            int index = i;
            for(int j = 0; j < i; j++) {
                int uv = Integer.parseInt(list.get(j).getUpVotes());
                if(uv < baseUV) {
                    index = j;
                }
                Answer smaller = list.get(index);
                list.set(index, list.get(i));
                list.set(i, smaller);
            }
        }
        return list;
    }
    public User getUserFromUID(String UID) {
        return userMap.get(UID);
    }
    public List<QA> getSortedQAFromUID(String UID) {
        User user = getUserFromUID(UID);
        List<QA> qaList = new ArrayList<>();
        if(user.getAnswers() == null && user.getQuestions() == null) {
            return null;
        }
        if(user.getQuestions() != null) {
            for(String pushID : user.getQuestions().values()) {
                for(Question question : questionList) {
                    if(question.getPushID().equals(pushID)) {
                        qaList.add(question);
                        break;
                    }
                }
            }
        }
        if(user.getAnswers() != null) {
            for (String pushID : user.getAnswers().values()) {
                for (Answer answer : answerList) {
                    if (answer.getAnswerPushID().equals(pushID)) {
                        qaList.add(answer);
                        break;
                    }
                }
            }
        }
        for(int i = qaList.size()-1; i > 0; i--) {
            QA baseQA = qaList.get(i);
            QA qa;
            int index = i;
            if(baseQA instanceof Answer) {
                long baseDate = Long.parseLong(((Answer) baseQA).getDateAnswered());
                for(int j = 0; j <= i; j++) {
                    qa = qaList.get(j);
                    long date = (qa instanceof Question) ?
                            Long.parseLong(((Question) qa).getDateAsked()) :
                            Long.parseLong(((Answer)qa).getDateAnswered());
                    if(date < baseDate) {
                        index = j;
                        baseDate = date;
                    }
                }
            } else if(baseQA instanceof Question){
                long baseDate = Long.parseLong(((Question) baseQA).getDateAsked());
                for(int j = 0; j <= i; j++) {
                    qa = qaList.get(j);
                    long date = (qa instanceof Question) ?
                            Long.parseLong(((Question) qa).getDateAsked()) :
                            Long.parseLong(((Answer) qa).getDateAnswered());
                    if(date < baseDate) {
                        index = j;
                        baseDate = date;
                    }
                }
            }
            QA smaller = qaList.get(index);
            qaList.set(index, baseQA);
            qaList.set(i, smaller);
        }
        return qaList;
    }

    public boolean userNameExists(String username) {
        if(userMap.size() > 0) {
            for (User user : userMap.values()) {
                if (user.getUserName().equals(username)) {
                    return true;
                }
            }
        }
        return true;

    }
}

package com.kennedy.peter.bloquery.api;

import com.kennedy.peter.bloquery.api.model.Answer;
import com.kennedy.peter.bloquery.api.model.Question;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private List<Question> questionList;
    private List<Answer> answerList;

    public DataSource() {
        questionList = new ArrayList<>();
        answerList = new ArrayList<>();
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }
}

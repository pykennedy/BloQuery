package com.kennedy.peter.bloquery.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.api.model.Answer;
import com.kennedy.peter.bloquery.api.model.QA;
import com.kennedy.peter.bloquery.api.model.Question;
import com.kennedy.peter.bloquery.helpers.LocalizedDateAndTime;
import com.kennedy.peter.bloquery.ui.OnQAClickListener;

import java.util.List;

public class ItemAdapterProfile extends RecyclerView.Adapter {
    private static final int TYPE_QUESTION = 0;
    private static final int TYPE_ANSWER = 1;
    List<QA> qaList;
    private final OnQAClickListener listener;

    public ItemAdapterProfile(List<QA> qaList, OnQAClickListener listener) {
        this.qaList = qaList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_ANSWER) {
            View inflate = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.answer_item, viewGroup, false);
            return new AnswerItem(inflate);
        } else if(viewType == TYPE_QUESTION) {
            View inflate = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.question_item, viewGroup, false);
            return new QuestionItem(inflate);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AnswerItem) {
            ((AnswerItem)holder).update((Answer)qaList.get(position));
        } else if(holder instanceof QuestionItem) {
            ((QuestionItem)holder).update((Question)qaList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return qaList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(qaList.get(position) instanceof Question)
            return TYPE_QUESTION;
        else
            return TYPE_ANSWER;
    }

    class AnswerItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView answerText;
        TextView dateText;
        TextView answeringUserText;
        Answer answer;

        public AnswerItem(View itemView) {
            super(itemView);
            answerText = (TextView)itemView.findViewById(R.id.profile_list_answer_text);
            dateText = (TextView)itemView.findViewById(R.id.profile_list_answer_date);
            answeringUserText = (TextView)itemView.findViewById(R.id.profile_list_answer_user);
            itemView.setOnClickListener(this);
        }

        public void update(Answer answer) {
            this.answer = answer;
            answerText.setText(answer.getAnswerText());
            dateText.setText(LocalizedDateAndTime.epochToDate(answer.getDateAnswered()));
            answeringUserText.setText(answer.getAnsweringUserNameFromUID(answer.getAnsweringUserID()));
        }

        @Override
        public void onClick(View v) {
            listener.onQAClick(answer.getQuestionPushID());
        }
    }

    class QuestionItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView questionText;
        TextView dateText;
        TextView askingUserText;
        Question question;

        public QuestionItem(View itemView) {
            super(itemView);
            questionText = (TextView)itemView.findViewById(R.id.home_list_question_text);
            dateText = (TextView)itemView.findViewById(R.id.home_list_question_date);
            askingUserText = (TextView)itemView.findViewById(R.id.home_list_question_user);
            itemView.setOnClickListener(this);
        }

        public void update(Question question) {
            this.question = question;
            questionText.setText(question.getQuestionText());
            dateText.setText(LocalizedDateAndTime.epochToDate(question.getDateAsked()));
            askingUserText.setText(question.getAskingUserNameFomID(question.getAskingUserID()));
        }

        @Override
        public void onClick(View v) {
            listener.onQAClick(question.getPushID());
        }
    }
}
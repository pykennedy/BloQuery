package com.kennedy.peter.bloquery.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.api.model.Answer;
import com.kennedy.peter.bloquery.api.model.Question;
import com.kennedy.peter.bloquery.firebase.FirebaseManager;
import com.kennedy.peter.bloquery.helpers.LocalizedDateAndTime;
import com.kennedy.peter.bloquery.ui.activity.ProfileActivity;

import java.util.List;

public class ItemAdapterFullQA extends RecyclerView.Adapter {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    List<Answer> answerList;
    Question question;

    public ItemAdapterFullQA(List<Answer> answerList, Question question) {
        this.answerList = answerList;
        this.question = question;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_ITEM) {
            View inflate = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.full_answer_item, viewGroup, false);
            return new VHItem(inflate);
        } else if(viewType == TYPE_HEADER) {
            View inflate = LayoutInflater.from(
                    viewGroup.getContext()).inflate(R.layout.full_question_item, viewGroup, false);
            return new VHHeader(inflate);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VHItem) {
            ((VHItem)holder).update(answerList.get(position-1));
        } else if(holder instanceof VHHeader) {
            ((VHHeader)holder).update(question);
        }
    }

    @Override
    public int getItemCount() {
        return answerList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView answerText;
        TextView dateText;
        TextView answeringUserText;
        TextView upVotes;
        Answer answer;

        public VHItem(final View itemView) {
            super(itemView);
            answerText = (TextView)itemView.findViewById(R.id.full_qa_answer_text);
            dateText = (TextView)itemView.findViewById(R.id.full_qa_answer_date);
            answeringUserText = (TextView)itemView.findViewById(R.id.full_qa_answer_user);
            upVotes = (TextView)itemView.findViewById(R.id.full_qa_answer_upvotes);
            answeringUserText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ProfileActivity.class)
                            .putExtra("UID", answer.getAnsweringUserID());
                    v.getContext().startActivity(intent);
                }
            });
            upVotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "clicked", Toast.LENGTH_SHORT).show();
                    FirebaseManager firebaseManager = new FirebaseManager();
                    firebaseManager.addUpvote(answer.getAnswerPushID(),
                            BloQueryApplication.getSharedUser().UID);
                }
            });

        }

        public void update(Answer answer) {
            this.answer = answer;
            answerText.setText(answer.getAnswerText());
            dateText.setText(LocalizedDateAndTime.epochToDate(answer.getDateAnswered()));
            answeringUserText.setText(answer.getAnsweringUserNameFromUID(answer.getAnsweringUserID()));
            upVotes.setText(answer.getUpVotes());
        }
    }
    class VHHeader extends RecyclerView.ViewHolder {
        TextView questionText;
        TextView dateText;
        TextView askingUserText;
        Question question;

        public VHHeader(View itemView) {
            super(itemView);
            questionText = (TextView)itemView.findViewById(R.id.full_qa_question_text);
            dateText = (TextView)itemView.findViewById(R.id.full_qa_question_date);
            askingUserText = (TextView)itemView.findViewById(R.id.full_qa_question_user);
            askingUserText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ProfileActivity.class)
                            .putExtra("UID", question.getAskingUserID());
                    v.getContext().startActivity(intent);
                }
            });
        }

        public void update(Question question) {
            this.question = question;
            questionText.setText(question.getQuestionText());
            dateText.setText(LocalizedDateAndTime.epochToDate(question.getDateAsked()));
            askingUserText.setText(question.getAskingUserNameFomID(question.getAskingUserID()));
        }
    }
}

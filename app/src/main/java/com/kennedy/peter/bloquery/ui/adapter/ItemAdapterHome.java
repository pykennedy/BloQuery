package com.kennedy.peter.bloquery.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.api.DataSource;
import com.kennedy.peter.bloquery.api.model.Question;
import com.kennedy.peter.bloquery.helpers.LocalizedDateAndTime;
import com.kennedy.peter.bloquery.ui.activity.FullQAActivity;

public class ItemAdapterHome extends RecyclerView.Adapter<ItemAdapterHome.ItemAdapterViewHolder> {

    @Override
    public ItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_item, viewGroup, false);
        return new ItemAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ItemAdapterViewHolder itemAdapterViewHolder, int index) {
        DataSource dataSource = BloQueryApplication.getSharedInstance().getDataSource();
        // read data in reverse order so most recent stuff is displayed first
        itemAdapterViewHolder.update(dataSource.getQuestionList().get((getItemCount()-1)-index));
    }

    @Override
    public int getItemCount() {
        return BloQueryApplication.getSharedInstance().getDataSource().getQuestionList().size();
    }

    class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView questionText;
        TextView dateText;
        TextView askingUserText;
        Question question;

        public ItemAdapterViewHolder(View itemView) {
            super(itemView);
            questionText = (TextView) itemView.findViewById(R.id.home_list_question_text);
            dateText = (TextView) itemView.findViewById(R.id.home_list_question_date);
            askingUserText = (TextView) itemView.findViewById(R.id.home_list_question_user);
            itemView.setOnClickListener(this);
        }

        void update(Question question) {
            this.question = question;
            questionText.setText(question.getQuestionText());
            dateText.setText(LocalizedDateAndTime.epochToDate(question.getDateAsked()));
            askingUserText.setText(question.getAskingUserName());
        }

        @Override
        public void onClick(View v) {
            Activity activity = (Activity) v.getContext();
            Intent intent = new Intent(activity, FullQAActivity.class);
            intent.putExtra("questionsPushID", question.getPushID());
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            Toast.makeText(v.getContext(), question.getQuestionText(), Toast.LENGTH_SHORT).show();
        }
    }
}

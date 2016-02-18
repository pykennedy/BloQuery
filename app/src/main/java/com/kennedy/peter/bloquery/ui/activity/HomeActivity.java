package com.kennedy.peter.bloquery.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.dialogs.AskQuestionDialog;
import com.kennedy.peter.bloquery.ui.OnQuestionClickListener;
import com.kennedy.peter.bloquery.ui.animations.DepthPageTransformer;
import com.kennedy.peter.bloquery.ui.fragment.QuestionListFragment;
import com.kennedy.peter.bloquery.ui.fragment.QuestionWithAnswersFragment;

public class HomeActivity extends DrawerActivity implements AskQuestionDialog.NoticeDialogListener, OnQuestionClickListener {
    private ViewPager pager;
    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        pager = (ViewPager) findViewById(R.id.home_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setPageTransformer(true, new DepthPageTransformer());
    }

    @Override
    public void onQuestionClick(String questionPushID) {
        pager.setCurrentItem(1);
        ((QuestionWithAnswersFragment)pagerAdapter.getItem(1)).refreshQuestion(questionPushID);
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        private QuestionListFragment questionListFragment;
        private QuestionWithAnswersFragment questionWithAnswersFragment;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(questionListFragment == null)
                questionListFragment = new QuestionListFragment();
            if(questionWithAnswersFragment == null)
                questionWithAnswersFragment = new QuestionWithAnswersFragment();
            switch(position) {
                case 0: return questionListFragment;
                case 1: return questionWithAnswersFragment;
                default: return questionListFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onBackPressed() {
        if(pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            pager.setCurrentItem(pager.getCurrentItem()-1);
    }
}

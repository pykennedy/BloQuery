package com.kennedy.peter.bloquery.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.ui.animations.DepthPageTransformer;
import com.kennedy.peter.bloquery.ui.fragment.HomeQAFragment;
import com.kennedy.peter.bloquery.ui.fragment.QuestionListFragment;

public class HomeActivity extends DrawerActivity implements QuestionListFragment.Listener {
    private ViewPager pager;
    private FragmentStatePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        pager = (ViewPager) findViewById(R.id.home_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setPageTransformer(true, new DepthPageTransformer());
    }

    public Fragment getFullQAFragment() {
        return pagerAdapter.getItem(1);
    }

    @Override
    public void onQuestionClick(String questionPushID) {
        Log.e("frag", "onQuestionClick() - round 1 : " + ((ScreenSlidePagerAdapter) pagerAdapter).getItem(1));
        pager.setCurrentItem(1);
        Log.e("frag", "onQuestionClick() - round 2: " + ((ScreenSlidePagerAdapter) pagerAdapter).getItem(1));
        ((HomeQAFragment)pagerAdapter.getItem(1)).refreshQuestion(questionPushID);
        Log.e("frag", "onQuestionClick() - round 3: " + ((ScreenSlidePagerAdapter) pagerAdapter).getItem(1));
    }

    @Override
    public void onAnswerAdded() {
        ((HomeQAFragment)pagerAdapter.getItem(1)).refresh();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private QuestionListFragment questionListFragment;
        private HomeQAFragment homeQAFragment;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(questionListFragment == null)
                questionListFragment = new QuestionListFragment();
            if(homeQAFragment == null)
                homeQAFragment = new HomeQAFragment();
           // if(homeQAFragment.getQuestionPushID() == null)
             //   homeQAFragment.setQuestionPushID();
            switch(position) {
                case 0: return questionListFragment;
                case 1:
                    Log.e("frag", "getItem(): " + homeQAFragment);
                    return homeQAFragment;
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

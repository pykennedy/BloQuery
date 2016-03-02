package com.kennedy.peter.bloquery.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.ui.animations.DepthPageTransformer;
import com.kennedy.peter.bloquery.ui.fragment.HomeQAFragment;
import com.kennedy.peter.bloquery.ui.fragment.QuestionListFragment;

public class HomeActivity extends DrawerActivity implements QuestionListFragment.Listener {
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

    public Fragment getFullQAFragment() {
        return pagerAdapter.getItem(1);
    }

    @Override
    public void onQuestionClick(String questionPushID) {
        pager.setCurrentItem(1);
        String name = makeFragmentName(pager.getId(), 1);
        Fragment viewPagerFragment = getSupportFragmentManager().findFragmentByTag(name);
        ((HomeQAFragment)viewPagerFragment).refreshQuestion(questionPushID);
    }

    @Override
    public void onAnswerAdded() {
        ((HomeQAFragment)pagerAdapter.getItem(1)).refresh();
    }

    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
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
                case 1: return homeQAFragment;
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

package com.kennedy.peter.bloquery.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.ui.animations.DepthPageTransformer;
import com.kennedy.peter.bloquery.ui.fragment.ProfileFragment;
import com.kennedy.peter.bloquery.ui.fragment.HomeQAFragment;

public class ProfileActivity extends DrawerActivity implements ProfileFragment.Listener {
    private ViewPager pager;
    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        Intent intent = getIntent();
        String uidToSend = intent.getStringExtra("UID");

        pager = (ViewPager) findViewById(R.id.profile_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setPageTransformer(true, new DepthPageTransformer());

        ((ProfileFragment)pagerAdapter.getItem(0)).setUID(uidToSend);
    }

    public Fragment getFullQAFragment() {
        return pagerAdapter.getItem(1);
    }

    @Override
    public void onQAClick(String questionPushID) {
        pager.setCurrentItem(1);
        ((HomeQAFragment)pagerAdapter.getItem(1)).refreshQuestion(questionPushID);
    }

    @Override
    public void onAnswerAdded() {
        ((HomeQAFragment)pagerAdapter.getItem(1)).refresh();
    }

    @Override
    public void onQAAdded() {
        ((ProfileFragment)pagerAdapter.getItem(0)).refresh();
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        private ProfileFragment profileFragment;
        private HomeQAFragment homeQAFragment;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(profileFragment == null)
                profileFragment = new ProfileFragment();
            if(homeQAFragment == null)
                homeQAFragment = new HomeQAFragment();
            switch(position) {
                case 0: return profileFragment;
                case 1: return homeQAFragment;
                default: return profileFragment;
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
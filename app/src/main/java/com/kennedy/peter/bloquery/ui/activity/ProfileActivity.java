package com.kennedy.peter.bloquery.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.kennedy.peter.bloquery.BloQueryApplication;
import com.kennedy.peter.bloquery.R;
import com.kennedy.peter.bloquery.ui.animations.DepthPageTransformer;
import com.kennedy.peter.bloquery.ui.fragment.ProfileFragment;
import com.kennedy.peter.bloquery.ui.fragment.ProfileQAFragment;

public class ProfileActivity extends DrawerActivity implements ProfileFragment.Listener {
    private ViewPager pager;
    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        Intent intent = getIntent();
        final String uidToSend = intent.getStringExtra("UID");

        setTitleText(BloQueryApplication.getSharedInstance().getDataSource().getUserFromUID(uidToSend).getUserName());

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
//        ((ProfileQAFragment)pagerAdapter.getItem(1)).refreshQuestion(questionPushID);
        Log.e("frag", "onQAClick(): " + ((ScreenSlidePagerAdapter)pagerAdapter).getProfileQAFragment());
        ((ScreenSlidePagerAdapter)pagerAdapter).getProfileQAFragment().refreshQuestion(questionPushID);
        Log.e("frag", "onQAClick() - round2: " + ((ScreenSlidePagerAdapter)pagerAdapter).getProfileQAFragment());
        //pager.setCurrentItem(1);
    }

    @Override
    public void onAnswerAdded() {
        ((ScreenSlidePagerAdapter)pagerAdapter).getProfileQAFragment().refresh();
    }

    @Override
    public void onQAAdded() {
        ((ProfileFragment)pagerAdapter.getItem(0)).refresh();
    }

    private static class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        private ProfileFragment profileFragment;
        private ProfileQAFragment profileQAFragment;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(profileFragment == null) {
                profileFragment = new ProfileFragment();
            }
            if(profileQAFragment == null) {
                profileQAFragment = new ProfileQAFragment();
            }
            switch(position) {
                case 0:
                    return profileFragment;
                case 1:
                    Log.e("frag", "getItem(): " + profileQAFragment);
                    return profileQAFragment;
                default: return profileFragment;
            }
        }

        public ProfileQAFragment getProfileQAFragment() {
            return profileQAFragment;
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
package com.kennedy.peter.bloquery.ui.activity;

import android.os.Bundle;

import com.kennedy.peter.bloquery.R;

public class FullQAActivity extends DrawerActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_qa_activity);

        //Toast.makeText(this, "FullQAActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onBackPressed();
    }
}

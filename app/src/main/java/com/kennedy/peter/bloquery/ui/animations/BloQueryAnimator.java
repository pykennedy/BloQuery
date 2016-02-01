package com.kennedy.peter.bloquery.ui.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class BloQueryAnimator {

    public static void fadeOutThenInViews(View view1, View view2) {
        final View out = view1;
        final View in = view2;
        out.animate()
                .alpha(0f)
                .setDuration(250)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        out.setVisibility(View.GONE);
                        in.setAlpha(0f);
                        in.setVisibility(View.VISIBLE);
                        in.animate()
                                .alpha(1f)
                                .setDuration(250)
                                .setListener(null);
                    }
                });
    }
}

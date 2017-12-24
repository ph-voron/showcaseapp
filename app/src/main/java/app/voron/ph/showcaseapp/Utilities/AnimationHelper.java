package app.voron.ph.showcaseapp.Utilities;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import app.voron.ph.showcaseapp.R;

/**
 * Created by TJack on 14.11.2017.
 */

public class AnimationHelper {
    public static void animateViewSwapZoomAndAppear(final View firstView, final View secondView){
        Context context = firstView.getContext();
        Animation zoomAndAppearAnim = AnimationUtils.loadAnimation(context, R.anim.anim_zoom_and_appear);
        Animation zoomAndAppearAnimReverse = AnimationUtils.loadAnimation(context, R.anim.anim_zoom_and_appear_reverse);
        //
        zoomAndAppearAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                firstView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                firstView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        //
        zoomAndAppearAnimReverse.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                secondView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                secondView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        firstView.startAnimation(zoomAndAppearAnim);
        secondView.startAnimation(zoomAndAppearAnimReverse);
    }
    //
    public static void animateViewFadeIn(final  View view, int duration, int delay){
        view.setAlpha(0.0f);
        view.setVisibility(View.GONE);
        view.animate()
                .alpha(1)
                .setDuration(duration)
                .setStartDelay(delay)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }
    //
    public static void animateViewFlyIn(final View view, int duration, int delay){
        int parentWidth = ((View)view.getParent()).getWidth();
        view.setVisibility(View.GONE);
        view.setTranslationX(parentWidth);
        view.animate()
                .translationX(0)
                .setDuration(duration)
                .setStartDelay(delay)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }
}

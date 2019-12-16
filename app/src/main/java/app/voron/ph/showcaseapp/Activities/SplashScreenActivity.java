package app.voron.ph.showcaseapp.Activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.Utilities.AnimationHelper;

public class SplashScreenActivity extends AppCompatActivity {
    //
    private RelativeLayout layout1 = null;
    private ImageView image1 = null;
    private ImageView image2 = null;
    private ImageView image3 = null;
    private ImageView image4 = null;
    private TextView text1 = null;
    //
    private void beginAnimation(){
        int duration = getResources().getInteger(R.integer.anim_splash_screen_duration);
        int delay = 0;
        AnimationHelper.animateViewFlyIn(image1, duration, delay);
        delay+= duration;
        AnimationHelper.animateViewFlyIn(image2, duration, delay);
        delay+= duration;
        AnimationHelper.animateViewFlyIn(image3, duration, delay);
        delay+= duration;
        AnimationHelper.animateViewFlyIn(image4, duration, delay);
        delay+= duration;
        AnimationHelper.animateViewFadeIn(text1, duration, delay);
        delay+= duration;
        //
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMainWindow();
            }
        }, delay);
    }
    //
    private void showMainWindow(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //
        layout1 = (RelativeLayout) findViewById(R.id.layout_1);
        image1 = (ImageView) findViewById(R.id.image_1);
        image2 = (ImageView) findViewById(R.id.image_2);
        image3 = (ImageView) findViewById(R.id.image_3);
        image4 = (ImageView) findViewById(R.id.image_4);
        text1 = (TextView) findViewById(R.id.text_1);
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) beginAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

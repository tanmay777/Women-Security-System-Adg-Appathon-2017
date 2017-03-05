package com.example.tanmay.womensecurity.Entity.SplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.example.tanmay.womensecurity.Entity.IntroSlider.IntroSliderActivity;
import com.example.tanmay.womensecurity.R;

public class SplashScreenActivity extends Activity {

    private final int SPLASH_DISPLAY_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    public void onStart()
    {
    super.onStart();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this,IntroSliderActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_DURATION);
    }
}
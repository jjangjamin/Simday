package com.example.q.simday;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class HomeActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageView kylie = (ImageView) findViewById(R.id.kylie);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(kylie);
        Glide.with(this).load(R.drawable.splashlogo).into(gifImage);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent mainIntent = new Intent(HomeActivity.this, LoadingActivity.class);
                startActivity(mainIntent);
                finish();


                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        },SPLASH_TIME_OUT);

    }
}

package com.example.q.simday.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.q.simday.R;

public class LoadingActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;
    String master = "ㅠㅠㅠㅠㅠㅠ";
    Intent master2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView box = (ImageView) findViewById(R.id.rotatebox);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(box);
        Glide.with(LoadingActivity.this).load(R.drawable.loading3).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gifImage);

        TextView loadingtext = (TextView) findViewById(R.id.loadingtext);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(60);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        loadingtext.startAnimation(anim);

        //master2=getIntent();
        //master=master2.getStringExtra("master");
        //Log.i("master","@@@sssssssss@@@"+master);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent mainIntent = new Intent(LoadingActivity.this, MainActivity.class);
                mainIntent.putExtra("master",master);
                Log.i("master","0eeeeeeee000"+master);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        },SPLASH_TIME_OUT);


    }
}

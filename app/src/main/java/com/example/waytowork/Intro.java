package com.example.waytowork;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Intro extends AppCompatActivity {
    ImageView iv;
    AnimationDrawable ad;
    Animation an;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        Handler hander = new Handler();

        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intro.this,Login.class);
                startActivity(intent);
                finish();

            }
        },4200);
        iv = (ImageView) findViewById(R.id.image); //이미지 뷰에 에니메이션 xml 을 연결
        iv.setBackgroundResource(R.drawable.anim); //xml로만든 anim을 넣어서
        ad = (AnimationDrawable)iv.getBackground() ; //갯 백그라운드를 AnimationDrawable로 캐스팅해온다
        ad.setOneShot(false); // 한번만 할꺼인가  한번만은 true 여러번은 false
        ad.start();


        iv = (ImageView) findViewById(R.id.image2);
        iv.setBackgroundResource(R.drawable.anim2); //xml로만든 anim을 넣어서
        ad = (AnimationDrawable)iv.getBackground();
        an = new AlphaAnimation(0,1);  //진하기
        an.setDuration(2000);//지속시간
        an.setRepeatCount(0); //몇번할꺼니 4번
        iv.startAnimation(an);

        iv = (ImageView) findViewById(R.id.image3);
        iv.setBackgroundResource(R.drawable.anim3);
        ad = (AnimationDrawable)iv.getBackground();
        an = new AlphaAnimation(0,1);
        an.setDuration(4000);
        an.setRepeatCount(0);
        iv.startAnimation(an);

        iv = (ImageView) findViewById(R.id.image4);
        iv.setBackgroundResource(R.drawable.anim4);
        ad = (AnimationDrawable)iv.getBackground();
        an = new AlphaAnimation(0,1);
        an.setDuration(6000);
        an.setRepeatCount(0);
        iv.startAnimation(an);


    }
}

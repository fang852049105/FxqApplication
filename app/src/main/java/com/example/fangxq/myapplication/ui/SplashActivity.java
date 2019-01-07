package com.example.fangxq.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.fangxq.myapplication.R;

import fxq.android.com.commonbusiness.customview.parallax.ParallxContainer;


/**
 * @author huiguo
 * @date 2019/1/7
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        ParallxContainer container = (ParallxContainer) findViewById(R.id.parallax_container);
        container.setUp(getSupportFragmentManager(),
                new int[]{
                        R.layout.view_intro_1,
                        R.layout.view_intro_2,
                        R.layout.view_intro_3,
                        R.layout.view_intro_4,
                        R.layout.view_intro_5,
                        R.layout.view_login
                }
        );

        //设置动画
        ImageView iv_man = (ImageView) findViewById(R.id.iv_man);
        iv_man.setBackgroundResource(R.drawable.man_run);
        container.setIv_man(iv_man);

        findViewById(R.id.iv_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                SplashActivity.this.finish();
            }
        });
    }
}

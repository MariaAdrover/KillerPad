package com.example.killerpad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView titulo = (TextView) findViewById(R.id.splash_text);

        //carga la animacion
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.alpha_rotate_scale);
        titulo.startAnimation(rotate);

        //al acabar la animacion...
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, MenuActivity.class));
                SplashActivity.this.finish();   //para no volver a la slpash
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.

        // Hide action bar, la barra de status (hora, notificacinoes, etc)
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();
        getSupportActionBar().hide();
    }
}

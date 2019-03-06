package com.example.killerpad;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        //imageview del logo
        ImageView titulo = (ImageView) findViewById(R.id.splash_text);
        //imageview del fondo
        ImageView back = (ImageView) findViewById(R.id.spacebackground);

        //animacion background, creamos el objectanimator y le pasamos la view a animar y su propiedad
        ObjectAnimator scroll = ObjectAnimator.ofInt(back,"scrollX",
                (back.getDrawable().getIntrinsicWidth() - Resources.getSystem().getDisplayMetrics().widthPixels));
        //le indicamos que el scroll sera el ancho de toda la imagen menos el tamaño de la pantalla.
        scroll.setDuration(4000); //durara 4 segundos
        scroll.start();  //la iniciamos

        //carga la animacion  del logo
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.alpha_rotate_scale);
        titulo.startAnimation(rotate);

        //añadimos los listeners...
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //listener cuando acabe la animación.
            @Override
            public void onAnimationEnd(Animation animation) {
                //intent para ir a menu activity
                startActivity(new Intent(SplashActivity.this, MenuActivity.class));
                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(500);   //el móvil vibra medio segundo
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

        //Oculta la barra de notificaciones.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Oculta la barra con el nombre de la aplicación.
        getSupportActionBar().hide();
    }
}

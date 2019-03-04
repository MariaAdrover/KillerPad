package com.example.killerpad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class ShipView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private SurfaceHolder holder = null;

    private float centerX;
    private float centerY;
    private float baseRadius;
    private String sColor;
    Canvas myCanvas;

    public ShipView(Context context) {
        super(context);
    }

    public ShipView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SharedPreferences pref = getContext().getSharedPreferences("savedPrefs", Context.MODE_PRIVATE);
        this.sColor = pref.getString("color", "abcdef");

        if(holder==null) {
            holder = getHolder();
            holder.addCallback(this);
        }
        setOnTouchListener(this);
    }

    public ShipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void updateColor(String newColor){
        this.sColor = newColor;
        drawCanvas(centerX,centerY);
    }


    public void setupDimensions(){
        centerX = getWidth() / 2;           //centro del ancho donde está contenido
        centerY = getHeight() / 2;          //centro del alto donde está contenido
        baseRadius = Math.min(getWidth(), getHeight()) / 3.5f;
        //agarra el más pequeño: el ancho o el alto y lo divide entre 3.5 para el radio base
        //agarra el más pequeño: el ancho o el alto y lo divide entre 5.5 para el radio del pad

        /*con estas proporciones (dividido entre 3.5 y 5.5, el pad nunca se saldrá de su view
        cuando esta siendo pulsado ttodo lo lejos posible*/

    }


    private void drawCanvas(float newX, float newY) {

        //Esta condición mira si el objeto SurfaceView ha sido creado en pantalla.
        if (holder.getSurface().isValid()) {

            //almacena el canvas en variable
            this.myCanvas = this.getHolder().lockCanvas();

            //crea nuevo objeto de tipo paint para pintar
            Paint colors = new Paint();

            //pinta ttodo el canvas de color transparente para en modo clear para limpiarlo
            this.myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            //elegimos color
            colors.setColor(Color.parseColor("#" + this.sColor));

            //pintamos en el canvas un circulo con las coordenadas, el radio y el color escogido
            this.myCanvas.drawCircle(newX, newY, baseRadius, colors);

            //le pasamos la variable canvas para que actualice el canvas del surface.
            getHolder().unlockCanvasAndPost(myCanvas);
            //   }
        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupDimensions();
        drawCanvas(centerX,centerY);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(event.getAction() != event.ACTION_UP){

            drawCanvas(event.getX(),event.getY());
        }else{
            drawCanvas(centerX,centerY);
        }

        return true;
    }
}

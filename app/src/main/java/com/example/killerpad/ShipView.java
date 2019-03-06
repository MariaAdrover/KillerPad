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

        //cargamos de las sharedpreferences el color en hexadecimal y lo guardamos en el atributo "sColor"
        //su valor por defecto es "fffff" (color blanco)
        SharedPreferences pref = getContext().getSharedPreferences("savedPrefs", Context.MODE_PRIVATE);
        this.sColor = pref.getString("color", "ffffff");


        //si ya tiene "holder" (SurfaceHolder) le añadimos el que tiene la clase.
        if(holder==null) {
            holder = getHolder();
            holder.addCallback(this);
        }
        //le añadimos el listener
        setOnTouchListener(this);
    }

    public ShipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void updateColor(String newColor){
        //cambia el color con el que se pintara la nave y actualiza el canvas
        this.sColor = newColor;
        drawCanvas(centerX,centerY);
    }


    public void setupDimensions(){
        //establece las dimensiones que tendrá la nave y posición central

        centerX = getWidth() / 2;           //centro del ancho donde está contenido
        centerY = getHeight() / 2;          //centro del alto donde está contenido
        baseRadius = Math.min(getWidth(), getHeight()) / 3.5f;
        //agarra el más pequeño: el ancho o el alto y lo divide entre 3.5 para el radio base
        //agarra el más pequeño: el ancho o el alto y lo divide entre 5.5 para el radio del pad

        /*con estas proporciones (dividido entre 3.5 y 5.5, el pad nunca se saldrá de su view
        cuando esta siendo pulsado ttodo lo lejos posible*/

    }


    private void drawCanvas(float newX, float newY) {
        // pinta la nave con las coordenadas pasadas por parametros

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
        //al crearse la Shipview
        //establecemos las dimensiones
        setupDimensions();
        //actualizamos el canvas
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
        //al tocar la ShipView

        //si se esta tocando
        if(event.getAction() != event.ACTION_UP){
            //se pinta con las coordenadas del dedo
            drawCanvas(event.getX(),event.getY());
        }else{
            //en caso contrario
            //se pinta en el centro

            drawCanvas(centerX,centerY);
        }

        return true;
    }
}

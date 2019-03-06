package com.example.killerpad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener{



    private float centerX;  //coordenada x del que queremos que sea el centro del pad
    private float centerY;  //coordenada y del que queremos que sea el centro del pad
    private float baseRadius; //radio del circulo base
    private float topRadius;  //radio del circulo pequeño

    private float x; //coordenada X del pad
    private float y; //coordenada Y del pad

    private String direction = "idle";

    private JoystickListener callback;

    //constructor básico, para instanciar en código
    public JoystickView(Context context) {
        super(context);
        setZOrderOnTop(true); //pone este view encima de donde esta contenido en lugar de hacer un "agujero"
        getHolder().setFormat(PixelFormat.TRANSLUCENT); //le da formato para que lo transparente del canvas sea transparente de verdad
        getHolder().addCallback(this);
        /*añadira este objeto como callback ya que tiene la interfaz callback de surfaceholder.
         los metodos de surfaceholder saltaran gracias al callback.
         Por ahora, de estos métodos sólo utilizaremos el de SurfaceCreated.
         */
        setOnTouchListener(this);   //le añadimos el ontouchlistener

        if(context instanceof JoystickListener) {   //si el context implementa nuestra interfaz JoystickListener

            callback = (JoystickListener) context; //lo almacenamos en una variable llamada callback.

            /*De esta manera, como implementa la interfaz, cada vez que invoquemos a los métodos
            se le notificará al context como si se tratara de un listener.
            De manera que, por ejemplo, cuando un método de la interfaz es llamado aquí, el cuerpo del
            mismo método con los parámetros que le hemos pasado se ejecutará en la clase que contenga el context
            como si se tratase de un evento.
             */
        }
    }

    //constructor que llama el layout si en lugar de crearse en una clase, se crea definiendolo en el mismo.
    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener) {

            callback = (JoystickListener) context;
        }
    }
    //este constructor ni idea pero por si acaso lo he dejado igual que los otros.
    // jej ok       --Pau

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener) {

            callback = (JoystickListener) context;
        }
    }


    public void setupDimensions(){
        centerX = getWidth() / 2;           //centro del ancho donde está contenido
        centerY = getHeight() / 2;          //centro del alto donde está contenido
        baseRadius = Math.min(getWidth(), getHeight()) / 3.5f;
        //agarra el más pequeño: el ancho o el alto y lo divide entre 3.5 para el radio base
        topRadius = Math.min(getWidth(), getHeight()) / 5.5f;
        //agarra el más pequeño: el ancho o el alto y lo divide entre 5.5 para el radio del pad

        /*con estas proporciones (dividido entre 3.5 y 5.5, el pad nunca se saldrá de su view
        cuando esta siendo pulsado ttodo lo lejos posible*/

    }

    private void drawJoystick(float newX, float newY){

        //Esta condición mira si el objeto SurfaceView ha sido creado en pantalla.
        if(getHolder().getSurface().isValid()) {
            //almacena el canvas en variable
            Canvas myCanvas = this.getHolder().lockCanvas();
            //crea nuevo objeto de tipo paint para pintar
            Paint colors = new Paint();

            //pinta ttodo el canvas de color transparente para en modo clear para limpiarlo

            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            //elegimos color
            colors.setARGB(100, 255, 255, 255);
            //pintamos en el canvas un circulo con las coordenadas, el radio y el color escogido
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setARGB(255, 150, 150, 150);
            myCanvas.drawCircle(newX, newY, topRadius, colors);
            colors.setARGB(255, 255, 255, 255);
            myCanvas.drawCircle(newX, newY, topRadius-(topRadius/10), colors);
            //le pasamos la variable canvas para que actualice el canvas del surface.
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {  //cuando se crea el surfaceview por primera vez
        setupDimensions();
        drawJoystick(centerX,centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {   //este método no se usa

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {    //este método no se usa

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        /*el desplazamiento es el teorema de pitagoras tomando como catetos las diferencias
          entre las coordenadas X,Y del centro del pad y las coordenadas X,Y del dedo
          básicamente está calculando el valor del vector unitario utilizando otros dos vectores
          que representan sus componentes X e Y.
        */
        float displacement = (float) Math.sqrt(Math.pow(event.getX() - centerX, 2) + Math.pow(event.getY() - centerY, 2));

        //si el movimiento no es levantar el dedo de la pantalla
            if (event.getAction() != event.ACTION_UP) {
                //y si el desplazamiento es menor al radio del circulo que es la base del joystick
                if(displacement < baseRadius) {
                    x = event.getX();   //la coordenada x es la misma que la del dedo
                    y = event.getY();   //la coordenada y es la misma que la del dedo


                }else{ //si por el contrario, el desplazamiento es mayor al radio del circulo base...
                    /*
                    Para esto hayaremos la proporción que hay entre el radio y el desplazamiento
                    Conociendo esta proporción  y la distancia total de un centro a una coordenada del dedo
                    Si multiplicamos ambos el resultado será una coordenada restringida entre el centro
                    de ese componente(X o Y) y la componente(X o Y) del circulo base.
                    Lo que estamos haciendo es hacer la coordenada proporcional a esa distancia.

                    A continuacion calcula esa proporción.
                    Y declara que cada componente, x e y: (recordemos  el orden de las operaciones)
                    1. hace la operación del paréntesis, calcula la distancia entre el dedo y el centro del pad
                    2. multiplica esta distancia por la proporción y obtiene  la distancia "restringida"
                    3. suma esta distancia al centro del componente correspondiente.

                    Por ejemplo, teniendo:
                     baseRadius = 10
                     displacement = 28.2
                     eX = 20
                     centerX = 5

                     entonces:
                     proportion = 10/28.2 =0.35
                     x = 5 + (20-5) * 0.35
                     x = 5 + (15) * 0.35
                     x = 5 + 5.25

                     si el centro está en la coordenada 5 y el dedo está en la coordenada 20
                     quiere decir que la distancia entre ellos es de 15, pero y la distancia
                     entre el centro y donde queremos restringir nuestro pad? (en el limite del pad base)
                     pues multiplicamos esta distancia, 15 por esa proporción y nos da 5.25,
                     sin embargo está no es su posición absoluta, sino relativa con respecto al centroX
                     para obtener su absoluta la sumaremos al centroX

                     */
                    float proportion = baseRadius / displacement;
                    x = centerX + (event.getX() - centerX) * proportion;
                    y = centerY + (event.getY() - centerY) * proportion;

                }
                /*Ahora calculamos el porcentaje de desplazamiento en los dos componentes (x,y)
                  suponiendo que el radio de la base es el 100%.
                  Calculamos la diferencia entre el centro y la componente y dividimos para sacar el porcentaje
                  por ejemplo, si el centroX es la coordenada 5, x es la coordenada 15 y la base mide 20
                  (10-5)/20 = 0.5 <- el componente X del joystick se encuentra justo a la mitad.
                  Realizaremos el calculo tanto para sacar el porcentaje de X como de Y.
                */
                float percentX = (x-centerX)/baseRadius;
                float percentY = (centerY-y)/baseRadius;
                //el calculo del porcentaje Y está invertido para que sea positivo hacia arriba

                //si la dirección ha cambiado, lo notifica.
                if(!direction.equals(checkDirection(percentX,percentY))){
                    direction = checkDirection(percentX,percentY);
                    callback.directionChanged(direction);
                }
                callback.onJoystickMoved(percentX,percentY,getId());

        } else {
            x = centerX;
            y = centerY;
            callback.onJoystickMoved(0,0,1);
                if(!direction.equals(checkDirection(0,0))){
                    direction = checkDirection(0,0);
                    callback.directionChanged(direction);
                }
        }
        drawJoystick(x, y);
        return true;
    }

    public String checkDirection(float percentX,float percentY) {   //los rangos de dirección
        float var = 0.4f;   //variable de variación para el rango.
        String dir = null;
        if (percentX > var &&  percentY < var && percentY > -var) {
            dir =  "right";
        } else if (percentX > var && percentY > var) {
            dir = "upright";
        }else if(percentX > var && percentY < -var){
            dir = "downright";
        } else if (percentX < -var && percentY < var && percentY > -var) {
            dir =  "left";
        } else if (percentX < -var && percentY > var) {
            dir =  "upleft";
        }else if(percentX < -var && percentY < -var){
            dir = "downleft";
        }else if (percentX < var &&  percentX > -var && percentY > var){
            dir = "up";
        }else if (percentX < var &&  percentX > -var && percentY < -var){
            dir = "down";
        }else{
            dir="idle";
        }
        return dir;
    }



    //interfaz para notificar los movimientos
    public interface JoystickListener {

        //este método indicará los porcentajes X e Y además de la ID del joystick. (por si queremos más)
        void onJoystickMoved(float xPercent, float yPercent, int source);
        //este método simplemente indica la dirección
        void directionChanged(String direction);

    }
}

package es.unizar.eina.products.Productos.send;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/** Concrete implementor utilizando aplicacion por defecto de Android para gestionar mail. No funciona en el emulador si no se ha configurado previamente el mail */
public class SMSImplementor implements SendImplementor {

    /** actividad desde la cual se abrira la actividad de gestión de correo */
    private Activity sourceActivity;

    /** Constructor
     * @param source actividad desde la cual se abrira la actividad de gestion de correo
     */
    public SMSImplementor(Activity source){
        setSourceActivity(source);
    }

    /**  Actualiza la actividad desde la cual se abrira la actividad de gestion de correo */
    public void setSourceActivity(Activity source) {
        sourceActivity = source;
    }

    /**  Recupera la actividad desde la cual se abrira la actividad de gestion de correo */
    public Activity getSourceActivity(){
        return sourceActivity;
    }

    /**
     * Implementacion del metodo send utilizando la aplicacion de gestion de correo de Android
     * Solo se copia el asunto y el cuerpo
     * @param subject asunto
     * @param price precio
     * @param weight peso
     */
    public void send (String subject, String price, String weight) {
        Intent smsIntent = new Intent (Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.putExtra("sms_body", subject + " precio: " + price + " peso: " + weight);
        getSourceActivity().startActivity(smsIntent);
    }

}

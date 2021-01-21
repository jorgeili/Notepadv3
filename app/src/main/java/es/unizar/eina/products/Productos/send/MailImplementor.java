package es.unizar.eina.products.Productos.send;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.widget.TextView;

import es.unizar.eina.products.Productos.ListaProductos.EditModel;

/** Concrete implementor utilizando aplicacion por defecto de Android para gestionar mail. No funciona en el emulador si no se ha configurado previamente el mail */
public class MailImplementor implements SendImplementor{
	
   /** actividad desde la cual se abrira la actividad de gestión de correo */
   private Activity sourceActivity;
   
   /** Constructor
    * @param source actividad desde la cual se abrira la actividad de gestion de correo
    */
   public MailImplementor(Activity source){
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
   public void send (String subject, String price, String weight, Cursor listProducts) {
       Intent emailIntent = new Intent (Intent.ACTION_SEND);
       emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
       emailIntent.setType("plain/text");
       String contenidoCorreo = "PRECIO: " + price + " PESO: " + weight + "\n";
       if (listProducts.moveToFirst()) {
           do {
               contenidoCorreo += "Producto: " + listProducts.getString(listProducts.getColumnIndex("title")) +
                       " PRECIO: " + listProducts.getString(listProducts.getColumnIndex("price")) +
                       " PESO: " + listProducts.getString(listProducts.getColumnIndex("weight")) +
                       " CANTIDAD: " + listProducts.getString(listProducts.getColumnIndex("quantity")) + "\n";

           } while (listProducts.moveToNext());
       }
       emailIntent.putExtra(Intent.EXTRA_TEXT, contenidoCorreo);
       listProducts.close();
       getSourceActivity().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
   }

}

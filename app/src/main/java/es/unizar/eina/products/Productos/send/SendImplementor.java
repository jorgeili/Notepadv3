package es.unizar.eina.products.Productos.send;

import android.app.Activity;
import android.database.Cursor;

/** 
 * Define la interfaz para las clases de la implementacion.
 * La interfaz no se tiene que corresponder directamente con la interfaz de la abstraccion.
 *  
 */
public interface SendImplementor {
	   
   /**  Actualiza la actividad desde la cual se abrira la actividad de envioo de productos */
   public void setSourceActivity(Activity source);

   /**  Recupera la actividad desde la cual se abrira la actividad de envio de productos */
   public Activity getSourceActivity();

   /** Permite lanzar la actividad encargada de gestionar el envio de productos */
   public void send(String subject, String price, String weight, Cursor listProducts);

}

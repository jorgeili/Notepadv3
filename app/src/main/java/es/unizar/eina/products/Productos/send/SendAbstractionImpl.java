package es.unizar.eina.products.Productos.send;

import android.app.Activity;
import android.database.Cursor;

/** Implementa la interfaz de la abstraccion utilizando (delegando a) una referencia a un objeto de tipo implementor  */
public class SendAbstractionImpl implements SendAbstraction {
	
	/** objeto delegado que facilita la implementacion del metodo send */
	private SendImplementor implementor;
	
	/** Constructor de la clase. Inicializa el objeto delegado
	 * @param sourceActivity actividad desde la cual se abrira la actividad encargada de enviar el producto
	 * @param method parametro potencialmente utilizable para instanciar el objeto delegado
	 */
	public SendAbstractionImpl(Activity sourceActivity, String method) {
		if(method.equalsIgnoreCase("EMAIL")){
			implementor = new MailImplementor(sourceActivity);
		}
	}

	/** Envia el correo con el asunto (subject) y cuerpo (body) que se reciben como parametros a traves de un objeto delegado
     * @param subject asunto
	 * @param price precio
	 * @param weight peso
     */
	public void send(String subject, String price, String weight, Cursor listProducts) {
		implementor.send(subject, price, weight, listProducts);
	}
}

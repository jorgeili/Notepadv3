package es.unizar.eina.products.Productos.send;

import java.lang.ref.SoftReference;

/** Define la interfaz de la abstraccion */
public interface SendAbstraction {

	/** Definicion del metodo que permite enviar la nota con el asunto (subject) y cuerpo (body) que se reciben como parametros
     * @param subject asunto
	 * @param price precio
	 * @param weight peso
     */
	public void send(String subject, String price, String weight);
}

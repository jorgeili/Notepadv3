package es.unizar.eina.products.Productos.send;

import android.database.Cursor;

import java.lang.ref.SoftReference;

/** Define la interfaz de la abstraccion */
public interface SendAbstraction {

	/** Definicion del metodo que permite enviar el producto con el asunto (subject) y cuerpo (body),
	 * ademas del peso y precio total de la lista, junto a un listado de los productos que integra
     * @param subject asunto
	 * @param price precio
	 * @param weight peso
     */
	public void send(String subject, String price, String weight, Cursor listProducts);
}

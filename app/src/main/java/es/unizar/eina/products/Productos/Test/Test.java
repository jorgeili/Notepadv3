package es.unizar.eina.products.Productos.Test;

import android.content.Context;
import android.util.Log;

import es.unizar.eina.products.Productos.BD.*;

public class Test {
    private static Context mCtx;
    private ProductsDbAdapter mDbHelper;
    private long idUnitaria;

    public Test(Context mCtx){
        this.mCtx = mCtx;
        mDbHelper = new ProductsDbAdapter(mCtx);
        mDbHelper.open();
    }
    /**
     * Crea un nuevo producto a partir del título y texto proporcionados. Si el
     * producto se crea correctamente, devuelve el nuevo rowId de este; en otro
     * caso, devuelve -1 para indicar el fallo.
     *
     * @param title
     * el título del producto;
     * title != null y title.length() > 0
     * @param weight
     * el título del producto;
     * title != null y title.length() > 0     *
     * @param price
     * el título del producto;
     * title != null y title.length() > 0
     * @param body
     * el texto del producto;
     * body != null
     * @return rowId del nuevo producto o -1 si no se ha podido crear
     */
    public long createProduct(String title, Double weight, Double price, String body){
        try {
            long rowId;
            rowId = mDbHelper.createProduct(title, weight, price, body);
            if (rowId <= 0) {
                Log.d("TEST crear producto: ", "incorrecto");
            } else {
                Log.d("TEST crear producto: ", "correcto");
            }
            return rowId;
        } catch(Throwable th){
            Log.d("TEST crear producto: ","se ha producido un error.");
        }
        return -1;
    }

    private boolean createProductCasosPrueba(){
        idUnitaria = createProduct("t1",1.0,1.0,"");
        if (idUnitaria <= 0) {
            return false;
        }
        long id = createProduct(null,1.0,1.0,"");
        if (id !=- 1) {
            return false;
        }
        id = createProduct("t1",null,1.0,"");
        if (id != -1) {
            return false;
        }
        id = createProduct("t1",1.0,null,"");
        if (id != -1) {
            return false;
        }
        return true;
    }


    /**
     * Borra el producto cuyo rowId se ha pasado como parámetro
     *
     * @param rowId
     * el identificador del producto que se desea borrar ;
     * rowId > 0
     * @return true si y solo si la producto se ha borrado
     */
    public boolean deleteProduct(long rowId){
        try {
            boolean deleted;
            deleted = mDbHelper.deleteProduct(rowId);
            Log.d("testDeleteProduct", String.valueOf(rowId));
            if (deleted) {
                Log.d("TEST: eliminar producto", "correcto");
            } else {
                Log.d("TEST: eliminar producto", "incorrecto");
            }
            return deleted;
        } catch(Throwable th){
            Log.d("TEST eliminar producto:","se ha producido un error.");
        }
        return false;
    }

    private boolean deleteProductCasosPrueba(){
        return deleteProduct(idUnitaria) && !deleteProduct(-1);
    }


    /**
     * Actualiza un producto a partir de los valores de los parámetros. El producto que
     * se actualizará es aquella cuyo rowId coincida con el valor del parámetro.
     * Su título, peso, precio y texto se modificarán con los valores de title, weight, price, body,
     * respectivamente.
     *
     * @param rowId
     * el identificador del producto que se desea borrar;
     * rowId > 0
     * @param title
     * el título del producto;
     * title != null y title.length() > 0
     * @param weight
     * el título del producto;
     * title != null y title.length() > 0     *
     * @param price
     * el título del producto;
     * title != null y title.length() > 0
     * @param body
     * el texto del producto;
     * body != null
     * @return true si y solo si el producto se actualizó correctamente
     */
    public boolean updateProduct (long rowId, String title, Double weight, Double price, String body){
        try {
            boolean updated;
            updated = mDbHelper.updateProduct(rowId, title, weight , price, body);
            if( updated ){
                Log.d("TEST: update producto", "incorrecto");
            }
            else{
                Log.d("TEST: update producto", "correcto");
            }
            return updated;
        } catch(Throwable th){
            Log.d("TEST update producto: ","se ha producido un error.");
        }
        return false;
    }

    private boolean updateProductCasosPrueba(){
        boolean correcto = true;
        correcto&=updateProduct(idUnitaria,"t1",1.0,1.0,"");
        correcto&=!updateProduct(idUnitaria,null,1.0,1.0,"");
        correcto&=!updateProduct(idUnitaria,"t1",null,1.0,"");
        correcto&=!updateProduct(idUnitaria,"t1",1.0,null,"");
        correcto&=!updateProduct(-1,"t1",1.0,1.0,"");
        return correcto;
    }


    public boolean pruebasUnitarias(){
        boolean correcto = true;
        if(createProductCasosPrueba()) {
            Log.d("TESTUNIT createProduct:","correcto");
        }
        else {
            correcto=false;
            Log.d("TESTUNIT createProduct:","incorrecto");
        }

        if(updateProductCasosPrueba()) {
            Log.d("TESTUNIT updateProduct:","correcto");
        }
        else {
            correcto=false;
            Log.d("TESTUNIT updateProduct:","incorrecto");
        }

        if(deleteProductCasosPrueba()) {
            Log.d("TESTUNIT deleteProduct:","correcto");
        }
        else {
            correcto=false;
            Log.d("TESTUNIT deleteProduct:","incorrecto");
        }
        return correcto;
    }

    /**
     * PRUEBA DE VOLUMEN DE CREACIÓN DE 1000 PRODUCTOS
     */
    public boolean testVolumen(){
        long rowId;
        boolean fallo = false;
        for(int i = 1; i <= 1000 ; i++) {
            rowId = mDbHelper.createProduct("PRODUCTO" + i,0.0,0.0,"PRUEBA: CREAR UN PRODUCTO");
            if (rowId < 0 && !fallo){
                fallo = true;
                break;
            }
        }
        if (fallo){
            Log.d("TEST: prueba de volumen", "incorrecto");
        }
        else{
            Log.d("TEST: prueba de volumen", "correcto");
        }
        return !fallo;
    }

    /**
     * PRUEBA DE SOBRECARGA INTRODUCIENDO UN GRAN VOLUMEN DE DATOS
     */
    public void testSobrecarga (){
        String body = "";
        for (int i = 1; i <= 100000; i++){
            mDbHelper.createProduct("TestSobrecarga" + i, 0.0,0.0,body);
            body = body + "Sobrecarga: " + i + " ";
            Log.d("Almacenada con éxito: ", "Producto numero: " + String.valueOf(i) + "con cuerpo" + String.valueOf(body.length()));
        }
    }
}

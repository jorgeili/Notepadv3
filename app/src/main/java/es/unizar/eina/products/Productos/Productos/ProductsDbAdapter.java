package es.unizar.eina.products.Productos.Productos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.database.DatabaseUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.products.Productos.ListaProductos.EditModel;

/**
 * Simple products database access helper class. Defines the basic CRUD operations
 * for the products list example, and gives the ability to list all products as well as
 * retrieve or modify a specific product.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class ProductsDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_PRICE = "price";
    public static final String KEY_BODY = "body";

    public static final String KEY_ROWID_SL = "_id";
    public static final String KEY_TITLE_SL = "title";
    public static final String KEY_WEIGHT_SL = "weight";
    public static final String KEY_PRICE_SL = "price";

    public static final String KEY_ROWID_SL_ADD = "_idSL";
    public static final String KEY_ROWID_P_ADD = "_idP";
    public static final String KEY_QUANTITY = "quantity";

    public static final String KEY_TOTAL_WEIGHT = "total_weight";



    private static final String TAG = "ProductsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE_P =
            "create table products (_id integer primary key autoincrement, "
                    + "title text not null, weight double not null, price double not null, body text not null);";

    private static final String DATABASE_CREATE_SL =
            "create table shoppingLists (_id integer primary key autoincrement, "
                    + "title text not null, weight double not null, price double not null);";

    private static final String DATABASE_CREATE_ADD_PRODUCT =
            "create table shoppingListsProducts ( _idSL integer, _idP integer, quantity integer, primary key(_idSL, _idP));";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_P = "products";
    private static final String DATABASE_TABLE_SL = "shoppingLists";
    private static final String DATABASE_TABLE_ADD_PRODUCT = "shoppingListsProducts";

    private static final int DATABASE_VERSION = 25;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_P);
            db.execSQL(DATABASE_CREATE_SL);
            db.execSQL(DATABASE_CREATE_ADD_PRODUCT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_P);
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_SL);
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_ADD_PRODUCT);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public ProductsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the products database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public ProductsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new product using the title, weight, price and body provided. If the product is
     * successfully created return the new rowId for that product, otherwise return
     * a -1 to indicate failure.
     *
     * @param title the title of the product
     * @param weight the weight of the product
     * @param price the price of the product
     * @param body the description of the product
     * @return rowId or -1 if failed
     */
    public long createProduct(String title, Double weight, Double price, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_WEIGHT, weight);
        initialValues.put(KEY_PRICE, price);
        initialValues.put(KEY_BODY, body);

        return mDb.insert(DATABASE_TABLE_P, null, initialValues);
    }

    public long insertProductOnSL(long idSL, long idP) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID_SL_ADD, idSL);
        initialValues.put(KEY_ROWID_P_ADD, idP);
        initialValues.put(KEY_QUANTITY, 1);

        long insert = 0;
        String comprobar = "SELECT count(*) as counter FROM shoppingListsProducts WHERE _idP='"+idP+"' AND _idSL='"+idSL+"'";
        Cursor cursorComprobacion = mDb.rawQuery(comprobar, null);
        if (cursorComprobacion.moveToFirst()){
            if (Integer.parseInt(cursorComprobacion.getString(cursorComprobacion.getColumnIndex("counter")))==0){
                insert = mDb.insert(DATABASE_TABLE_ADD_PRODUCT, null, initialValues);

                String selectQuery = "SELECT weight, price FROM shoppingLists WHERE _id = '" + idSL +"'";
                Cursor cursor = mDb.rawQuery(selectQuery, null);
                Double we_sl = 0.0, pr_sl = 0.0, we = 0.0, pr = 0.0;
                if (cursor.moveToFirst()) {
                    we_sl = Double.parseDouble(cursor.getString(cursor.getColumnIndex("weight")));
                    pr_sl = Double.parseDouble(cursor.getString(cursor.getColumnIndex("price")));
                }
                cursor.close();

                selectQuery = "SELECT weight, price FROM products WHERE _id = '" + idP +"'";
                Cursor newCursor = mDb.rawQuery(selectQuery, null);
                if (newCursor.moveToFirst()) {
                    we = Double.parseDouble(newCursor.getString(newCursor.getColumnIndex("weight")));
                    pr = Double.parseDouble(newCursor.getString(newCursor.getColumnIndex("price")));
                }
                newCursor.close();

                ContentValues args = new ContentValues();
                args.put(KEY_WEIGHT_SL, we_sl + we );
                args.put(KEY_PRICE_SL, pr_sl + pr );
                Log.v("PESO", "El peso total es: "+ ( we_sl + we ));
                mDb.update(DATABASE_TABLE_SL, args, KEY_ROWID_SL + "=" + idSL, null);
            }
        }
        cursorComprobacion.close();
        return insert;
    }

    public void updateQuantities(ArrayList<EditModel> quantityArrayList, ArrayList<TextView> namesArrayList, String idSL){
        for (int i = 0; i<quantityArrayList.size(); i++){
            int oldQuantityValue = 0, quantity;
            String select = "SELECT _id, weight, price FROM products WHERE title = '"+namesArrayList.get(i).getText()+"'";
            Cursor cursor = mDb.rawQuery(select, null);
            if (cursor.moveToFirst()) {
                do {
                    //Update quantity
                    String idProd = cursor.getString(cursor.getColumnIndex("_id"));

                    String oldQuantitySelect = "SELECT quantity FROM shoppingListsProducts WHERE _idP='"+idProd+"' AND _idSL='"+idSL+"'";
                    Cursor oldQuantityCursor = mDb.rawQuery(oldQuantitySelect,null);
                    if (oldQuantityCursor.moveToFirst()){
                        String old = oldQuantityCursor.getString(oldQuantityCursor.getColumnIndex("quantity"));
                        oldQuantityValue = old.equals("") ? 0 : Integer.parseInt(old);
                    }
                    oldQuantityCursor.close();

                    String whereClause = "_idP = '" +idProd+"' AND _idSL = '" + idSL + "'";

                    String quantityStr = quantityArrayList.get(i).getEditTextValue();
                    quantity = quantityStr.equals("") ? 0 : Integer.parseInt(quantityStr);
                    if (quantity == 0){
                        mDb.delete(DATABASE_TABLE_ADD_PRODUCT, whereClause,null);
                    }
                    else{
                        ContentValues args = new ContentValues();
                        args.put(KEY_QUANTITY, quantityStr);
                        mDb.update(DATABASE_TABLE_ADD_PRODUCT, args, whereClause, null);
                    }

                    //Update SL's weight and price
                    Double we_sl = 0.0, pr_sl = 0.0, we, pr;
                    we = Double.parseDouble(cursor.getString(cursor.getColumnIndex("weight")));
                    pr = Double.parseDouble(cursor.getString(cursor.getColumnIndex("price")));

                    String selectQuery = "SELECT weight, price FROM shoppingLists WHERE _id = '" + idSL +"'";
                    Cursor cursorSL = mDb.rawQuery(selectQuery, null);
                    if (cursorSL.moveToFirst()) {
                        we_sl = Double.parseDouble(cursorSL.getString(cursorSL.getColumnIndex("weight")));
                        pr_sl = Double.parseDouble(cursorSL.getString(cursorSL.getColumnIndex("price")));
                    }
                    cursorSL.close();

                    ContentValues argsSL = new ContentValues();
                    if (quantity == 0){
                        argsSL.put(KEY_WEIGHT_SL, we_sl - we*oldQuantityValue );
                        argsSL.put(KEY_PRICE_SL, pr_sl - pr*oldQuantityValue );
                    }
                    else{
                        argsSL.put(KEY_WEIGHT_SL, we_sl + we*(quantity-oldQuantityValue) );
                        argsSL.put(KEY_PRICE_SL, pr_sl + pr*(quantity-oldQuantityValue) );
                    }
                    mDb.update(DATABASE_TABLE_SL, argsSL, KEY_ROWID_SL + "=" + idSL, null);

                } while (cursor.moveToNext());
            }
            cursor.close();

        }
    }

    /**
     * Return a Cursor over the list of all products in the database
     *
     * @return Cursor over all products
     */
    public Cursor fetchAllSLProducts(String rowid_SL) {
        Cursor mShoppingListsCursor=  mDb.query(DATABASE_TABLE_ADD_PRODUCT, new String[] {KEY_ROWID_P_ADD},
                KEY_ROWID_SL_ADD, null, null, null, null);

        String producto = "";
        boolean first = true;
        String selectQuery = "SELECT _id, title, weight, price, quantity" +
                " FROM (" +
                "   SELECT * FROM products INNER JOIN shoppingListsProducts ON products._id =  shoppingListsProducts._idP " +
                ") " +
                "WHERE _idSL = '" + rowid_SL +"'";
        if (mShoppingListsCursor.moveToFirst()) {
            do {
                producto = mShoppingListsCursor.getString(0);
                if(first) {
                    first = false;
                    selectQuery += " AND ( _id = '" + producto+"'";
                }else{
                    selectQuery += " OR _id = '" + producto+"'";
                }
            } while (mShoppingListsCursor.moveToNext());
            selectQuery += " ) ";
        }
        Cursor finalCursor = mDb.rawQuery(selectQuery, null);
        String resultado = DatabaseUtils.dumpCursorToString(finalCursor);

        Log.d("VOLCADOOOO1:",resultado);
        mShoppingListsCursor.close();

        return finalCursor;
    }


    public Double[] getWeightPriceSL(String rowId){
        String selectQuery = "SELECT weight, price FROM shoppingLists WHERE _id = '" + rowId +"'";
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        Double[] res = new Double[2];
        if (cursor.moveToFirst()) {
            String aux = cursor.getString(cursor.getColumnIndex("weight"));
            res[0] = Double.parseDouble(aux);
            aux = cursor.getString(cursor.getColumnIndex("price"));
            res[1] = Double.parseDouble(aux);
        }
        cursor.close();
        return res;
    }

    public int getNumProducts(String rowId){
        String queryNumProd = "SELECT count(*) as counter FROM shoppingListsProducts WHERE _idSL = '" + rowId +"'";
        Cursor cursorNum = mDb.rawQuery(queryNumProd, null);
        int numProd = 0;
        if (cursorNum.moveToFirst())
            numProd = cursorNum.getInt(cursorNum.getColumnIndex("counter"));
        return numProd;
    }

    /**
     * Create a new shopping list using the title, weight, price and body provided. If the shopping list is
     * successfully created return the new rowId for that shopping list, otherwise return
     * a -1 to indicate failure.
     *
     * @return rowId or -1 if failed
     */
    public long createShoppingList() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE_SL, "Sin nombre");
        initialValues.put(KEY_WEIGHT_SL, 0.0);
        initialValues.put(KEY_PRICE_SL, 0.0);

        return mDb.insert(DATABASE_TABLE_SL, null, initialValues);
    }

    /**
     * Delete the product with the given rowId
     *
     * @param rowId id of product to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteProduct(long rowId) {

        return mDb.delete(DATABASE_TABLE_P, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Delete the shopping list with the given rowId
     *
     * @param rowId id of shopping list to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteShoppingList(long rowId) {

        return mDb.delete(DATABASE_TABLE_SL, KEY_ROWID_SL + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all shopping lists in the database
     *
     * @return Cursor over all shopping lists
     */
    public Cursor fetchAllShoppingLists() {

        return mDb.query(DATABASE_TABLE_SL, new String[] {KEY_ROWID_SL, KEY_TITLE_SL, KEY_WEIGHT_SL, KEY_PRICE_SL
        }, null, null, null, null, null);
    }

    /**
     * Return a Cursor over the list of all shopping lists  in the database ordered by name
     *
     * @return Cursor over all shopping lists  ordered by name
     */
    public Cursor fetchAllShoppingListsByName() {

        return mDb.query(DATABASE_TABLE_SL, new String[] {KEY_ROWID_SL, KEY_TITLE_SL, KEY_WEIGHT_SL, KEY_PRICE_SL
        }, null, null, null, null, KEY_TITLE_SL);
    }

    /**
     * Return a Cursor over the list of all shopping lists  in the database ordered by price
     *
     * @return Cursor over all shopping lists  ordered by price
     */
    public Cursor fetchAllShoppingListsByPrice() {

        return mDb.query(DATABASE_TABLE_SL, new String[] {KEY_ROWID_SL, KEY_TITLE_SL, KEY_WEIGHT_SL, KEY_PRICE_SL
        }, null, null, null, null, KEY_PRICE_SL);
    }

    /**
     * Return a Cursor over the list of all shopping lists in the database ordered by weight
     *
     * @return Cursor over all shopping lists ordered by weight
     */
    public Cursor fetchAllShoppingListsByWeight() {

        return mDb.query(DATABASE_TABLE_SL, new String[] {KEY_ROWID_SL, KEY_TITLE_SL, KEY_WEIGHT_SL, KEY_PRICE_SL
        }, null, null, null, null, KEY_WEIGHT_SL);
    }

    /**
     * Return a Cursor over the list of all products in the database
     *
     * @return Cursor over all products
     */
    public Cursor fetchAllProducts() {

        return mDb.query(DATABASE_TABLE_P, new String[] {KEY_ROWID, KEY_TITLE, KEY_WEIGHT, KEY_PRICE,
                KEY_BODY}, null, null, null, null, null);
    }

    /**
     * Return a Cursor over the list of all products in the database ordered by name
     *
     * @return Cursor over all products ordered by name
     */
    public Cursor fetchAllProductsByName() {

        return mDb.query(DATABASE_TABLE_P, new String[] {KEY_ROWID, KEY_TITLE, KEY_WEIGHT, KEY_PRICE,
                KEY_BODY}, null, null, null, null, KEY_TITLE);
    }

    /**
     * Return a Cursor over the list of all products in the database ordered by price
     *
     * @return Cursor over all products ordered by price
     */
    public Cursor fetchAllProductsByPrice() {

        return mDb.query(DATABASE_TABLE_P, new String[] {KEY_ROWID, KEY_TITLE, KEY_WEIGHT, KEY_PRICE,
                KEY_BODY}, null, null, null, null, KEY_PRICE);
    }

    /**
     * Return a Cursor over the list of all products in the database ordered by weight
     *
     * @return Cursor over all products ordered by weight
     */
    public Cursor fetchAllProductsByWeight() {

        return mDb.query(DATABASE_TABLE_P, new String[] {KEY_ROWID, KEY_TITLE, KEY_WEIGHT, KEY_PRICE,
                KEY_BODY}, null, null, null, null, KEY_WEIGHT);
    }

    /**
     * Return a Cursor positioned at the product that matches the given rowId
     *
     * @param rowId id of product to retrieve
     * @return Cursor positioned to matching product, if found
     * @throws SQLException if product could not be found/retrieved
     */
    public Cursor fetchProduct(long rowId) throws SQLException {

        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_P, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_WEIGHT, KEY_PRICE, KEY_BODY},
                        KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Return a Cursor positioned at the product that matches the given rowId
     *
     * @param rowId id of product to retrieve
     * @return Cursor positioned to matching product, if found
     * @throws SQLException if product could not be found/retrieved
     */
    public Cursor fetchShoppingList(long rowId) throws SQLException {

        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_SL, new String[] {KEY_ROWID_SL,
                                KEY_TITLE_SL, KEY_WEIGHT_SL, KEY_PRICE_SL},
                        KEY_ROWID_SL + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the product using the details provided. The product to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of product to update
     * @param title value to set product title to
     * @param weight value to set product weight to
     * @param price value to set product price to
     * @param body value to set product body to
     * @return true if the product was successfully updated, false otherwise
     */
    public boolean updateProduct(long rowId, String title, Double weight, Double price, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_WEIGHT, weight);
        args.put(KEY_PRICE, price);
        args.put(KEY_BODY, body);

        return mDb.update(DATABASE_TABLE_P, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Update the shopping list using the details provided. The shopping list to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of shopping list to update
     * @param title value to set shopping list title to
     * @return true if the shopping list was successfully updated, false otherwise
     */
    public boolean updateShoppingList(long rowId, String title) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE_SL, title);

        return mDb.update(DATABASE_TABLE_SL, args, KEY_ROWID_SL + "=" + rowId, null) > 0;
    }

    /**
     * Function to load the spinner of Products data from SQLite database
     * */
    public ArrayAdapter<String> loadProducts(Spinner addProduct) {
        List<String> productos = new ArrayList<>();
        String getProducts = "SELECT title, weight, price FROM products";
        Cursor pointer = mDb.rawQuery(getProducts, null);
        Log.v("spinner","Productos recolectados " + pointer.getCount());
        if (pointer.moveToFirst()) {
            do {
                productos.add(pointer.getString(1));
            } while (pointer.moveToNext());
        }
        pointer.close();
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(mCtx, android.R.layout.simple_spinner_item, productos);

        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        addProduct.setAdapter(productAdapter);

        return productAdapter;
    }
}
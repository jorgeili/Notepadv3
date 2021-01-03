package es.unizar.eina.products.Productos.Productos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static final String KEY_BODY_SL = "body";

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
                    + "title text not null, weight double not null, price double not null, body text not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_P = "products";
    private static final String DATABASE_TABLE_SL = "shoppingLists";
    private static final int DATABASE_VERSION = 5;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_P);
            db.execSQL(DATABASE_CREATE_SL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_P);
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_SL);
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

    /**
     * Create a new shopping list using the title, weight, price and body provided. If the shopping list is
     * successfully created return the new rowId for that shopping list, otherwise return
     * a -1 to indicate failure.
     *
     * @param title the title of the shopping list
     * @param weight the weight of the shopping list
     * @param price the price of the shopping list
     * @param body the description of the shopping list
     * @return rowId or -1 if failed
     */
    public long createShoppingList(String title, Double weight, Double price, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE_SL, title);
        initialValues.put(KEY_WEIGHT_SL, weight);
        initialValues.put(KEY_PRICE_SL, price);
        initialValues.put(KEY_BODY_SL, body);

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

        return mDb.query(DATABASE_TABLE_SL, new String[] {KEY_ROWID_SL, KEY_TITLE_SL, KEY_WEIGHT_SL, KEY_PRICE_SL,
                KEY_BODY_SL}, null, null, null, null, null);
    }

    /**
     * Return a Cursor over the list of all shopping lists  in the database ordered by name
     *
     * @return Cursor over all shopping lists  ordered by name
     */
    public Cursor fetchAllShoppingListsByName() {

        return mDb.query(DATABASE_TABLE_SL, new String[] {KEY_ROWID_SL, KEY_TITLE_SL, KEY_WEIGHT_SL, KEY_PRICE_SL,
                KEY_BODY_SL}, null, null, null, null, KEY_TITLE_SL);
    }

    /**
     * Return a Cursor over the list of all shopping lists  in the database ordered by price
     *
     * @return Cursor over all shopping lists  ordered by price
     */
    public Cursor fetchAllShoppingListsByPrice() {

        return mDb.query(DATABASE_TABLE_SL, new String[] {KEY_ROWID_SL, KEY_TITLE_SL, KEY_WEIGHT_SL, KEY_PRICE_SL,
                KEY_BODY_SL}, null, null, null, null, KEY_PRICE_SL);
    }

    /**
     * Return a Cursor over the list of all shopping lists in the database ordered by weight
     *
     * @return Cursor over all shopping lists ordered by weight
     */
    public Cursor fetchAllShoppingListsByWeight() {

        return mDb.query(DATABASE_TABLE_SL, new String[] {KEY_ROWID_SL, KEY_TITLE_SL, KEY_WEIGHT_SL, KEY_PRICE_SL,
                KEY_BODY_SL}, null, null, null, null, KEY_WEIGHT_SL);
    }

    /**
     * Return a Cursor over the list of all shopping lists  in the database
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
     * @param weight value to set shopping list weight to
     * @param price value to set shopping list price to
     * @param body value to set shopping list body to
     * @return true if the shopping list was successfully updated, false otherwise
     */
    public boolean updateShoppingList(long rowId, String title, Double weight, Double price, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE_SL, title);
        args.put(KEY_WEIGHT_SL, weight);
        args.put(KEY_PRICE_SL, price);
        args.put(KEY_BODY_SL, body);

        return mDb.update(DATABASE_TABLE_SL, args, KEY_ROWID_SL + "=" + rowId, null) > 0;
    }
}
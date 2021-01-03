package es.unizar.eina.products.Productos.Productos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import es.unizar.eina.products.R;
import es.unizar.eina.products.Productos.ListaProductos.*;


public class Products extends AppCompatActivity {

    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
    private static final int ORDER_NAME = Menu.FIRST + 3;
    private static final int ORDER_PRICE= Menu.FIRST + 4;
    private static final int ORDER_WEIGHT = Menu.FIRST + 5;
    private static final int SHOW_SHOPPING_LISTS= Menu.FIRST + 7;


    private ProductsDbAdapter mDbHelper;
    private Cursor mNotesCursor;
    private ListView mList;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_list);

        mDbHelper = new ProductsDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.products_list);
        fillData(0);

        registerForContextMenu(mList);

    }

    private void fillData(int orderCriteria) {
        if(orderCriteria == 0) {
            // Get all of the notes from the database and create the item list
            mNotesCursor = mDbHelper.fetchAllProducts();
            startManagingCursor(mNotesCursor);
        } else if (orderCriteria == 1) {
            // Get all of the notes ordered by name from the database and create the item list
            mNotesCursor = mDbHelper.fetchAllProductsByName();
            startManagingCursor(mNotesCursor);
        }else if (orderCriteria == 2) {
            // Get all of the notes ordered by price from the database and create the item list
            mNotesCursor = mDbHelper.fetchAllProductsByPrice();
            startManagingCursor(mNotesCursor);
        }else if (orderCriteria == 3) {
            // Get all of the notes ordered by weight from the database and create the item list
            mNotesCursor = mDbHelper.fetchAllProductsByWeight();
            startManagingCursor(mNotesCursor);
        }
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { ProductsDbAdapter.KEY_TITLE };

        // and an array of the fields we want to bind those fields to (in this case just nameProd)
        int[] to = new int[] { R.id.nameProd };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter products =
                new SimpleCursorAdapter(this, R.layout.row, mNotesCursor, from, to);
        mList.setAdapter(products);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.menu_insert_prod);
        menu.add(Menu.NONE, ORDER_NAME, Menu.NONE, R.string.menu_order_prod_name);
        menu.add(Menu.NONE, ORDER_PRICE, Menu.NONE, R.string.menu_order_prod_price);
        menu.add(Menu.NONE, ORDER_WEIGHT, Menu.NONE, R.string.menu_order_prod_weight);
        menu.add(Menu.NONE, SHOW_SHOPPING_LISTS, Menu.NONE, R.string.menu_shopping_lists);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createProduct();
                return true;
            case ORDER_NAME:
                fillData(1);
                return true;
            case ORDER_PRICE:
                fillData(2);
                return true;
            case ORDER_WEIGHT:
                fillData(3);
                return true;
            case SHOW_SHOPPING_LISTS:
                Intent showShoppingLists = new Intent(this,ShoppingList.class);
                startActivity(showShoppingLists);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_delete);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.edit_product);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteProduct(info.id);
                fillData(0);
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editProduct(info.position, info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createProduct() {
        Intent i = new Intent(this, ProductEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }


    protected void editProduct(int position, long id) {
        Cursor c = mNotesCursor;
        c.moveToPosition(position);
        Intent i = new Intent(this, ProductEdit.class);
        i.putExtra(ProductsDbAdapter.KEY_ROWID, id);
        i.putExtra(ProductsDbAdapter.KEY_TITLE, c.getString(
                c.getColumnIndexOrThrow(ProductsDbAdapter.KEY_TITLE)));
        i.putExtra(ProductsDbAdapter.KEY_WEIGHT, c.getDouble(
                c.getColumnIndexOrThrow(ProductsDbAdapter.KEY_WEIGHT)));
        i.putExtra(ProductsDbAdapter.KEY_PRICE, c.getDouble(
                c.getColumnIndexOrThrow(ProductsDbAdapter.KEY_PRICE)));
        i.putExtra(ProductsDbAdapter.KEY_BODY, c.getString(
                c.getColumnIndexOrThrow(ProductsDbAdapter.KEY_BODY)));
        startActivityForResult(i, ACTIVITY_EDIT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        switch(requestCode) {
            case ACTIVITY_CREATE:
                String title = extras.getString(ProductsDbAdapter.KEY_TITLE);
                Double weight = extras.getDouble(ProductsDbAdapter.KEY_WEIGHT);
                Double price = extras.getDouble(ProductsDbAdapter.KEY_PRICE);
                String body = extras.getString(ProductsDbAdapter.KEY_BODY);
                mDbHelper.createProduct(title, weight, price, body);
                fillData(0);
                break;
            case ACTIVITY_EDIT:
                Long rowId = extras.getLong(ProductsDbAdapter.KEY_ROWID);
                if (rowId != null) {
                    String editTitle = extras.getString(ProductsDbAdapter.KEY_TITLE);
                    Double editWeight = extras.getDouble(ProductsDbAdapter.KEY_WEIGHT);
                    Double editPrice = extras.getDouble(ProductsDbAdapter.KEY_PRICE);
                    String editBody = extras.getString(ProductsDbAdapter.KEY_BODY);
                    mDbHelper.updateProduct(rowId, editTitle, editWeight, editPrice, editBody);
                }
                fillData(0);
                break;
        }
    }

}

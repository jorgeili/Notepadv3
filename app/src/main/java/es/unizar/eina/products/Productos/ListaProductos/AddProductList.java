package es.unizar.eina.products.Productos.ListaProductos;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import es.unizar.eina.products.Productos.Productos.ProductEdit;
import es.unizar.eina.products.Productos.Productos.ProductsDbAdapter;
import es.unizar.eina.products.R;


public class AddProductList extends AppCompatActivity {

    private static final int ADD_ID = Menu.FIRST + 2;

    private ProductsDbAdapter mDbHelper;
    private Cursor mProductsCursor;
    private ListView mList;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        Intent intent = getIntent();
        String value = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.v("VALUEEEEEEEEEE",value);

        mDbHelper = new ProductsDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.add_product_list);
        fillData();

        registerForContextMenu(mList);

    }

    private void fillData() {
        // Get all of the notes from the database and create the item list
        mProductsCursor = mDbHelper.fetchAllProducts();
        startManagingCursor(mProductsCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { ProductsDbAdapter.KEY_TITLE };

        // and an array of the fields we want to bind those fields to (in this case just nameProd)
        int[] to = new int[] { R.id.nameProd };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter products =
                new SimpleCursorAdapter(this, R.layout.row, mProductsCursor, from, to);
        mList.setAdapter(products);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, ADD_ID, Menu.NONE, R.string.add_product);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case ADD_ID:
                return true;
        }
        return super.onContextItemSelected(item);
    }



}

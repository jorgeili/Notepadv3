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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import es.unizar.eina.products.Productos.BD.ProductsDbAdapter;
import es.unizar.eina.products.R;


public class AddProductList extends AppCompatActivity {

    private static final int ADD_ID = Menu.FIRST + 2;

    private ProductsDbAdapter mDbHelper;
    private Cursor mProductsCursor;
    private ListView mList;
    private String value;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        final Intent intent = getIntent();
        value = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.v("idSL", "id: "+value);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mDbHelper = new ProductsDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.add_product_list);

        Button confirmButton = (Button) findViewById(R.id.confirm_list);

        fillData();

        registerForContextMenu(mList);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                Intent mIntent = new Intent();
                //mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });
    }

    private void fillData() {
        mProductsCursor = mDbHelper.fetchAllProducts();
        startManagingCursor(mProductsCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { ProductsDbAdapter.KEY_TITLE, ProductsDbAdapter.KEY_WEIGHT, ProductsDbAdapter.KEY_PRICE };

        // and an array of the fields we want to bind those fields to (in this case just nameProd)
        int[] to = new int[] { R.id.nameProd, R.id.weightProd, R.id.priceProd };

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
        switch (item.getItemId()) {
            case ADD_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                long add = mDbHelper.insertProductOnSL(Long.parseLong(value), info.id);
                return add > 0;
        }
        return super.onContextItemSelected(item);
    }
}

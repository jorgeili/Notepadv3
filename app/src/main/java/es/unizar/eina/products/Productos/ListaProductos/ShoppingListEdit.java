package es.unizar.eina.products.Productos.ListaProductos;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import es.unizar.eina.products.Productos.Productos.ProductsDbAdapter;
import es.unizar.eina.products.R;

public class ShoppingListEdit extends AppCompatActivity {

    private Cursor mProductsCursor;

    private EditText mTitleText;
    private EditText mWeightText;
    private EditText mPriceText;
    private ListView mListProd;
    private Long mRowId;
    private ProductsDbAdapter mDbHelper;
    public String SL_rowid;

    private static final int ACTIVITY_ADD_PRODUCT=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_edit);
        setTitle(R.string.title_activity_shoppingList_edit);

        mTitleText = (EditText) findViewById(R.id.nameSL);
        mWeightText = (EditText) findViewById(R.id.title_weight);
        mWeightText.setEnabled(false);
        mPriceText = (EditText) findViewById(R.id.title_price);
        mPriceText.setEnabled(false);
        mListProd = (ListView) findViewById(R.id.products_in_list);

        mDbHelper = new ProductsDbAdapter(this);
        mDbHelper.open();

        Button addProduct = (Button) findViewById(R.id.button_add);
        Button deleteButton = (Button) findViewById(R.id.delete_list);
        Button confirmButton = (Button) findViewById(R.id.confirm_list);

        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(ProductsDbAdapter.KEY_TITLE_SL);
            Double weight = extras.getDouble(ProductsDbAdapter.KEY_WEIGHT_SL,0);
            Double price = extras.getDouble(ProductsDbAdapter.KEY_PRICE_SL,0);
            mRowId = extras.getLong(ProductsDbAdapter.KEY_ROWID_SL);
            SL_rowid = mRowId.toString();
            if (title != null) {
                mTitleText.setText(title);
            }
            if (weight != null) {
                mWeightText.setText(Double.toString(weight));
            }
            if (price != null) {
                mPriceText.setText(Double.toString(price));
            }
            fillData();
        }
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openAddProductActivity();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString(ProductsDbAdapter.KEY_TITLE_SL, mTitleText.getText().toString());
                bundle.putDouble(ProductsDbAdapter.KEY_WEIGHT_SL, 0.0);
                bundle.putDouble(ProductsDbAdapter.KEY_PRICE_SL, 0.0);

                if (mRowId != null) {
                    bundle.putLong(ProductsDbAdapter.KEY_ROWID_SL, mRowId);
                }

                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                mDbHelper.deleteShoppingList(mRowId);
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_TEXT,"ELIMINADO");
                setResult(RESULT_OK, intent);
                finish();
            }

        });
    }

    private void fillData() {
        // Get all of the notes from the database and create the item list
        mProductsCursor = mDbHelper.fetchAllSLProducts();
        startManagingCursor(mProductsCursor);

        // Create an array to specify the fields we want to display in the list
        String[] from = new String[] { ProductsDbAdapter.KEY_TITLE, ProductsDbAdapter.KEY_WEIGHT, ProductsDbAdapter.KEY_PRICE };

        // and an array of the fields we want to bind those fields to
        int[] to = new int[] { R.id.nameProd, R.id.weightProd, R.id.priceProd };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter products =
                new SimpleCursorAdapter(this, R.layout.product_in_list, mProductsCursor, from, to);
        mListProd.setAdapter(products);
    }

    private void fillWeightPrice() {
        // Get all of the notes from the database and create the item list
        mProductsCursor = mDbHelper.fetchWeight();
        startManagingCursor(mProductsCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[] { ProductsDbAdapter.KEY_WEIGHT, ProductsDbAdapter.KEY_PRICE };

        // and an array of the fields we want to bind those fields to (in this case just nameProd)
        int[] to = new int[] { R.id.weightProd, R.id.priceProd };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter products =
                new SimpleCursorAdapter(this, R.layout.product_in_list, mProductsCursor, from, to);
        mListProd.setAdapter(products);
    }

    public void openAddProductActivity() {
        Intent intentsl = new Intent(this, AddProductList.class);
        intentsl.putExtra(Intent.EXTRA_TEXT,SL_rowid);
        startActivityForResult(intentsl, ACTIVITY_ADD_PRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        switch (requestCode) {
            case ACTIVITY_ADD_PRODUCT:

                break;
        }
    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}

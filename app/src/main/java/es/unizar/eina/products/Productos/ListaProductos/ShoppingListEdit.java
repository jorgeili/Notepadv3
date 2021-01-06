package es.unizar.eina.products.Productos.ListaProductos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import es.unizar.eina.products.Productos.Productos.ProductsDbAdapter;
import es.unizar.eina.products.R;

public class ShoppingListEdit extends AppCompatActivity {

    private EditText mTitleText;
    private EditText mWeightText;
    private EditText mPriceText;
    private Long mRowId;
    private ProductsDbAdapter mDbHelper;
    public String SL_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_edit);
        setTitle(R.string.title_activity_shoppingList_edit);

        mTitleText = (EditText) findViewById(R.id.nameSL);
        mWeightText = (EditText) findViewById(R.id.title_weight);
        mPriceText = (EditText) findViewById(R.id.title_price);

        mDbHelper = new ProductsDbAdapter(this);
        mDbHelper.open();

        Button addProduct = (Button) findViewById(R.id.button_add);
        Button deleteButton = (Button) findViewById(R.id.delete_list);
        Button confirmButton = (Button) findViewById(R.id.confirm_list);

        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(ProductsDbAdapter.KEY_TITLE_SL);
            SL_title = title;
            Double weight = extras.getDouble(ProductsDbAdapter.KEY_WEIGHT_SL,0);
            Double price = extras.getDouble(ProductsDbAdapter.KEY_PRICE_SL,0);
            mRowId = extras.getLong(ProductsDbAdapter.KEY_ROWID_SL);

            if (title != null) {
                mTitleText.setText(title);
            }
            if (weight != null) {
                mWeightText.setText(Double.toString(weight));
            }
            if (price != null) {
                mPriceText.setText(Double.toString(price));
            }
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
                bundle.putDouble(ProductsDbAdapter.KEY_WEIGHT_SL, Double.parseDouble(mWeightText.getText().toString()));
                bundle.putDouble(ProductsDbAdapter.KEY_PRICE_SL, Double.parseDouble(mPriceText.getText().toString()));

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

    public void openAddProductActivity() {
        Intent intentsl = new Intent(this, AddProductList.class);
        intentsl.putExtra(Intent.EXTRA_TEXT,SL_title);
        startActivity(intentsl);
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

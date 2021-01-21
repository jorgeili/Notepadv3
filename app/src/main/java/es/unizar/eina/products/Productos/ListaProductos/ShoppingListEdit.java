package es.unizar.eina.products.Productos.ListaProductos;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import es.unizar.eina.products.Productos.BD.ProductsDbAdapter;
import es.unizar.eina.products.R;

public class ShoppingListEdit extends AppCompatActivity {

    private CustomeAdapter customAdapter;
    private EditText mTitleText;
    private EditText mWeightText;
    private EditText mPriceText;
    private ListView mListProd;
    private Long mRowId;
    private ProductsDbAdapter mDbHelper;
    public String SL_rowid;
    public ArrayList<EditModel> quantityArrayList;
    public ArrayList<TextView> namesArrayList;
    public ArrayList<TextView> weightArrayList;
    public ArrayList<TextView> pricesArrayList;

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
        mListProd.setItemsCanFocus(true);

        mDbHelper = new ProductsDbAdapter(this);
        mDbHelper.open();

        Button addProduct = (Button) findViewById(R.id.button_add);
        Button deleteButton = (Button) findViewById(R.id.delete_list);
        Button confirmButton = (Button) findViewById(R.id.confirm_list);

        mRowId = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(ProductsDbAdapter.KEY_TITLE_SL);
            mRowId = extras.getLong(ProductsDbAdapter.KEY_ROWID_SL);

            SL_rowid = mRowId.toString();

            fillData();

            if (title != null) {
                mTitleText.setText(title);
            }

        }
        else {
            deleteButton.setEnabled(false);
        }
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDbHelper.updateQuantities(quantityArrayList, namesArrayList, SL_rowid);
                openAddProductActivity();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundle = new Bundle();

                String title = mTitleText.getText().toString().equals("") ? "Sin nombre" : mTitleText.getText().toString();
                bundle.putString(ProductsDbAdapter.KEY_TITLE_SL, title);
                if (mRowId != null) {
                    bundle.putLong(ProductsDbAdapter.KEY_ROWID_SL, mRowId);
                }
                mDbHelper.updateQuantities(quantityArrayList, namesArrayList, SL_rowid);

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

    private void fillData(){
        namesArrayList = new ArrayList<>();
        weightArrayList = new ArrayList<>();
        pricesArrayList = new ArrayList<>();
        quantityArrayList = new ArrayList<>();

        if (mDbHelper.getNumProducts(SL_rowid) > 0) {
            Cursor cursor = mDbHelper.fetchAllSLProducts(SL_rowid);
            if (cursor.moveToFirst()) {
                do {
                    TextView textViewName = new TextView(this);
                    textViewName.setEnabled(false);
                    textViewName.setText(cursor.getString(cursor.getColumnIndex("title")));
                    namesArrayList.add(textViewName);

                    TextView textViewWeight = new TextView(this);
                    textViewWeight.setEnabled(false);
                    textViewWeight.setText(cursor.getString(cursor.getColumnIndex("weight")));
                    weightArrayList.add(textViewWeight);

                    TextView textViewPrice = new TextView(this);
                    textViewPrice.setEnabled(false);
                    textViewPrice.setText(cursor.getString(cursor.getColumnIndex("price")));
                    pricesArrayList.add(textViewPrice);

                    EditModel editModel = new EditModel();
                    editModel.setEditTextValue(cursor.getString(cursor.getColumnIndex("quantity")));
                    quantityArrayList.add(editModel);

                } while (cursor.moveToNext());
            }
            cursor.close();
            customAdapter = new CustomeAdapter(this,quantityArrayList, namesArrayList, weightArrayList, pricesArrayList);
            mListProd.setAdapter(customAdapter);
        }

        Double[] res = mDbHelper.getWeightPriceSL(SL_rowid);
        mWeightText.setText(Double.toString(res[0]));
        mPriceText.setText(Double.toString(res[1]));

        Log.v("Num cantidades: ", String.valueOf(quantityArrayList.size()));
    }

    public void openAddProductActivity() {
        Intent intentsl = new Intent(this, AddProductList.class);
        Log.v("spinner","Id lista " + SL_rowid);
        intentsl.putExtra(Intent.EXTRA_TEXT,SL_rowid);
        startActivityForResult(intentsl, ACTIVITY_ADD_PRODUCT);
        fillData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        switch (requestCode) {
            case ACTIVITY_ADD_PRODUCT:
                fillData();
                break;
        }
    }
}

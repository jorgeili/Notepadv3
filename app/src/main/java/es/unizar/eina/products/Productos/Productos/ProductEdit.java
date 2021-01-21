package es.unizar.eina.products.Productos.Productos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import es.unizar.eina.products.Productos.BD.ProductsDbAdapter;
import es.unizar.eina.products.R;

public class ProductEdit extends AppCompatActivity {

    private EditText mTitleText;
    private EditText mWeightText;
    private EditText mPriceText;
    private EditText mBodyText;
    private Long mRowId;
    private ProductsDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_edit);
        setTitle(R.string.edit_product);

        mTitleText = (EditText) findViewById(R.id.nameProd);
        mWeightText = (EditText) findViewById(R.id.weightProd);
        mPriceText = (EditText) findViewById(R.id.priceProd);
        mBodyText = (EditText) findViewById(R.id.body);

        mDbHelper = new ProductsDbAdapter(this);
        mDbHelper.open();

        final Button deleteButton = (Button) findViewById(R.id.delete);
        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = null;
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(ProductsDbAdapter.KEY_TITLE);
            Double weight = extras.getDouble(ProductsDbAdapter.KEY_WEIGHT);
            Double price = extras.getDouble(ProductsDbAdapter.KEY_PRICE);
            String body = extras.getString(ProductsDbAdapter.KEY_BODY);
            mRowId = extras.getLong(ProductsDbAdapter.KEY_ROWID);

            if (title != null) {
                mTitleText.setText(title);
            }
            if (body != null) {
                mBodyText.setText(body);
            }
            if (weight != null) {
                mWeightText.setText(Double.toString(weight));
            }
            if (price != null) {
                mPriceText.setText(Double.toString(price));
            }
        }
        else {
            deleteButton.setEnabled(false);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString(ProductsDbAdapter.KEY_TITLE, mTitleText.getText().toString());
                bundle.putDouble(ProductsDbAdapter.KEY_WEIGHT, Double.parseDouble(mWeightText.getText().toString()));
                bundle.putDouble(ProductsDbAdapter.KEY_PRICE, Double.parseDouble(mPriceText.getText().toString()));
                bundle.putString(ProductsDbAdapter.KEY_BODY, mBodyText.getText().toString());

                if (mRowId != null) {
                    bundle.putLong(ProductsDbAdapter.KEY_ROWID, mRowId);
                }

                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });
        deleteButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                mDbHelper.deleteProduct(mRowId);
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_TEXT,"ELIMINADO");
                setResult(RESULT_OK, intent);
                finish();
            }

        });
    }

}

package es.unizar.eina.products;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProductEdit extends AppCompatActivity {

    private EditText mTitleText;
    private EditText mWeightText;
    private EditText mPriceText;
    private EditText mBodyText;
    private Long mRowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_edit);
        setTitle(R.string.edit_product);

        mTitleText = (EditText) findViewById(R.id.title);
        mWeightText = (EditText) findViewById(R.id.weightProd);
        mPriceText = (EditText) findViewById(R.id.priceProd);
        mBodyText = (EditText) findViewById(R.id.body);

        Button deleteButton = (Button) findViewById(R.id.delete);
        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(ProductsDbAdapter.KEY_TITLE);
            String weight = extras.getString(ProductsDbAdapter.KEY_WEIGHT);
            String price = extras.getString(ProductsDbAdapter.KEY_PRICE);
            String body = extras.getString(ProductsDbAdapter.KEY_BODY);
            mRowId = extras.getLong(ProductsDbAdapter.KEY_ROWID);

            if (title != null) {
                mTitleText.setText(title);
            }
            if (body != null) {
                mBodyText.setText(body);
            }
            if (weight != null) {
                mWeightText.setText(weight);
            }
            if (price != null) {
                mPriceText.setText(price);
            }
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString(ProductsDbAdapter.KEY_TITLE, mTitleText.getText().toString());
                bundle.putString(ProductsDbAdapter.KEY_WEIGHT, mWeightText.getText().toString());
                bundle.putString(ProductsDbAdapter.KEY_PRICE, mPriceText.getText().toString());
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

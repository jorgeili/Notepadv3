package es.unizar.eina.products.Productos.ListaProductos;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import es.unizar.eina.products.Productos.Productos.ProductsDbAdapter;
import es.unizar.eina.products.R;

/**
 * Created by Parsania Hardik on 03-Jan-17.
 */
public class CustomeAdapter extends BaseAdapter {
    private Context context;
    public static ArrayList<EditModel> editModelArrayList;
    public static ArrayList<TextView> namesArrayList;
    public static ArrayList<TextView> weightsArrayList;
    public static ArrayList<TextView> pricesArrayList;

    private ProductsDbAdapter mDB;

    public CustomeAdapter(Context context, ArrayList<EditModel> editModelArrayList,
                          ArrayList<TextView> namesArrayList, ArrayList<TextView> weightsArrayList,
                          ArrayList<TextView> pricesArrayList) {
        this.context = context;
        this.editModelArrayList = editModelArrayList;
        this.namesArrayList = namesArrayList;
        this.weightsArrayList = weightsArrayList;
        this.pricesArrayList = pricesArrayList;
    }
    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getCount() {
        return editModelArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return editModelArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflaterQuant = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflaterQuant.inflate(R.layout.product_in_list, null, true);
            holder.editText = (EditText) convertView.findViewById(R.id.quantityProd);
            holder.textViewName = (TextView) convertView.findViewById(R.id.nameProd);
            holder.textViewWeight = (TextView) convertView.findViewById(R.id.weightProd);
            holder.textViewPrice = (TextView) convertView.findViewById(R.id.priceProd);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textViewName.setText(namesArrayList.get(position).getText());

        holder.textViewPrice.setText(pricesArrayList.get(position).getText());

        holder.textViewWeight.setText(weightsArrayList.get(position).getText());

        holder.editText.setText(editModelArrayList.get(position).getEditTextValue());
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editModelArrayList.get(position).setEditTextValue(holder.editText.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return convertView;
    }
    private class ViewHolder {
        protected EditText editText;
        protected TextView textViewName;
        protected TextView textViewWeight;
        protected TextView textViewPrice;
    }
}
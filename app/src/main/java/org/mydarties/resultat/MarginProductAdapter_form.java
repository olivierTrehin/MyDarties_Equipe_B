package org.mydarties.resultat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import org.mydarties.R;

import java.util.List;

/**
 * Created by Olivier Tréhin on 29/11/2016.
 */

public class MarginProductAdapter_form extends ArrayAdapter<Product>{

    public MarginProductAdapter_form(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_adapter_margin_product_form,parent, false);
        }

        SalesProductViewHolder viewHolder = (SalesProductViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new SalesProductViewHolder();
            viewHolder.name = (EditText) convertView.findViewById(R.id.nameProduitEdit);
            viewHolder.objMargin =(EditText) convertView.findViewById(R.id.marginObjEdit);
            viewHolder.realMargin = (EditText) convertView.findViewById(R.id.marginRealEdit);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Product> products
        Product product = getItem(position);

        //remplissage des vues
        viewHolder.name.setText(""+product.getName());
        return convertView;
    }

    private class SalesProductViewHolder{
        public EditText name;
        public EditText realMargin;
        public EditText objMargin;
    }
}

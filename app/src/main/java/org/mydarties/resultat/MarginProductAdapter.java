package org.mydarties.resultat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.mydarties.R;

import java.util.List;

/**
 * Created by Olivier Tréhin on 29/11/2016.
 */

public class MarginProductAdapter extends ArrayAdapter<Product>{

    public MarginProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_adapter_margin_product_result,parent, false);
        }

        SalesProductViewHolder viewHolder = (SalesProductViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new SalesProductViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.nameProduit);
            viewHolder.objMargin =(TextView) convertView.findViewById(R.id.marginObj);
            viewHolder.realMargin = (TextView) convertView.findViewById(R.id.marginReal);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Product> products
        Product product = getItem(position);

        //remplissage des vues
        viewHolder.name.setText(""+product.getName());
        viewHolder.objMargin.setText(""+product.getObjMargin());
        viewHolder.realMargin.setText(""+product.getRealMargin());
        return convertView;
    }

    private class SalesProductViewHolder{
        public TextView name;
        public TextView realMargin;
        public TextView objMargin;
    }
}

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

public class SalesProductAdapter_form extends ArrayAdapter<Product>{

    public SalesProductAdapter_form(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_adapter_sales_product_form,parent, false);
        }

        SalesProductViewHolder viewHolder = (SalesProductViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new SalesProductViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.nameProduitEdit);
            viewHolder.objSales =(TextView) convertView.findViewById(R.id.salesObjEdit);
            viewHolder.realSales = (TextView) convertView.findViewById(R.id.salesRealEdit);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Product> products
        Product product = getItem(position);

        //remplissage des vues
        viewHolder.name.setText(""+product.getName());
        viewHolder.objSales.setText(""+product.getObjSales());
        viewHolder.realSales.setText(""+product.getRealSales());
        return convertView;
    }

    private class SalesProductViewHolder{
        public TextView name;
        public TextView realSales;
        public TextView objSales;
    }
}

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

public class TurnoverProductAdapter_form extends ArrayAdapter<Product>{
    ProductViewHolder viewHolder;
    boolean textChanged = false;

    public TurnoverProductAdapter_form(Context context, List<Product> products) {
        super(context, 0, products);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_adapter_turnover_product_form,parent, false);
        }

        this.viewHolder = (ProductViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ProductViewHolder();

            viewHolder.name = (TextView) convertView.findViewById(R.id.TextViewProduitEdit);
            viewHolder.objTurn =(TextView) convertView.findViewById(R.id.TextViewProduitObjEdit);
            viewHolder.realTurn = (TextView) convertView.findViewById(R.id.TextViewReelEdit);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Product> products
        Product product = getItem(position);

        //remplissage des vues
        viewHolder.name.setText(""+product.getName());
        viewHolder.objTurn.setText(""+product.getObjTurnover());
        viewHolder.realTurn.setText(""+product.getRealTurnover());
        return convertView;
    }

    public class ProductViewHolder{
        public TextView name;
        public TextView realTurn;
        public TextView objTurn;
    }
}

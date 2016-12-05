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

public class TurnoverProductAdapter extends ArrayAdapter<Product>{

    public TurnoverProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.turnover_product,parent, false);
        }

        ProductViewHolder viewHolder = (ProductViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ProductViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.TextViewProduit);
            viewHolder.objTurn =(TextView) convertView.findViewById(R.id.TextViewProduitObj);
            viewHolder.realTurn = (TextView) convertView.findViewById(R.id.TextViewReel);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Product> products
        Product product = getItem(position);

        //remplissage des vues
        viewHolder.name.setText(""+product.getName());
        viewHolder.realTurn.setText(""+product.getRealTurnover());
        viewHolder.objTurn.setText(""+product.getObjTurnover());
        return convertView;
    }

    private class ProductViewHolder{
        public TextView name;
        public TextView realTurn;
        public TextView objTurn;
    }
}

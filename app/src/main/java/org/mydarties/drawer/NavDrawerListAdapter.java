package org.mydarties.drawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.mydarties.R;

import java.util.List;

/**
 * Created by DartiesB on 02/12/2016.
 */
public class NavDrawerListAdapter extends ArrayAdapter<NavDrawerItem> {
    Context context;

    public NavDrawerListAdapter(Context context, List<NavDrawerItem> NavItem){
        super(context,0, NavItem);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item,parent, false);
        }

        NavDrawerListAdapter.NavViewHolder viewHolder = (NavDrawerListAdapter.NavViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new NavDrawerListAdapter.NavViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.nav_icon);
            viewHolder.title = (TextView) convertView.findViewById(R.id.nav_label);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        NavDrawerItem item = getItem(position);

        Drawable drawable = this.context.getResources().getDrawable(item.getNavIcon());
        //il ne reste plus qu'à remplir notre vue
        viewHolder.icon.setImageDrawable(drawable);
        viewHolder.title.setText(item.getNavTittle());

        return convertView;
    }

    private class NavViewHolder{
        public TextView title;
        public ImageView icon;
    }
}

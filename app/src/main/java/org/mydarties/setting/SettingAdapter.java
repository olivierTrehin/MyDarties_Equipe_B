package org.mydarties.setting;

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
 * Created by DartiesB on 25/11/2016.
 */

public class SettingAdapter extends ArrayAdapter<Setting_item> {

    Context context;

    public SettingAdapter(Context context, List<Setting_item> Settings){
        super(context,0, Settings);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_setting_adapter,parent, false);
        }

        SettingViewHolder viewHolder = (SettingViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new SettingViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.setting_icon);
            viewHolder.label = (TextView) convertView.findViewById(R.id.setting_label);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Setting_item item = getItem(position);

        int id = this.context.getResources().getIdentifier(item.getIcon(), "drawable", this.context.getPackageName());
        Drawable drawable = this.context.getResources().getDrawable(id);
        //il ne reste plus qu'à remplir notre vue
        viewHolder.icon.setImageDrawable(drawable);
        viewHolder.label.setText(item.getLabel());

        return convertView;
    }

    private class SettingViewHolder{
        public ImageView icon;
        public TextView label;
    }
}

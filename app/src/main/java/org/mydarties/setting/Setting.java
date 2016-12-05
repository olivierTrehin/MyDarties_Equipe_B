package org.mydarties.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.mydarties.R;
import org.mydarties.dir.drawer_dir.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class Setting extends BaseActivity {

    ListView list_setting;
    String[] prenoms = new String[]{
            "Modifier mot de passe"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_setting_listview);

        list_setting = (ListView)findViewById(R.id.list_setting);
        List<Setting_item> settings = genererSetting();

        SettingAdapter adapter = new SettingAdapter(Setting.this, settings);
        list_setting.setAdapter(adapter);


        //Allow to access to the wanted setting activity
        list_setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Setting_item item = (Setting_item)list_setting.getItemAtPosition(position);

                if(item.getLabel().equals("Modifier le mot de passe")){
                    Toast.makeText(getBaseContext(), item.getLabel(), Toast.LENGTH_LONG).show();
                    Intent intent_new_password = new Intent(Setting.this, SettingDirNewPasswd.class);
                    startActivity(intent_new_password);
                }else{
                    Toast.makeText(getBaseContext(), "Fail", Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    private List<Setting_item> genererSetting(){
        List<Setting_item> settings = new ArrayList<Setting_item>();
        settings.add(new Setting_item("ic_lock_black_24dp", "Modifier le mot de passe"));
        return settings;
    }


}

package org.mydarties.dir;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.mydarties.R;
import org.mydarties.dir.drawer_dir.BaseActivity;

public class HomeDir extends BaseActivity {

    private TextView NomDirView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_main_dir);
        SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        String NOM = prefs.getString("user_surname", "Error somewhere");//"No name defined" is the default value.


        NomDirView = (TextView)findViewById(R.id.NomDir);

        NomDirView.setText(NOM);


    }
}

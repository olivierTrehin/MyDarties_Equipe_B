package org.mydarties.resp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.mydarties.R;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivityResp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_resp);

        Bundle extra_result = getIntent().getExtras();

        String result = extra_result.getString("user_info");

        try {
            JSONObject user = new JSONObject(String.valueOf(result));
            System.out.println("User = " + user.getString("user_surname"));
            System.out.println("Profil = " + user.getString("lib_profil"));
            System.out.println("Type = " + user.getInt("type_profil"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

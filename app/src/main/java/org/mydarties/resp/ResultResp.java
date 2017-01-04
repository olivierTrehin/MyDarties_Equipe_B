package org.mydarties.resp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.mydarties.R;
import org.mydarties.drawer.BaseActivity;

/**
 * Created by windows8 on 02/01/2017.
 */

public class ResultResp extends BaseActivity {

    private TextView chosenVille;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_result_resp);

        Intent intent = getIntent();
        String ville = intent.getStringExtra("ville");

        TextView textVille = (TextView)findViewById(R.id.textChosenVille);
        textVille.setText(ville);

    }

}

package org.mydarties.resp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.mydarties.R;
import org.mydarties.drawer.BaseActivity;

/**
 * Created by DartiesB on 09/12/2016.
 */

public class Consult_form extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private TextView ProfilDirView;
    private Spinner SpinMag;
    private String[] Spin_item_ville;
    private String PROFIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_form_consult_resp);

        SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        PROFIL = prefs.getString("lib_profil", "Error somewhere");

        System.out.println(this.PROFIL);

        ProfilDirView = (TextView)findViewById(R.id.ProfilHome);
        ProfilDirView.setText(PROFIL);

        SpinMag = (Spinner)findViewById(R.id.spinner_mag_resp);
        SpinMag.setOnItemSelectedListener(this);

        Spin_item_ville = setArraySpinMag();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Spin_item_ville);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        SpinMag.setAdapter(dataAdapter);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String[] setArraySpinMag(){

        if(PROFIL.equals("Directeur Nord_Ouest")) {

            Spin_item_ville = getResources().getStringArray(R.array.Nord_Ouest);
        }else if(PROFIL.equals("Directeur Nord_Est")){

            Spin_item_ville = getResources().getStringArray(R.array.Nord_Est);
        }else if(PROFIL.equals("Directeur Sud_Ouest")){

            Spin_item_ville = getResources().getStringArray(R.array.Sud_Ouest);
        }else if(PROFIL.equals("Directeur Sud_Est")){

            Spin_item_ville = getResources().getStringArray(R.array.Sud_Est);
        }else if(PROFIL.equals("Directeur Région_parisienne")){

            Spin_item_ville = getResources().getStringArray(R.array.Région_parisienne);
        };
        System.out.println(Spin_item_ville);
        return Spin_item_ville;
    }

}

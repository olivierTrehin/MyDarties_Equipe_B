package org.mydarties.resp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.mydarties.R;
import org.mydarties.drawer.BaseActivity;

public class HomeResp extends BaseActivity {

    private TextView NomDirView, PrenomDirView, MailDirView, ProfilDirView, DateMajDirView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_main_home);

        SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        String NOM = prefs.getString("name", "Error somewhere");//"No name defined" is the default value.
        String PRENOM = prefs.getString("surname", "Error somewhere");
        String MAIL = prefs.getString("mail", "Error somewhere");
        String PROFIL = prefs.getString("lib_profil", "Error somewhere");
        String DATEMAJ = prefs.getString("date_maj", "Error somewhere");
        //------------------------------------------------------------------------------------------
        NomDirView = (TextView)findViewById(R.id.NomHome);
        NomDirView.setText(NOM);
        //------------------------------------------------------------------------------------------
        PrenomDirView = (TextView)findViewById(R.id.PrenomHome);
        PrenomDirView.setText((PRENOM));
        //------------------------------------------------------------------------------------------
        MailDirView = (TextView)findViewById(R.id.MailHome);
        MailDirView.setText(MAIL);
        //------------------------------------------------------------------------------------------
        ProfilDirView = (TextView)findViewById(R.id.ProfilHome);
        ProfilDirView.setText(PROFIL);
        //------------------------------------------------------------------------------------------
        DateMajDirView = (TextView)findViewById(R.id.DateHome);
        DateMajDirView.setText(DATEMAJ);
        //------------------------------------------------------------------------------------------

    }
}

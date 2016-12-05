package org.mydarties.dir;

import android.os.Bundle;
import org.mydarties.R;
import org.mydarties.dir.drawer_dir.BaseActivity;

public class SaisirDir extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_saisir_dir);
    }
}

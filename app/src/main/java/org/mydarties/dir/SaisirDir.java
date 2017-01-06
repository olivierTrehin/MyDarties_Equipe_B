package org.mydarties.dir;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TableRow;

import org.mydarties.R;
import org.mydarties.drawer.BaseActivity;
import org.mydarties.resultat.MarginProductAdapter_form;
import org.mydarties.resultat.Product;
import org.mydarties.resultat.SalesProductAdapter_form;
import org.mydarties.resultat.TurnoverProductAdapter_form;

import java.util.ArrayList;
import java.util.List;

public class SaisirDir extends BaseActivity {

    private static List<Product> ProductArray;
    private View mProgressView, mLoginFormView;
    ListView mListView, salesView, marginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_form_saisir_dir);

        this.ProductArray = new ArrayList<>();

        this.addProduct();

        final TabHost host = (TabHost)findViewById(R.id.tabHostEdit);
        host.setup();

        //Tab 1
        TabHost.TabSpec turnoverSpec = host.newTabSpec("Chiffre d'affaires");
        turnoverSpec.setContent(R.id.tab1Edit);
        turnoverSpec.setIndicator("Chiffre d'affaires");
        host.addTab(turnoverSpec);

        //Tab 2
        TabHost.TabSpec salesSpec = host.newTabSpec("Ventes");
        salesSpec.setContent(R.id.tab2Edit);
        salesSpec.setIndicator("Ventes");
        host.addTab(salesSpec);

        //Tab 3
        TabHost.TabSpec marginSpec = host.newTabSpec("Marge");
        marginSpec.setContent(R.id.tab3Edit);
        marginSpec.setIndicator("Marge");
        host.addTab(marginSpec);

        //Création de la turnoverView
        mListView = (ListView) findViewById(R.id.turnoverViewEdit);
        final TurnoverProductAdapter_form adapter_Turnover = new TurnoverProductAdapter_form(SaisirDir.this, this.ProductArray);
        mListView.setAdapter(adapter_Turnover);



        //Création de la salesView
        salesView = (ListView) findViewById(R.id.salesViewEdit);
        SalesProductAdapter_form salesAdapter = new SalesProductAdapter_form(SaisirDir.this, this.ProductArray);
        salesView.setAdapter(salesAdapter);

        //Création de la marginView
        marginView = (ListView) findViewById(R.id.marginViewEdit);
        MarginProductAdapter_form marginAdapter = new MarginProductAdapter_form(SaisirDir.this, this.ProductArray);
        marginView.setAdapter(marginAdapter);

        Button addButton = (Button)findViewById(R.id.buttonAddProduit);
        addButton.setText("Ajouter Produit");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });


        Button sendButton = (Button)findViewById(R.id.buttonSendData);
        sendButton.setText("Envoyer");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View tab = host.getTabWidget().getChildTabViewAt(2);
                System.out.println(tab);
                System.out.println(adapter_Turnover.getItem(0).getRealTurnover());

                RelativeLayout rl = (RelativeLayout)mListView.getChildAt(0);
                TableRow tr = (TableRow)rl.getChildAt(0);
                EditText et = (EditText)tr.getChildAt(1);
                System.out.println(et.getText());
            }
        });

    }

    public void addProduct(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Ajouter un produit");
        alert.setMessage("Nom du produit");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                SaisirDir.ProductArray.add(new Product(input.getText().toString()));
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();


    }

}

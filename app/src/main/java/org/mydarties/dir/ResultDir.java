package org.mydarties.dir;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TabHost;

import org.mydarties.R;
import org.mydarties.resultat.MarginProductAdapter;
import org.mydarties.resultat.Product;
import org.mydarties.resultat.SalesProductAdapter;
import org.mydarties.resultat.TurnoverProductAdapter;
import org.mydarties.dir.drawer_dir.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ResultDir extends BaseActivity {

    ListView mListView;
    ListView salesView;
    ListView marginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_result_dir);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec turnoverSpec = host.newTabSpec("Chiffre d'affaires");
        turnoverSpec.setContent(R.id.tab1);
        turnoverSpec.setIndicator("Chiffre d'affaires");
        host.addTab(turnoverSpec);

        //Tab 2
        TabHost.TabSpec salesSpec = host.newTabSpec("Ventes");
        salesSpec.setContent(R.id.tab2);
        salesSpec.setIndicator("Ventes");
        host.addTab(salesSpec);

        //Tab 3
        TabHost.TabSpec marginSpec = host.newTabSpec("Marge");
        marginSpec.setContent(R.id.tab3);
        marginSpec.setIndicator("Marge");
        host.addTab(marginSpec);


        List<Product> products = genererProduct();
        //Création de la turnoverView
        mListView = (ListView) findViewById(R.id.turnoverView);
        TurnoverProductAdapter adapter = new TurnoverProductAdapter(ResultDir.this, products);
        mListView.setAdapter(adapter);

        //Création de la salesView
        salesView = (ListView) findViewById(R.id.salesView);
        SalesProductAdapter salesAdapter = new SalesProductAdapter(ResultDir.this, products);
        salesView.setAdapter(salesAdapter);

        //Création de la marginView
        marginView = (ListView) findViewById(R.id.marginView);
        MarginProductAdapter marginAdapter = new MarginProductAdapter(ResultDir.this, products);
        marginView.setAdapter(marginAdapter);

    }

    private List<Product> genererProduct(){
        List<Product> listProducts = new ArrayList<Product>();
        listProducts.add(new Product("tv", 1,1,1,1,1,1));
        listProducts.add(new Product("tv", 1,10,10,10,10,10));
        listProducts.add(new Product("tv", 1,1,1,1,1,1));
        listProducts.add(new Product("tv", 1,1,1,1,1,2));
        return listProducts;
    }
}

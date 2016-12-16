package org.mydarties.dir;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mydarties.R;
import org.mydarties.drawer.BaseActivity;
import org.mydarties.resultat.MarginProductAdapter;
import org.mydarties.resultat.Product;
import org.mydarties.resultat.SalesProductAdapter;
import org.mydarties.resultat.TurnoverProductAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResultDir extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView mListView;
    ListView salesView;
    ListView marginView;

    private View mProgressView;
    private View mLoginFormView;

    String response;

    /**
     * Connection_timeout and Read_timeout in milliseconds
     */
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_result_dir);

        mLoginFormView = findViewById(R.id.tabHost);
        mProgressView = findViewById(R.id.login_progress);

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

        SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        String token = prefs.getString("token", "Error somewhere");
        int id = prefs.getInt("id", 999);
        int id_profil = prefs.getInt("profil", 999);

        SharedPreferences tableau = getSharedPreferences("TABLEAU", MODE_PRIVATE);

        if(!tableau.contains("accueil")) {
            System.out.println("Don't exist...");
            showProgress(true);
            String url = "http://172.24.1.1/darties1/private_html/index.php/getTableauAcceuilDM?id=" + id + "&id_profil=" + id_profil + "&token=" + token;
            System.out.println("@URL = " + url);
            new HttpAsyncTask().execute(url);

        }else {
            System.out.println("Exist...");
            List<Product> products = null;

            products = genererProduct();

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

    }

    private List<Product> genererProduct() {
        SharedPreferences prefs = getSharedPreferences("TABLEAU", MODE_PRIVATE);
        String response = prefs.getString("accueil", "Error somewhere");
        System.out.println("response : " + response);
        List<Product> listProducts = new ArrayList<Product>();
        try {

            JSONArray jsonArray = new JSONArray(String.valueOf(response));

            for(int i = 0; i< jsonArray.length(); i++){
                JSONObject object = new JSONObject(String.valueOf(jsonArray.get(i)));
                System.out.println(object.get("lib_famille_produit"));
                String libFamme = object.getString("lib_famille_produit");
                int CA_Reel = object.getInt("CA_Reel");
                int CA_Objectif = object.getInt("CA_Objectif");
                int Ventes_Reel = object.getInt("Ventes_Reel");
                int Ventes_Objectif = object.getInt("Ventes_Objectif");
                int Marge_Reel = object.getInt("Marge_Reel");
                int Marge_Objectif = object.getInt("Marge_Objectif");
                listProducts.add(new Product(libFamme,CA_Objectif,CA_Reel,Ventes_Objectif,Ventes_Reel,Marge_Objectif,Marge_Reel));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listProducts;
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            response = GET(urls[0]);
            return response;

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            showProgress(false);
            System.out.println(response);
            SharedPreferences.Editor editor = getSharedPreferences("TABLEAU", MODE_PRIVATE).edit();
            editor.putString("accueil", response).apply();

            List<Product> products = null;

            products = genererProduct();

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
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ResultDir.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}

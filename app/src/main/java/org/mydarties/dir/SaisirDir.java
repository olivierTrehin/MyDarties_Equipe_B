package org.mydarties.dir;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mydarties.R;
import org.mydarties.drawer.BaseActivity;
import org.mydarties.resultat.MarginProductAdapter_form;
import org.mydarties.resultat.Product;
import org.mydarties.resultat.SalesProductAdapter_form;
import org.mydarties.resultat.TurnoverProductAdapter_form;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SaisirDir extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static ArrayList<Product> ProductArray;
    private View mProgressView, mLoginFormView;
    ListView mListView, salesView, marginView;
    String response;
    boolean succes;

    private SaisirDir.AddSalesTask mAuthTask = null;

    /**
     * Connection_timeout and Read_timeout in milliseconds
     */
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_form_saisir_dir);

        ProductArray = new ArrayList<>();
        System.out.println(ProductArray.toString());
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey("Array_Product")) {
                ProductArray = (ArrayList<Product>) getIntent().getExtras().get("Array_Product");
                JSONArray Product_array = new JSONArray(ProductArray);
                System.out.println("Array = "+Product_array);
            }
        }

        mLoginFormView = findViewById(R.id.activity_result_dir);
        mProgressView = findViewById(R.id.login_progress);

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
                addProduct(ProductArray);
            }
        });


        Button sendButton = (Button)findViewById(R.id.buttonSendData);
        sendButton.setText("Envoyer");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray = new JSONArray();
                for(int i = 0; i < ProductArray.size(); i++){
                    jsonArray.put(ProductArray.get(i).getJSONObject());
                }
                System.out.println(jsonArray.toString());

                showProgress(true);
                mAuthTask = new AddSalesTask(jsonArray.toString());
                mAuthTask.execute((Void) null);


            }
        });

    }

    public void addProduct(ArrayList<Product> list){

        Intent addThing = new Intent(SaisirDir.this, SaisirProduct.class);

        ArrayList<Product> Product_Array = list;
        addThing.putExtra("Array_Product", Product_Array);

        startActivity(addThing);

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
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), SaisirDir.ProfileQuery.PROJECTION,

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


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }

    /**
     * Represents an asynchronous update password task used
     */
    public class AddSalesTask extends AsyncTask<Void, Void, Boolean> {

        String jsonArray;
        HttpURLConnection conn;
        URL url = null;
        private boolean resultat;

        AddSalesTask(String jsonArray) {
            this.jsonArray = jsonArray;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            Boolean succes = false;

            try {

                // Enter URL address where your php file resides
                url = new URL(getString(R.string.url_addVente));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                System.out.println("Add = " + conn);

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
                String token = prefs.getString("token", "Error somewhere");//"No name defined" is the default value.
                String idProfil = prefs.getString("id_profil", "Wrong label");

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("string_vente", this.jsonArray)
                        .appendQueryParameter("id_profil", idProfil)
                        .appendQueryParameter("token", (token));
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {

                int response_code = conn.getResponseCode();
                System.out.println("Response = " + response_code);

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    System.out.println(input);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    System.out.println("Result = " + result);

                    JSONObject returned = new JSONObject(String.valueOf(result));
                    resultat = returned.getBoolean("success");


                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            System.out.println("Succes : " + succes);
            return succes;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if(resultat){
                Toast.makeText(SaisirDir.this, "Donnees ajoutés avec succès!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SaisirDir.this, "Données déjà existante (Conflit Primary Key)", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

}

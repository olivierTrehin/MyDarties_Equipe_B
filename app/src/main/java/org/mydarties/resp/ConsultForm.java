package org.mydarties.resp;

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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.mydarties.R;
import org.mydarties.drawer.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by DartiesB on 09/12/2016.
 */

public class ConsultForm extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView ProfilDirView;
    private View mProgressView;
    private View mLoginFormView;
    private Spinner SpinMag;
    private String[] Spin_item_ville;
    private String PROFIL;
    private Button Search;
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_form_consult_resp);

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);

        SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        PROFIL = prefs.getString("lib_profil", "Error somewhere");

        System.out.println(this.PROFIL);

        ProfilDirView = (TextView)findViewById(R.id.ProfilHome);
        ProfilDirView.setText(PROFIL);

        showProgress(true);
        String url = "http://172.24.1.1/darties1/private_html/index.php/getSpinnerVille";
        System.out.println("@URL = " + url);
        new ConsultForm.HttpAsyncTask().execute(url);

        Search = (Button)findViewById(R.id.But_search_result_resp);

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ville = (String) SpinMag.getSelectedItem();
                Intent SearchTown = new Intent(ConsultForm.this, ResultResp.class);
                SearchTown.putExtra("ville", ville);
                startActivity(SearchTown);
            }
        });

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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> implements AdapterView.OnItemSelectedListener{
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

            SpinMag = (Spinner)findViewById(R.id.spinner_mag_resp);
            SpinMag.setOnItemSelectedListener(this);

            setSpinnerItem(response);

        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void setSpinnerItem(String response){
        try {
            JSONArray jsonArray = new JSONArray(String.valueOf(response));
            String[] items = new String[jsonArray.length()];
            for(int i = 0; i< jsonArray.length(); i++){
                String object = new String(String.valueOf(jsonArray.get(i)));
                System.out.println(object);
                items[i] = object;
            }

            Spin_item_ville = items;

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Spin_item_ville);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            SpinMag.setAdapter(dataAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
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

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ConsultForm.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    public void onLoaderReset(Loader<Cursor> loader) {

    }

}

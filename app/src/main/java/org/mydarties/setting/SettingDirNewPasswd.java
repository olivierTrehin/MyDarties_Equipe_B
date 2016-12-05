package org.mydarties.setting;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.mydarties.R;
import org.mydarties.dir.drawer_dir.BaseActivity;
import org.mydarties.resp.MainActivityResp;

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

public class SettingDirNewPasswd extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private SettingDirNewPasswd.UserResetPasswordTask mAuthTask = null;

    /**
     * Connection_timeout and Read_timeout in milliseconds
     */
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    // UI references.
    private EditText ancientPassword, mPasswordView1, mPasswordView2;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateDrawer(R.layout.activity_setting_new_passwd);

        // Set up the login form.
        ancientPassword = (EditText) findViewById(R.id.ancient_password);
        mPasswordView1 = (EditText) findViewById(R.id.new_password_1);
        mPasswordView2 = (EditText) findViewById(R.id.new_password_2);

        mPasswordView1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptReset();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptReset();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptReset() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        ancientPassword.setError(null);
        mPasswordView1.setError(null);
        mPasswordView2.setError(null);

        // Store values at the time of the login attempt.
        String ancient_password = ancientPassword.getText().toString();
        String new_password1 = mPasswordView1.getText().toString();
        String new_password2 = mPasswordView2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        ancient_password = prefs.getString("password", "Error somewhere");


        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(ancient_password)) {
            ancientPassword.setError(getString(R.string.error_invalid_password));
            focusView = ancientPassword;
            cancel = true;
        }

        //Check if current password is equal to field ancient password
        if(!(ancient_password.equals(ancient_password))){
            ancientPassword.setError(getString(R.string.error_correspond_ancient_password));
            focusView = ancientPassword;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(new_password1)) {
            mPasswordView1.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView1;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(new_password2)) {
            mPasswordView2.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView2;
            cancel = true;
        }

        //Check if user entered same new password twice
        if(!(new_password1.equals(new_password2))){
            mPasswordView2.setError(getString(R.string.error_correspond_password));
            focusView = mPasswordView2;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new SettingDirNewPasswd.UserResetPasswordTask(new_password1);
            mAuthTask.execute((Void) null);
        }



    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
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
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), SettingDirNewPasswd.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

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
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserResetPasswordTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPassword;
        HttpURLConnection conn;
        URL url = null;
        private String result_string;

        UserResetPasswordTask(String password) {
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            Boolean succes = false;

            try {

                // Enter URL address where your php file resides
                url = new URL(getString(R.string.url_updatepassword));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                System.out.println("Uddate = " + conn + " / new password = " + mPassword);

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
                String token = prefs.getString("token", "Error somewhere");//"No name defined" is the default value.

                int id = prefs.getInt("id", 999);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("password", (mPassword))
                        .appendQueryParameter("id", String.valueOf(id))
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


                    this.result_string = String.valueOf(result);
                    System.out.println("Result = " + result);

                    JSONObject user = new JSONObject(String.valueOf(result));
                    succes = user.getBoolean("success");

                    SharedPreferences.Editor editor = getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit();
                    editor.putString("password", user.getString("password"));

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

            if (success) {

                SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
                int typeProfil = prefs.getInt("type_profil", 0);

                System.out.println(typeProfil);
                if(typeProfil == 2){
                    Toast.makeText(getBaseContext(), "Mot de passe modifié avec succès!", Toast.LENGTH_SHORT).show();
                    System.out.println("Directeur magasin");
                    Intent intent_dir = new Intent(SettingDirNewPasswd.this, SettingDir.class);
                    startActivity(intent_dir);

                }else if(typeProfil == 1){

                    Intent intent_resp = new Intent(SettingDirNewPasswd.this, MainActivityResp.class);
                    intent_resp.putExtra("user_info", this.result_string);
                    startActivity(intent_resp);
                    System.out.println("Responsable region");

                }else{
                    mPasswordView1.setError(getString(R.string.error_invalid_profil));
                    mPasswordView1.requestFocus();
                }

            } else {
                System.out.println("Error...");
                mPasswordView1.setError("An error occured...");
                mPasswordView1.requestFocus();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }
}

package org.mydarties;

import android.content.Intent;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mydarties.dir.HomeDir;
import org.mydarties.resp.HomeResp;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class LoginTest {

    private LoginActivity loginActivity;

    private AutoCompleteTextView email;
    private EditText password;
    private Button login;

    @Before
    public void setup(){
       // loginActivity = Robolectric.setupActivity(LoginActivity.class);
        loginActivity = Robolectric.buildActivity(LoginActivity.class).create().get();
        email = (AutoCompleteTextView) loginActivity.findViewById(R.id.email);
        password = (EditText) loginActivity.findViewById(R.id.password);
        login = (Button) loginActivity.findViewById(R.id.email_sign_in_button);
    }

    @Test
    public void shouldBeWrongPassword(){
        /**
         * Test wrong password
         * assert that the activity hasn't changed
         * A successful login will switch to another activity
         */
        email.setText("javier.audibert@darties.com");
        password.setText("wrong_password");

        login.performClick();

        Intent expectedIntent = new Intent(loginActivity, LoginActivity.class);
        assertEquals(expectedIntent, shadowOf(loginActivity).getNextStartedActivity());
    }

    @Test
    public void shouldBeWrongProfile(){
        /**
         * Test wrong profile
         * Connect with the administrator, he shouldn't be allowed on the app
         */
        email.setText("javier.audibert@darties.com");
        password.setText("4AfgG97");

        login.performClick();

        Intent expectedIntent = new Intent(loginActivity, LoginActivity.class);
        assertEquals(expectedIntent, shadowOf(loginActivity).getNextStartedActivity());
    }

    @Test
    public void shouldBeRegionManager(){
        /**
         * Test successful login as region manager
         * Should switch activity to MainActivityResp
         */
        email.setText("angelique.delaunay@darties.com");
        password.setText("g6Gb25T");

        login.performClick();

        Intent expectedIntent = new Intent(loginActivity, HomeResp.class);
        assertEquals(expectedIntent, shadowOf(loginActivity).getNextStartedActivity());
    }

    @Test
    public void shouldBeStoreDirector(){
        /**
         * Test successful login as store director
         * Should switch activity to MainActivityDir
         */
        email.setText("estelle.bernier@darties.com");
        password.setText("CJ62hy9");

        login.performClick();

        Intent expectedIntent = new Intent(loginActivity, HomeDir.class);
        assertEquals(expectedIntent, shadowOf(loginActivity).getNextStartedActivity());
    }
}

package com.example.q.simday.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.q.simday.Fragments.ContactFragment;
import com.example.q.simday.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;


public class LoginActivity extends Activity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private String master;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult result) {
                GraphRequest request;
                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        if (response.getError() != null) {
                        } else {
                            Log.i("TAG", "user: " + user.toString());
                            try {
                                Log.i("TAG", "user: " + user.get("name"));
                                master = (String) user.get("name");
                                Log.i("master","00@@@@@"+master);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());
                            Log.i("master","000!!!"+master);

                            Intent master2 = new Intent(LoginActivity.this, MainActivity.class);
                            master2.putExtra("master",master);
                            Log.i("master","0000"+master);


                            Bundle bundle = new Bundle();
                            bundle.putString("master", master);
                            ContactFragment contactfragment = new ContactFragment();
                            android.app.FragmentManager fm = getFragmentManager();
                            android.app.FragmentTransaction ft = fm.beginTransaction();
                            ft.commit();
                            contactfragment.setArguments(bundle);


                            setResult(RESULT_OK);
                            //startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

                Intent  loadIntent = new Intent(LoginActivity.this, LoadingActivity.class);
                startActivity(loadIntent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }


            @Override
            public void onCancel() {
                finish();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("test", "Error: " + error);
                finish();
            }
        });
    }

    public String getMyData() {
        Log.i("master","1111"+master);
        return master;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

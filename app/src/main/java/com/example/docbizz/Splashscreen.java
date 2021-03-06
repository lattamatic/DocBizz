package com.example.docbizz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gcm.GCMRegistrar;
import util.ServiceHandler;
import util.data;


public class Splashscreen extends ActionBarActivity {

    String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            GCMRegistrar.register(this, data.SENDER_ID);
        } else {
            Log.v("Reg id", regId);

        }

        SharedPreferences sharedPreferences = getSharedPreferences("DocBizz", MODE_PRIVATE);
        String strUser = sharedPreferences.getString("user","");
        if(!strUser.equals("")) {
            try {
                JSONObject userObj = new JSONObject(strUser);
                email = userObj.getString("email");
                password = userObj.getString("password");

                new LoginTask().execute(email, password);
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Sorry. An unknown error occurred.", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Intent intent = new Intent(Splashscreen.this, Home.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        GCMRegistrar.onDestroy(this);
    }

    public class LoginTask extends AsyncTask<String,Void,String>{

        String email, password;

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler requestMakerLogin = new ServiceHandler();

            email = params[0];
            password = params[1];

            List<NameValuePair> paramsLogin = new ArrayList<NameValuePair>();
            paramsLogin.add(new BasicNameValuePair("email", email));
            paramsLogin.add(new BasicNameValuePair("password", password));

            String responseLogin = requestMakerLogin.makeServiceCall(data.urlLogin, ServiceHandler.POST, paramsLogin);
            JSONObject responseLoginObject;

            if(responseLogin!=null) {
                try {
                    responseLoginObject = new JSONObject(responseLogin);

                    SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("DocBizz", MODE_PRIVATE).edit();
                    try {
                        sharedPreferencesEditor.putString("user", responseLoginObject.getJSONObject("data").toString());
                        sharedPreferencesEditor.putString("id", responseLoginObject.getJSONObject("data").getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sharedPreferencesEditor.commit();

                    Intent intent = new Intent(Splashscreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}

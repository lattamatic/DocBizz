package com.example.docbizz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import util.ServiceHandler;
import util.data;


public class Home extends ActionBarActivity {

    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnRegister = (Button) findViewById(R.id.btnHomeRegister);
        Button btnLogin = (Button) findViewById(R.id.btnHomeLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1 == 0) {
                    Toast.makeText(getApplicationContext(), "Incorrect Email/Password", Toast.LENGTH_SHORT).show();
                }
                else if(msg.arg1 == 1) {
                    Toast.makeText(getApplicationContext(), "Sorry. An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = Home.this;
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                final View loginView = layoutInflater.inflate(R.layout.dialog_login, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(loginView);
                alertDialogBuilder.setCancelable(true);

                Button btnLoginD = (Button) loginView.findViewById(R.id.btnLogin);
                btnLoginD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ServiceHandler requestMaker = new ServiceHandler();

                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                        final EditText editTextEmail = (EditText) loginView.findViewById(R.id.editText1);
                        final EditText editTextPassword = (EditText) loginView.findViewById(R.id.editText2);

                        new LoginTask().execute(editTextEmail.getText().toString(),editTextPassword.getText().toString());

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class LoginTask extends AsyncTask<String,Void,String> {

        String email, password;

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler requestMakerLogin = new ServiceHandler();

            email = params[0];
            password = params[1];

            List<NameValuePair> paramsLogin = new ArrayList<NameValuePair>();
            paramsLogin.add(new BasicNameValuePair("email", email));
            paramsLogin.add(new BasicNameValuePair("password", password));

            String response = requestMakerLogin.makeServiceCall(data.urlLogin, ServiceHandler.POST, paramsLogin);
            try {
                JSONObject responseObject = new JSONObject(response);

                if (responseObject.getBoolean("result")) {
                    SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("DocBizz", MODE_PRIVATE).edit();
                    sharedPreferencesEditor.putString("user", responseObject.getJSONObject("data").toString());
                    sharedPreferencesEditor.commit();

                    Intent intent = new Intent(Home.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Message msg = new Message();
                    msg.arg1 = 0;
                    mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.arg1 = 1;
                mHandler.sendMessage(msg);
            }

            return null;
        }
    }
}

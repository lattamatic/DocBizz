package com.example.docbizz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import util.ServiceHandler;
import util.data;


public class Home extends ActionBarActivity {

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getApplicationContext();
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View loginView = layoutInflater.inflate(R.layout.dialog_login, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(loginView);

                final EditText editTextEmail = (EditText) loginView.findViewById(R.id.editTextEmail);
                final EditText editTextPassword = (EditText) loginView.findViewById(R.id.editTextPassword);

                alertDialogBuilder.setCancelable(true).setNeutralButton(R.id.btnLogin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ServiceHandler requestMaker = new ServiceHandler();

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("email", editTextEmail.getText().toString()));
                        params.add(new BasicNameValuePair("password", editTextPassword.getText().toString()));

                        String response = requestMaker.makeServiceCall(data.urlLogin, ServiceHandler.POST, params);

                        try {
                            JSONObject responseObject = new JSONObject(response);

                            if (responseObject.getBoolean("result")) {
                                SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("DocBizz", MODE_PRIVATE).edit();
                                sharedPreferencesEditor.putString("user", responseObject.getJSONObject("data").toString());
                                sharedPreferencesEditor.commit();

                                Intent intent = new Intent(Home.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Incorrect Email/Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Sorry. An unknown error occurred.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
}

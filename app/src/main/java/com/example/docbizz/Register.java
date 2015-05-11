package com.example.docbizz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import util.ServiceHandler;
import util.data;


public class Register extends ActionBarActivity {

    private static EditText editTextFullName, editTextEmail, editTextPhone, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Spinner spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapterCity);

        final Spinner spinnerHospital = (Spinner) findViewById(R.id.spinnerHospital);
        ArrayAdapter<CharSequence> adapterHospital = ArrayAdapter.createFromResource(this,
                R.array.hospitals_array,android.R.layout.simple_spinner_item);
        adapterHospital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospital.setAdapter(adapterHospital);

        final Spinner spinnerSpeciality = (Spinner) findViewById(R.id.spinnerSpeciality);
        ArrayAdapter<CharSequence> adapterSpeciality = ArrayAdapter.createFromResource(this,
                R.array.hospitals_array,android.R.layout.simple_spinner_item);
        adapterSpeciality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpeciality.setAdapter(adapterSpeciality);

        editTextFullName = (EditText) findViewById(R.id.editTextFullName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RegisterTask().execute(editTextFullName.getText().toString(),editTextEmail.getText().toString()
                        ,editTextPhone.getText().toString(),editTextPassword.getText().toString(),spinnerSpeciality.getSelectedItem().toString(),
                        spinnerCity.getSelectedItem().toString(),spinnerHospital.getSelectedItem().toString());

            }
        });
    }

    public class RegisterTask extends AsyncTask<String,Void,String>{

        String name, email, phone, password, spec, city, hospital;

        @Override
        protected String doInBackground(String... params) {

            name = params[0];
            email = params[1];
            phone = params[2];
            password = params[3];
            spec = params[4];
            city = params[5];
            hospital = params[6];

            List<NameValuePair> paramsRegister = new ArrayList<NameValuePair>();
            paramsRegister.add(new BasicNameValuePair("name", name));
            paramsRegister.add(new BasicNameValuePair("email", email));
            paramsRegister.add(new BasicNameValuePair("phone", phone));
            paramsRegister.add(new BasicNameValuePair("password", password));
            paramsRegister.add(new BasicNameValuePair("spec", spec));
            paramsRegister.add(new BasicNameValuePair("city", city));
            paramsRegister.add(new BasicNameValuePair("hospital", hospital));

            ServiceHandler requestMaker = new ServiceHandler();

            String response = requestMaker.makeServiceCall(data.urlRegister, ServiceHandler.POST, paramsRegister);

            try {
                JSONObject responseObject = new JSONObject(response);
                if(responseObject.getBoolean("result")) {

                    new LoginTask().execute(editTextEmail.getText().toString(),editTextPassword.getText().toString());
                }
                else {
                    Toast.makeText(getApplicationContext(), "Sorry. An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Sorry. An unknown error occurred.", Toast.LENGTH_SHORT).show();
            }

            return null;
        }
    }

    public class LoginTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler requestMakerLogin = new ServiceHandler();

            List<NameValuePair> paramsLogin = new ArrayList<NameValuePair>();
            paramsLogin.add(new BasicNameValuePair("email", editTextEmail.getText().toString()));
            paramsLogin.add(new BasicNameValuePair("password", editTextPassword.getText().toString()));

            String responseLogin = requestMakerLogin.makeServiceCall(data.urlLogin, ServiceHandler.POST, paramsLogin);
            JSONObject responseLoginObject = null;
            try {
                responseLoginObject = new JSONObject(responseLogin);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("DocBizz", MODE_PRIVATE).edit();
            try {
                sharedPreferencesEditor.putString("user", responseLoginObject.getJSONObject("data").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sharedPreferencesEditor.commit();

            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
            finish();

            return null;
        }
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

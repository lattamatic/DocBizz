package com.example.docbizz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

import gcm.GCMRegistrar;
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
                R.array.specialities_array,android.R.layout.simple_spinner_item);
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
        boolean registerSuccess = false;

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
            paramsRegister.add(new BasicNameValuePair("gcm_id", GCMRegistrar.getRegistrationId(getApplicationContext())));

            Log.i("gcm_id", GCMRegistrar.getRegistrationId(getApplicationContext()));

            ServiceHandler requestMaker = new ServiceHandler();

            String response = requestMaker.makeServiceCall(data.urlRegister, ServiceHandler.POST, paramsRegister);

            try {
                JSONObject responseObject = new JSONObject(response);
                if(responseObject.getBoolean("result")) {
                    registerSuccess = true;
                    new LoginTask().execute(editTextEmail.getText().toString(),editTextPassword.getText().toString());
                }
                else {
                    registerSuccess = false;
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                registerSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String res){
            super.onPostExecute(res);

            if(!registerSuccess){
                Toast.makeText(getApplicationContext(), "Sorry. An unknown error occurred.", Toast.LENGTH_SHORT).show();
            }
        }
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
            try {
               JSONObject responseLoginObject = new JSONObject(responseLogin);

                SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("DocBizz", MODE_PRIVATE).edit();
                try {
                    sharedPreferencesEditor.putString("user", responseLoginObject.getJSONObject("data").toString());
                    sharedPreferencesEditor.putString("id", responseLoginObject.getJSONObject("data").getString("id"));
                    MainActivity.doctorID = responseLoginObject.getJSONObject("data").getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sharedPreferencesEditor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
            finish();

            return null;
        }
    }

}

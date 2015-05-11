package com.example.docbizz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import util.ServiceHandler;
import util.data;


public class Register extends ActionBarActivity {

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

        final EditText editTextFullName = (EditText) findViewById(R.id.editTextFullName);
        final EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        final EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        final EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceHandler requestMaker = new ServiceHandler();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", editTextFullName.getText().toString()));
                params.add(new BasicNameValuePair("email", editTextEmail.getText().toString()));
                params.add(new BasicNameValuePair("phone", editTextPhone.getText().toString()));
                params.add(new BasicNameValuePair("password", editTextPassword.getText().toString()));
                params.add(new BasicNameValuePair("spec", spinnerSpeciality.getSelectedItem().toString()));
                params.add(new BasicNameValuePair("city", spinnerCity.getSelectedItem().toString()));
                params.add(new BasicNameValuePair("hospital", spinnerHospital.getSelectedItem().toString()));

                String response = requestMaker.makeServiceCall(data.urlRegister, ServiceHandler.POST, params);

                try {
                    JSONObject responseObject = new JSONObject(response);
                    if(responseObject.getBoolean("result")) {
                        ServiceHandler requestMakerLogin = new ServiceHandler();

                        List<NameValuePair> paramsLogin = new ArrayList<NameValuePair>();
                        paramsLogin.add(new BasicNameValuePair("email", editTextEmail.getText().toString()));
                        paramsLogin.add(new BasicNameValuePair("password", editTextPassword.getText().toString()));

                        String responseLogin = requestMakerLogin.makeServiceCall(data.urlLogin, ServiceHandler.POST, paramsLogin);
                        JSONObject responseLoginObject = new JSONObject(responseLogin);

                        SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("DocBizz", MODE_PRIVATE).edit();
                        sharedPreferencesEditor.putString("user", responseLoginObject.getJSONObject("data").toString());
                        sharedPreferencesEditor.commit();

                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry. An unknown error occurred.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Sorry. An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
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

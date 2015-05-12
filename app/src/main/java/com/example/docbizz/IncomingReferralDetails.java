package com.example.docbizz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import util.ServiceHandler;
import util.data;


public class IncomingReferralDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_referral_details);

        ImageView btnApproveReferral = (ImageView) findViewById(R.id.btnApproveReferral);
        ImageView btnDeclineReferral = (ImageView) findViewById(R.id.btnDeclineReferral);

        btnApproveReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");

                new ApproveDeclineReferral().execute("","Approved"); //TODO edit the arguments. Also check bottom most TODOs to decide if this is needed
            }
        });

        btnDeclineReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ApproveDeclineReferral().execute("","Declined"); //TODO edit the arguments
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_incoming_referral_details, menu);
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
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = 1994;
            int month = 0;
            int day = 1;

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //TODO : Make a backend request with the year, month and day..

            //TODO : Sandeep said the above TODO is not needed as it is going to be done in some other way
        }

    }

    public static class ApproveDeclineReferral extends AsyncTask<String,Void,String> {

        String Id, status;

        @Override
        protected String doInBackground(String... params) {

            Id = params[0];
            status = params[1];

            List<NameValuePair> paramsList = new ArrayList<>();

            paramsList.add(new BasicNameValuePair("ref_id",Id));
            paramsList.add(new BasicNameValuePair("status",status));

            ServiceHandler requestMaker = new ServiceHandler();

            String response = requestMaker.makeServiceCall(data.urlApproveDeclineReferral, ServiceHandler.POST, paramsList);

            try {
                JSONObject approveDeclineJSON = new JSONObject(response);
                Log.d("approveDeclineJSON", String.valueOf(approveDeclineJSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

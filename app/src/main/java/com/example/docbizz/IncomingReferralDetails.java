package com.example.docbizz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import util.ServiceHandler;
import util.data;


public class IncomingReferralDetails extends ActionBarActivity {

    public static android.os.Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_referral_details);

        ImageView btnApproveReferral = (ImageView) findViewById(R.id.btnApproveReferral);
        ImageView btnDeclineReferral = (ImageView) findViewById(R.id.btnDeclineReferral);

        final Intent intent = getIntent();
        String referralId = intent.getStringExtra("referralID");
        Log.i("ref_id", referralId);
        final int index = intent.getIntExtra("index", 1);
        Log.i("index", index + "");
        if(referralId!=null) {
            new GetReferralDetails().execute(referralId);
        }

        final String fromDoctor = MainActivity.inboxItemsList.get(index).doctorName;

        btnApproveReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogFragment newFragment = new DatePickerFragment();
                //newFragment.show(getSupportFragmentManager(), "datePicker");
                new ApproveDeclineReferral().execute("","Approved"); //TODO edit the arguments. Also check bottom most TODOs to decide if this is needed
            }
        });

        btnDeclineReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ApproveDeclineReferral().execute("","Declined"); //TODO edit the arguments
            }
        });

        mHandler = new android.os.Handler() {
            @Override public void handleMessage(Message msg) {
                if(msg.obj!=null) {
                    try {
                        JSONObject responseObj = new JSONObject(msg.obj.toString());
                        JSONObject detailsObj = responseObj.getJSONArray("details").getJSONObject(0);
                        JSONArray commentsArr = responseObj.getJSONArray("comments");

                        TextView txtFromRecipient = (TextView) findViewById(R.id.txtFromRecipient);
                        TextView txtPatientName = (TextView) findViewById(R.id.txtPatientName);
                        TextView txtPatientPhoneNo = (TextView) findViewById(R.id.txtPatientPhoneNo);
                        TextView txtPatientReason = (TextView) findViewById(R.id.txtPatientReason);

                        txtPatientName.setText(detailsObj.getString("name"));
                        txtPatientPhoneNo.setText(detailsObj.getString("mbl"));
                        txtPatientReason.setText(detailsObj.getString("reason"));

                        txtFromRecipient.setText(fromDoctor);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                    if(msg.arg1 == 0) {
                        Toast.makeText(getApplicationContext(), "The referral has been approved.", Toast.LENGTH_SHORT).show();
                    }
                    else if(msg.arg1 == 1) {
                        Toast.makeText(getApplicationContext(), "The referral has been declined.", Toast.LENGTH_SHORT).show();
                    }


            }
        };
    }
    public static class GetReferralDetails extends AsyncTask<String,Void,String> {

        String referralId;

        @Override
        protected String doInBackground(String... params) {

            referralId = params[0];

            List<NameValuePair> paramsList = new ArrayList<>();
            paramsList.add(new BasicNameValuePair("ref_id",referralId));

            ServiceHandler requestMaker = new ServiceHandler();

            String response = requestMaker.makeServiceCall(data.urlReferralDetails, ServiceHandler.POST, paramsList);

            Message msg = new Message();
            msg.obj = response;
            msg.arg1 = -1;
            mHandler.sendMessage(msg);

            return null;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
            Log.i("status", data.getFlagFromStatus(status) + "");
            paramsList.add(new BasicNameValuePair("status",data.getFlagFromStatus(status)+""));

            ServiceHandler requestMaker = new ServiceHandler();

            String response = requestMaker.makeServiceCall(data.urlApproveDeclineReferral, ServiceHandler.POST, paramsList);

            try {
                JSONObject approveDeclineJSON = new JSONObject(response);
                Log.d("approveDeclineJSON", String.valueOf(approveDeclineJSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(status.equals("Approved")) {
                Message msg = new Message();
                msg.arg1 = 0;
                mHandler.sendMessage(msg);
            }
            else if(status.equals("Declined")){
                Message msg = new Message();
                msg.arg1 = 1;
                mHandler.sendMessage(msg);
            }
            return null;
        }
    }
}

package com.example.docbizz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import referrals.InboxReferralItem;
import referrals.IncomingReferralRecyclerViewAdapter;
import referrals.SentReferralItem;
import referrals.SentReferralRecyclerViewAdapter;
import reports.ReportItem;
import util.ServiceHandler;
import util.data;


public class SentReferralDetails extends ActionBarActivity {

    public static android.os.Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_referral_details);

        final Intent intent = getIntent();
        String referralId = intent.getStringExtra("referralID");
        int index = intent.getIntExtra("index", 1);
        Log.i("index", index + "");
        if(referralId!=null) {
            new GetReferralDetails().execute(referralId);
        }

        final String toDoctor = MainActivity.sentItemsList.get(index).doctorName;

        mHandler = new android.os.Handler() {
            @Override public void handleMessage(Message msg) {
                try {
                    JSONObject responseObj = new JSONObject(msg.obj.toString());
                    JSONObject detailsObj = responseObj.getJSONArray("details").getJSONObject(0);
                    JSONArray commentsArr = responseObj.getJSONArray("comments");

                    TextView txtToRecipient = (TextView) findViewById(R.id.txtToRecipient);
                    TextView txtPatientName = (TextView) findViewById(R.id.txtPatientName);
                    TextView txtPatientPhoneNo = (TextView) findViewById(R.id.txtPatientPhoneNo);
                    TextView txtPatientReason = (TextView) findViewById(R.id.txtPatientReason);
                    TextView txtReferralStatus = (TextView) findViewById(R.id.txtReferralStatus);

                    txtPatientName.setText(detailsObj.getString("name"));
                    txtPatientPhoneNo.setText(detailsObj.getString("mbl"));
                    txtPatientReason.setText(detailsObj.getString("reason"));
                    txtReferralStatus.setText(data.getStatusFromFlag(detailsObj.getInt("status")));

                    txtToRecipient.setText(toDoctor);

                }
                catch(Exception e) {
                    e.printStackTrace();
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
            mHandler.sendMessage(msg);

            return null;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

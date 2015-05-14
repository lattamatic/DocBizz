package com.example.docbizz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import contacts.ContactItem;
import contacts.ContactRecyclerViewAdapter;
import navigationDrawer.NavDrawerItem;
import navigationDrawer.NavDrawerListAdapter;
import referrals.InboxReferralItem;
import referrals.IncomingReferralRecyclerViewAdapter;
import referrals.ReferralItem;
import referrals.SentReferralItem;
import referrals.SentReferralRecyclerViewAdapter;
import referrals.ViewPagerAdapter;
import reports.ReportItem;
import reports.ReportRecyclerViewAdapter;
import util.ServiceHandler;
import util.data;



public class MainActivity extends ActionBarActivity {


    public static String doctorID;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    public static ViewPagerAdapter customAdapter;
    public static IncomingReferralRecyclerViewAdapter inboxAdapter;
    public static SentReferralRecyclerViewAdapter sentAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    public static ProgressDialog loadInboxProgress, loadSentProgress;
    public static ArrayList<String> contactsIDs, contactsName, contactsEmail, contactsPhone, contactsSpeciality, contactsCity, contactsHospital;
    public static ArrayList<ContactItem> contactItemArrayList;
    public static ArrayList<ReportItem> reportItemArrayList;
    public static ArrayList<ReferralItem> inboxItemsList, sentItemsList;
    public static ArrayList<String> reportsName, reportsSpeciality;
    public static ArrayList<Integer> reportsSentApproved, reportsSentDeclined, reportsReceivedApproved, reportsReceivedDeclined;
    public static ArrayList<String> inboxIDs, inboxName, inboxSenderID, inboxReason;
    public static ArrayList<String> sentIDs, sentReceiverIDs, sentStatus, sentName;
    public String inviteName, invitePhone;
    final int CONTACT_KEY = 1;
    int currentFragment = 0;
    String inviteMessage1 = "Hello ", inviteMessage2 = ", try out this cool app ", inviteMessage3 = ""; //TODO inviteMessage3 contains playstore URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadInboxProgress = new ProgressDialog(MainActivity.this);
        loadSentProgress = new ProgressDialog(MainActivity.this);

        setTitle("Referrals");

        List<NavDrawerItem> list = data.getNavDrawerItems();
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_container, new ReferralsFragment());
        fragmentTransaction.commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.list_slidermenu);

        drawerList.setAdapter(new NavDrawerListAdapter(getApplicationContext(), list));
        // Set the list's click listener
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_drawer);
        mDrawerToggle.syncState();

        contactItemArrayList = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("DocBizz", MODE_PRIVATE);
        String id = sharedPreferences.getString("id","");
        String strUser = sharedPreferences.getString("user", "{}");

        TextView profilepic_name = (TextView) findViewById(R.id.profilepic_name);

        try {
            JSONObject userObj = new JSONObject(strUser);
            profilepic_name.setText(userObj.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new GetContactsList().execute(id, "Main");

        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();

    }

    private void selectItem(int position) {

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Fragment f = fragmentManager.findFragmentById(R.id.frame_container);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(0,0,0);
        getSupportFragmentManager().findFragmentById(R.id.frame_container)
                .getView()
                .setLayoutParams(lp);
        setTitle(data.navtitles[position].getTitle());

        switch (position) {
            case 0 :
                fragmentTransaction.replace(R.id.frame_container, new SendReferralsFragment());
                currentFragment = 0;
                break;

            case 1 :
                fragmentTransaction.replace(R.id.frame_container, new ReferralsFragment());
                currentFragment = 1;
                break;

            case 2 :
                fragmentTransaction.replace(R.id.frame_container, new ReportFragment());
                currentFragment = 2;
                break;

            case 3 :
                fragmentTransaction.replace(R.id.frame_container, new ContactsFragment());
                currentFragment = 3;
                break;

            case 4:
                Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactIntent,CONTACT_KEY);
                break;

            case 5 :
                fragmentTransaction.replace(R.id.frame_container, new HelpFragment());
                currentFragment = 5;
                break;
            case 6 :
                SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("DocBizz", MODE_PRIVATE).edit();
                sharedPreferencesEditor.remove("user");
                sharedPreferencesEditor.remove("id");
                sharedPreferencesEditor.commit();

                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "You have been logged out successfully", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        fragmentTransaction.commit();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            System.out.println(position);
            selectItem(position);
            drawerList.setItemChecked(position, true);
            drawerLayout.closeDrawer(Gravity.START);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (CONTACT_KEY) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    ContentResolver cr = getContentResolver();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        inviteName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        if (Integer.parseInt(c.getString(
                                c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                invitePhone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            pCur.close();
                        }
                        Log.i("inviteName",inviteName);
                        Log.i("invitePhone",invitePhone);
                    }

                    if(invitePhone!=null&&inviteName!=null) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Do you wish to invite "+inviteName+" ?");
                        alert.setMessage("You will incur message charges as per your plan");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendSMS(invitePhone, inviteMessage1 + inviteName + inviteMessage2 + inviteMessage3);
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                dialog.dismiss();
                            }
                        });
                        alert.create().show();
                    }
                    selectItem(currentFragment);

                }
                break;
        }
    }

    public void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
*/
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SendReferralsFragment extends Fragment {

        public SendReferralsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_sendreferrals, container, false);

            final EditText editPatientName = (EditText) rootView.findViewById(R.id.editPatientName);
            final EditText editPatientContactNumber = (EditText) rootView.findViewById(R.id.editPatientContactNumber);
            final EditText editPatientReason = (EditText) rootView.findViewById(R.id.editPatientReason);
            final EditText editPatientMessage = (EditText) rootView.findViewById(R.id.editPatientMessage);

            final AutoCompleteTextView autoCompleteTextViewToDoctor = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextViewToDoctor);

            final ArrayAdapter<CharSequence> adapterContactsList = new ArrayAdapter<CharSequence>(rootView.getContext(), android.R.layout.simple_list_item_1, contactsName.toArray(new String[contactsName.size()]));
            autoCompleteTextViewToDoctor.setAdapter(adapterContactsList);

            SharedPreferences sharedPreferences = rootView.getContext().getSharedPreferences("DocBizz", MODE_PRIVATE);
            final String id = sharedPreferences.getString("id", "");

            Button btnSendReferral = (Button) rootView.findViewById(R.id.btnSendReferral);
            btnSendReferral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String selectedDoctor = autoCompleteTextViewToDoctor.getText().toString();
                    ContactItem item = contactItemArrayList.get( contactsName.indexOf(selectedDoctor));

                    Toast.makeText(rootView.getContext(), item.doctorName + " " + item.id + " " + contactsName.indexOf(selectedDoctor), Toast.LENGTH_SHORT).show();
                    new SendReferral().execute(id,item.id,editPatientName.getText().toString(),editPatientContactNumber.getText().toString(),
                            editPatientReason.getText().toString(),editPatientMessage.getText().toString());


                }
            });

            return rootView;
        }
    }
    public static class HelpFragment extends Fragment {

        public HelpFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_help, container, false);

            TextView txtHelp = (TextView) rootView.findViewById(R.id.txtHelp);

            txtHelp.setText("Send referral : Allows you to send referrals to other doctors using this app.\n" +
                    "\nIf the doctor to whom you want to send referral is not using this app, just go to Contacts(on Home Page) then click on Invite.\n" +
                    "You can invite using whatsapp, gmail,facebook etc." +
                    "\nA pre feeded message will go to doctors whom you want to invite and they can easily install the app.\n" +
                    "\n" +
                    "Referrals(on Home page) :  Allows you to see your inbox and referrals sent.\nJust click on any referral in your inbox and you can approve or decline the referral. You can also reply back\n" +
                    "\n" +
                    "Reports(on Home Page) : Generates a report automatically of your referrals sent, received, approved and declined.");

            return rootView;
        }
    }
    public static class ReportFragment extends Fragment {

        static android.os.Handler mHandler;

        public ReportFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_report, container, false);

            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewReports);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());

            final int rLimit = 10;
            final int rOffset = 0;

            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);

            reportItemArrayList = new ArrayList<>();

            SharedPreferences sharedPreferences = rootView.getContext().getSharedPreferences("DocBizz", MODE_PRIVATE);
            String id = sharedPreferences.getString("id", "");
            new GetReports().execute(id);

            final ReportRecyclerViewAdapter adapter = new ReportRecyclerViewAdapter(reportItemArrayList, rootView.getContext());
            recyclerView.setAdapter(adapter);

            mHandler = new android.os.Handler() {
                @Override public void handleMessage(Message msg) {
                    adapter.notifyDataSetChanged();
                }
            };

            return rootView;
        }
    }

    public static class ContactsFragment extends Fragment {

        static android.os.Handler mHandler;

        public ContactsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewContacts);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());

            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);

            contactItemArrayList = new ArrayList<>();

            SharedPreferences sharedPreferences = rootView.getContext().getSharedPreferences("DocBizz", MODE_PRIVATE);
            String id = sharedPreferences.getString("id", "");

            final ContactRecyclerViewAdapter adapter = new ContactRecyclerViewAdapter(contactItemArrayList, rootView.getContext());
            recyclerView.setAdapter(adapter);

            new GetContactsList().execute(id, "Contacts");

            mHandler = new android.os.Handler() {
                @Override public void handleMessage(Message msg) {
                    adapter.notifyDataSetChanged();
                }
            };

            return rootView;
        }
    }

    public static class ReferralsFragment extends Fragment {

        ViewPager mViewPager;
        static Handler mHandler;

        public ReferralsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_referrals, container, false);

            inboxItemsList = new ArrayList<>();
            sentItemsList = new ArrayList<>();

            mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
            customAdapter = new ViewPagerAdapter(getActivity().getApplicationContext(),getActivity().getSupportFragmentManager());

            mViewPager.setAdapter(customAdapter);
            mViewPager.setCurrentItem(0);

            SharedPreferences sharedPreferences = rootView.getContext().getSharedPreferences("DocBizz", MODE_PRIVATE);
            final String id = sharedPreferences.getString("id", "");

            /*RecyclerView recyclerViewInbox = (RecyclerView) rootView.findViewById(R.id.recyclerViewReferralsInbox);
            final LinearLayoutManager layoutManagerInbox = new LinearLayoutManager(rootView.getContext());

            layoutManagerInbox.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerViewInbox.setLayoutManager(layoutManagerInbox);
            recyclerViewInbox.setItemAnimator(new DefaultItemAnimator());
            recyclerViewInbox.setHasFixedSize(true);

            RecyclerView recyclerViewSent = (RecyclerView) rootView.findViewById(R.id.recyclerViewReferralsInbox);
            final LinearLayoutManager layoutManagerSent = new LinearLayoutManager(rootView.getContext());

            layoutManagerSent.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerViewSent.setLayoutManager(layoutManagerSent);
            recyclerViewSent.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSent.setHasFixedSize(true);*/

            /*final ReferralRecyclerViewAdapter inboxAdapter = new ReferralRecyclerViewAdapter(inboxItemsList, rootView.getContext());
            recyclerViewInbox.setAdapter(inboxAdapter);

            final ReferralRecyclerViewAdapter outboxAdapter = new ReferralRecyclerViewAdapter(sentItemsList, rootView.getContext());
            recyclerViewSent.setAdapter(outboxAdapter);*/

            new LoadInbox().execute(id, String.valueOf(0), String.valueOf(10));

            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mViewPager.setCurrentItem(position);
                    if(position==0){
                        new LoadInbox().execute(id,String.valueOf(0),String.valueOf(10));
                    }
                    else if(position==1){
                        new LoadSent().execute(id, String.valueOf(0), String.valueOf(10));
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            mHandler = new android.os.Handler() {
                @Override public void handleMessage(Message msg) {
                    if(msg.arg1 == 0) {
                        Log.i("inbox", "" + inboxItemsList.size());
                        inboxAdapter = null;
                        inboxAdapter = new IncomingReferralRecyclerViewAdapter(inboxItemsList, rootView.getContext());
                        InboxReferralItem.recyclerViewInbox.setAdapter(inboxAdapter);
                        inboxAdapter.notifyDataSetChanged();
                    }
                    else if(msg.arg1 == 1) {
                        Log.i("sent", "" + sentItemsList.size());
                        sentAdapter = null;
                        sentAdapter = new SentReferralRecyclerViewAdapter(sentItemsList, rootView.getContext());
                        SentReferralItem.recyclerViewSent.setAdapter(sentAdapter);
                        sentAdapter.notifyDataSetChanged();
                    }
                }
            };

            return rootView;
        }
    }

    public static class GetContactsList extends AsyncTask<String,Void,String>{

        String userId;

        @Override
        protected String doInBackground(String... params) {

            userId = params[0];

            List<NameValuePair> paramsList = new ArrayList<>();
            paramsList.add(new BasicNameValuePair("doc",userId));

            ServiceHandler requestMaker = new ServiceHandler();

            String response = requestMaker.makeServiceCall(data.urlContacts, ServiceHandler.POST, paramsList);

            try {
                JSONObject contactsJSOn = new JSONObject(response);

                if(contactsJSOn!=null){
                    JSONArray contactsArray = new JSONArray(contactsJSOn.getString("contacts"));

                    contactsName = new ArrayList<>();
                    contactsIDs = new ArrayList<>();
                    contactsEmail = new ArrayList<>();
                    contactsPhone = new ArrayList<>();
                    contactsSpeciality = new ArrayList<>();
                    contactsCity = new ArrayList<>();
                    contactsHospital = new ArrayList<>();

                    for(int i=0; i < contactsArray.length();i++){
                        JSONObject tempJSON = new JSONObject();
                        tempJSON = contactsArray.getJSONObject(i);
                        contactsIDs.add(i, tempJSON.getString("id"));
                        contactsName.add(i, tempJSON.getString("name"));
                        contactsEmail.add(i, tempJSON.getString("email"));
                        contactsPhone.add(i, tempJSON.getString("mobile"));
                        contactsSpeciality.add(i, tempJSON.getString("speciality"));
                        contactsCity.add(i, tempJSON.getString("city"));
                        contactsHospital.add(i, tempJSON.getString("hospital"));

                        ContactItem tempContact = new ContactItem(contactsIDs.get(i),"",contactsName.get(i),contactsPhone.get(i),contactsEmail.get(i),contactsHospital.get(i),contactsCity.get(i),contactsSpeciality.get(i));
                        //TODO change this to add imageURL

                        contactItemArrayList.add(i, tempContact);
                    }
                    Log.i("str", "sr");

                    if(params[1].equals("Contacts")) {
                        ContactsFragment.mHandler.sendEmptyMessage(1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class LoadInbox extends AsyncTask<String,Void,String>{

        String userId, response;
        int start, end;
        boolean loadInboxSuccess = false;

        @Override
        protected void onPreExecute(){
            loadInboxProgress.setMessage("Loading Inbox...");
            loadInboxProgress.show();

            Log.i("Inside","LoadInbox");
        }

        @Override
        protected String doInBackground(String... params) {

            userId = params[0];
            start = Integer.parseInt(params[1]);
            end = Integer.parseInt(params[2]);

            List<NameValuePair> paramsList = new ArrayList<>();
            paramsList.add(new BasicNameValuePair("receiver",userId));

            ServiceHandler requestMaker = new ServiceHandler();

            response = requestMaker.makeServiceCall(data.urlInbox, ServiceHandler.POST, paramsList);

            try {
                JSONObject inboxJSON = new JSONObject(response);

                if(inboxJSON!=null){
                    loadInboxSuccess = true;

                    JSONArray inboxArray = new JSONArray(inboxJSON.getString("inbox"));
                    String ID,name,reason,senderID,patientName, status;

                    Log.i("inboxJSON", String.valueOf(inboxArray));

                    inboxIDs = new ArrayList<>();
                    inboxName = new ArrayList<>();
                    inboxReason = new ArrayList<>();
                    inboxSenderID = new ArrayList<>();
                    inboxItemsList = new ArrayList<>();

                    for(int i=0; i < inboxArray.length();i++) {
                        JSONObject tempJSON = new JSONObject();
                        tempJSON = inboxArray.getJSONObject(i);
                        ID = tempJSON.getString("id");
                        name = tempJSON.getString("name");
                        reason = tempJSON.getString("reason");
                        senderID = tempJSON.getString("sender");
                        patientName = tempJSON.getString("patName");
                        status = data.getStatusFromFlag(tempJSON.getInt("status"));
                        inboxIDs.add(i, ID);
                        inboxName.add(i, name);
                        inboxSenderID.add(i, senderID);
                        inboxReason.add(i, reason);

                        //TODO change the arguments below
                        inboxItemsList.add(i, new ReferralItem(ID,"",name,patientName,"",reason,status,new ArrayList<messages.Message>(),""));
                    }

                }
                else
                    loadInboxSuccess = false;
            } catch (JSONException e) {
                e.printStackTrace();
                loadInboxSuccess = false;
            }

            return null;
        }



        @Override
        protected void onPostExecute(String res){
            super.onPostExecute(res);
            if(loadInboxProgress!=null&&loadInboxProgress.isShowing()) {
                loadInboxProgress.hide();
                loadInboxProgress.cancel();
            }
            if(loadInboxSuccess){
                Message msg = new Message();
                msg.arg1=0;
                ReferralsFragment.mHandler.sendMessage(msg);
            }
        }

    }

    public static class LoadSent extends AsyncTask<String,Void,String>{

        String userId, response;
        int start, end;
        boolean loadSentSuccess = false;

        @Override
        protected void onPreExecute(){
            loadSentProgress.setMessage("Loading Sent...");
            loadSentProgress.show();
            Log.i("Inside","LoadSent");
        }

        @Override
        protected String doInBackground(String... params) {

            userId = params[0];
            start = Integer.parseInt(params[1]);
            end = Integer.parseInt(params[2]);

            Log.i("userId",userId);

            List<NameValuePair> paramsList = new ArrayList<>();
            paramsList.add(new BasicNameValuePair("sender",userId));

            ServiceHandler requestMaker = new ServiceHandler();

            response = requestMaker.makeServiceCall(data.urlSent, ServiceHandler.POST, paramsList);

            try {
                JSONObject inboxJSON = new JSONObject(response);

                if(inboxJSON!=null){
                    loadSentSuccess = true;

                    JSONArray sentArray = new JSONArray(inboxJSON.getString("sent"));
                    String ID,name,status,receiverID,patientName,reason;

                    Log.i("sentArray", String.valueOf(sentArray));
                    sentIDs = new ArrayList<>();
                    sentName = new ArrayList<>();
                    sentStatus = new ArrayList<>();
                    sentReceiverIDs = new ArrayList<>();
                    sentItemsList = new ArrayList<>();

                    for(int i=0; i < sentArray.length();i++) {
                        JSONObject tempJSON = new JSONObject();
                        tempJSON = sentArray.getJSONObject(i);
                        ID = tempJSON.getString("id");
                        name = tempJSON.getString("name");
                        patientName = tempJSON.getString("patName");
                        status = data.getStatusFromFlag(tempJSON.getInt("status"));
                        receiverID = tempJSON.getString("receiver");
                        reason = tempJSON.getString("reason");
                        sentIDs.add(i, ID);
                        sentName.add(i, name);
                        sentReceiverIDs.add(i, receiverID);
                        sentStatus.add(i, status);

                        //TODO change the arguments below
                        sentItemsList.add(i, new ReferralItem(ID, "",name,patientName,"",reason,status,new ArrayList<messages.Message>(),""));
                    }

                }
                else
                    loadSentSuccess = false;
            } catch (JSONException e) {
                e.printStackTrace();
                loadSentSuccess = false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(String res){
            super.onPostExecute(res);
            if(loadSentProgress!=null&&loadSentProgress.isShowing()){
                loadSentProgress.hide();
                loadSentProgress.cancel();
            }
            if(loadSentSuccess){
                Message msg = new Message();
                msg.arg1=1;
                ReferralsFragment.mHandler.sendMessage(msg);
            }
        }

    }

    public static class GetReports extends AsyncTask<String,Void,String>{

        String userId;

        @Override
        protected String doInBackground(String... params) {

            userId = params[0];

            List<NameValuePair> paramsList = new ArrayList<>();
            paramsList.add(new BasicNameValuePair("doc",userId));

            ServiceHandler requestMaker = new ServiceHandler();

            String response = requestMaker.makeServiceCall(data.urlReports, ServiceHandler.POST, paramsList);

            try {
                JSONObject reportsJSOn = new JSONObject(response);

                if(reportsJSOn!=null){
                    JSONArray reportsArray = new JSONArray(reportsJSOn.getString("reports"));

                    reportsName = new ArrayList<>();
                    reportsReceivedApproved = new ArrayList<>();
                    reportsReceivedDeclined = new ArrayList<>();
                    reportsSentDeclined = new ArrayList<>();
                    reportsSentApproved = new ArrayList<>();
                    reportsSpeciality = new ArrayList<>();

                    for(int i=0; i < reportsArray.length();i++){
                        JSONObject tempJSON = new JSONObject();
                        tempJSON = reportsArray.getJSONObject(i);

                        String name, speciality;
                        Integer rec_app,sent_app,rec_dec,sent_dec;

                        name = tempJSON.getString("name");
                        speciality = tempJSON.getString("speciality");
                        rec_app = tempJSON.getInt("rec_approved");
                        rec_dec = tempJSON.getInt("rec_declined");
                        sent_app = tempJSON.getInt("sent_approved");
                        sent_dec = tempJSON.getInt("sent_declined");

                        reportsName.add(i, name);
                        reportsSpeciality.add(i, speciality);
                        reportsSentApproved.add(i, sent_app);
                        reportsSentDeclined.add(i, sent_dec);
                        reportsReceivedDeclined.add(i, rec_dec);
                        reportsReceivedApproved.add(i, rec_app);

                        ReportItem tempReport = new ReportItem(name,sent_app,sent_dec,rec_app,rec_dec);
                        reportItemArrayList.add(i, tempReport);
                    }

                    ReportFragment.mHandler.sendEmptyMessage(1);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class SendReferral extends AsyncTask<String,Void,String>{

        private String senderId, receiverId, patientName, patientMobile, reason, message;

        @Override
        protected String doInBackground(String... params) {

            senderId = params[0];
            receiverId = params[1];
            patientName = params[2];
            patientMobile = params[3];
            reason = params[4];
            message = params[5];

            Log.i("receiverId",receiverId);
            Log.i("senderId",senderId);

            List<NameValuePair> paramsList = new ArrayList<>();

            paramsList.add(new BasicNameValuePair("sender",senderId));
            paramsList.add(new BasicNameValuePair("receiver",receiverId));
            paramsList.add(new BasicNameValuePair("name",patientName));
            paramsList.add(new BasicNameValuePair("mbl",patientMobile));
            paramsList.add(new BasicNameValuePair("reason",reason));
            paramsList.add(new BasicNameValuePair("message",message));

            ServiceHandler requestMaker = new ServiceHandler();

            String response = requestMaker.makeServiceCall(data.urlSendReferral, ServiceHandler.POST, paramsList);

            try {
                JSONObject sendReferralJSON = new JSONObject(response);
                Log.d("sendReferralJSON",String.valueOf(sendReferralJSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}

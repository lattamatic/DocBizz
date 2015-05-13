package com.example.docbizz;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import contacts.ContactItem;
import contacts.ContactRecyclerViewAdapter;
import navigationDrawer.NavDrawerItem;
import navigationDrawer.NavDrawerListAdapter;
import reports.ReportItem;
import reports.ReportRecyclerViewAdapter;
import util.InfiniteRecyclerViewOnScrollListener;
import util.ServiceHandler;
import util.data;


public class MainActivity extends ActionBarActivity {


    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public static ArrayList<String> contactsIDs, contactsName, contactsEmail, contactsPhone, contactsSpeciality, contactsCity, contactsHospital;
    public static ArrayList<ContactItem> contactItemArrayList;
    public static ArrayList<ReportItem> reportItemArrayList;
    public static ArrayList<String> reportsName, reportsSpeciality;
    public static ArrayList<Integer> reportsSentApproved, reportsSentDeclined, reportsReceivedApproved, reportsReceivedDeclined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    private void selectItem(int position) {

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Fragment f = fragmentManager.findFragmentById(R.id.frame_container);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(0,0,0);
            getSupportFragmentManager().findFragmentById(R.id.frame_container)
                    .getView()
                    .setLayoutParams(lp);

        switch (position) {
            case 0 :
                fragmentTransaction.replace(R.id.frame_container, new SendReferralsFragment());
                break;

            case 1 :
                fragmentTransaction.replace(R.id.frame_container, new ReferralsFragment());
                break;

            case 2 :
                fragmentTransaction.replace(R.id.frame_container, new ReportFragment());
                break;

            case 3 :
                fragmentTransaction.replace(R.id.frame_container, new ContactsFragment());
                break;

            case 4 :
                fragmentTransaction.replace(R.id.frame_container, new HelpFragment());
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SendReferralsFragment extends Fragment {

        public SendReferralsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sendreferrals, container, false);

            //TODO : Change the arguments

            //TODO : Get the text from the edittexts and call this request on the click of the submit button.. these should be the arguments

            new SendReferral().execute("","","","","","");

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
            return rootView;
        }
    }
    public static class ReportFragment extends Fragment {

        public ReportFragment() {
        }

        public void loadMoreItems(int rLimit, int rOffset) {
            rOffset = rOffset + rLimit;
            //TODO : ping to the same URL with different offset and limit
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

            RecyclerView.OnScrollListener onScrollListener = new InfiniteRecyclerViewOnScrollListener(layoutManager) {
                @Override
                public void onLoadMore() {
                    loadMoreItems(rLimit, rOffset);
                }
            };
            recyclerView.setOnScrollListener(onScrollListener);

            reportItemArrayList = new ArrayList<>();

            SharedPreferences sharedPreferences = rootView.getContext().getSharedPreferences("DocBizz", MODE_PRIVATE);
            String id = sharedPreferences.getString("id", "");
            new GetReports().execute(id, rLimit + "", rOffset + "");

            ReportRecyclerViewAdapter adapter = new ReportRecyclerViewAdapter(reportItemArrayList, rootView.getContext());
            recyclerView.setAdapter(adapter);

            return rootView;
        }
    }

    public static class ContactsFragment extends Fragment {

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
            new GetContactsList().execute(id);

            ContactRecyclerViewAdapter adapter = new ContactRecyclerViewAdapter(contactItemArrayList, rootView.getContext());
            recyclerView.setAdapter(adapter);

            return rootView;
        }
    }

    public static class ReferralsFragment extends Fragment {

        public ReferralsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_referrals, container, false);

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

                        ContactItem tempContact = new ContactItem("",contactsName.get(i),contactsPhone.get(i),contactsEmail.get(i),contactsHospital.get(i),contactsCity.get(i),contactsSpeciality.get(i));
                        //TODO change this to add imageURL

                        contactItemArrayList.add(i, tempContact);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class GetReports extends AsyncTask<String,Void,String>{

        String userId, limit, offset;

        @Override
        protected String doInBackground(String... params) {

            userId = params[0];
            limit = params[1];
            offset = params[2];

            List<NameValuePair> paramsList = new ArrayList<>();
            paramsList.add(new BasicNameValuePair("doc",userId));
            paramsList.add(new BasicNameValuePair("limit", limit));
            paramsList.add(new BasicNameValuePair("offset", offset));

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

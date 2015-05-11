package com.example.docbizz;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import contacts.ContactItem;
import contacts.ContactRecyclerViewAdapter;
import reports.ReportItem;
import reports.ReportRecyclerViewAdapter;
import util.InfiniteRecyclerViewOnScrollListener;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ReferralsFragment())
                    .commit();
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

            //TODO : Make a backend request for sending the referral when Send button is clicked..

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

            ArrayList<ReportItem> reportItemArrayList = new ArrayList<>();

            //TODO : Fill the data to be displayed in this arraylist

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

            ArrayList<ContactItem> contactItemArrayList = new ArrayList<>();

            //TODO : Fill the data to be displayed in this arraylist

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
}

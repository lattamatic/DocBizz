package com.example.docbizz;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import contacts.ContactItem;
import contacts.ContactRecyclerViewAdapter;
import navigationDrawer.NavDrawerItem;
import navigationDrawer.NavDrawerListAdapter;
import reports.ReportItem;
import reports.ReportRecyclerViewAdapter;
import util.InfiniteRecyclerViewOnScrollListener;
import util.data;


public class MainActivity extends ActionBarActivity {


    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public static FragmentManager SupportFragmentManager;

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

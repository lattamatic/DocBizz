package com.example.docbizz;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import util.ImageSlidePagerFragment;
import util.ServiceHandler;
import util.ViewPagerTransformer;
import util.data;


public class Home extends FragmentActivity {

    Handler mHandler;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    int i=0;
    private static final int NUM_PAGES = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnRegister = (Button) findViewById(R.id.btnHomeRegister);
        Button btnLogin = (Button) findViewById(R.id.btnHomeLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1 == 0) {
                    Toast.makeText(getApplicationContext(), "Incorrect Email/Password", Toast.LENGTH_SHORT).show();
                }
                else if(msg.arg1 == 1) {
                    Toast.makeText(getApplicationContext(), "Sorry. An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = Home.this;
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                final View loginView = layoutInflater.inflate(R.layout.dialog_login, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(loginView);
                alertDialogBuilder.setCancelable(true);

                Button btnLoginD = (Button) loginView.findViewById(R.id.btnLogin);
                btnLoginD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ServiceHandler requestMaker = new ServiceHandler();

                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                        final EditText editTextEmail = (EditText) loginView.findViewById(R.id.editText1);
                        final EditText editTextPassword = (EditText) loginView.findViewById(R.id.editText2);

                        new LoginTask().execute(editTextEmail.getText().toString(),editTextPassword.getText().toString());

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        final Resources reso = this.getResources();

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ViewPagerTransformer());
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());

        mPager.setAdapter(mPagerAdapter);

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                ImageView slide1=(ImageView)findViewById(R.id.slide1);
                ImageView slide2=(ImageView)findViewById(R.id.slide2);
                ImageView slide3=(ImageView)findViewById(R.id.slide3);
                ImageView slide4=(ImageView)findViewById(R.id.slide4);
                ImageView slide5=(ImageView)findViewById(R.id.slide5);
                //slide1.setAlpha(0.0f);
                slide1.setBackground((GradientDrawable) reso.getDrawable(R.drawable.image_slider));
                slide2.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                slide3.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                slide4.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                slide5.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                i=position;
                switch (position){

                    case 0:
                        slide1.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider_current));

                        break;
                    case 1:
                        slide2.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider_current));

                        break;
                    case 2:
                        slide3.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider_current));
                        break;
                    case 3:
                        slide4.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider_current));
                        break;
                    case 4:
                        slide5.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider_current));
                        break;
                }
                invalidateOptionsMenu();
            }
        });

        change();

    }

    public void change(){
        new CountDownTimer(3000, 3000) {

            public void onTick(long millisUntilFinished) {
                slideToImage(i);
            }

            public void onFinish() {
                ++i;
                if(i==5)
                    i=0;
                change();


            }
        }.start();
    }

    public void slideToImage(int position){
        mPager.setCurrentItem(position);
    }

    public void slide(View v) {
        final Resources reso = this.getResources();

        switch (v.getId()) {
            case R.id.slide1:
                mPager.setCurrentItem(0);
                i=0;
                /*slide1.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider_current));
                slide2.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                slide3.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                slide4.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));*/
                break;
            case R.id.slide2:
                mPager.setCurrentItem(1);
                i=1;
               /* slide2.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider_current));
                slide1.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                slide3.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                slide4.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));*/
                break;
            case R.id.slide3:
                mPager.setCurrentItem(2);
                i=2;
                /*slide3.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider_current));
                slide2.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                slide1.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                slide4.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));*/
                break;
            case R.id.slide4:
                mPager.setCurrentItem(3);
                i=3;
               /* slide4.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider_current));
                slide2.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                slide3.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));
                slide1.setBackground((GradientDrawable)reso.getDrawable(R.drawable.image_slider));*/
                break;
            case R.id.slide5:
                mPager.setCurrentItem(4);
                i=4;
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

    public class LoginTask extends AsyncTask<String,Void,String> {

        String email, password;

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler requestMakerLogin = new ServiceHandler();

            email = params[0];
            password = params[1];

            List<NameValuePair> paramsLogin = new ArrayList<NameValuePair>();
            paramsLogin.add(new BasicNameValuePair("email", email));
            paramsLogin.add(new BasicNameValuePair("password", password));

            String response = requestMakerLogin.makeServiceCall(data.urlLogin, ServiceHandler.POST, paramsLogin);
            try {
                JSONObject responseObject = new JSONObject(response);

                if (responseObject.getBoolean("result")) {
                    SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("DocBizz", MODE_PRIVATE).edit();
                    sharedPreferencesEditor.putString("user", responseObject.getJSONObject("data").toString());
                    sharedPreferencesEditor.commit();

                    Intent intent = new Intent(Home.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Message msg = new Message();
                    msg.arg1 = 0;
                    mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.arg1 = 1;
                mHandler.sendMessage(msg);
            }
            return null;
        }
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImageSlidePagerFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

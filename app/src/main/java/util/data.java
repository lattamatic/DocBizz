package util;

/**
 * Created by aravind on 10/5/15.
 */

import com.example.docbizz.R;

import java.util.Arrays;
import java.util.List;

import navigationDrawer.NavDrawerItem;

public class data {
    public static NavDrawerItem[] navtitles={
            new NavDrawerItem("Send Referral", R.drawable.ic_launcher),
            new NavDrawerItem("Referrals",R.drawable.ic_launcher),
            new NavDrawerItem("Reports",R.drawable.ic_launcher),
            new NavDrawerItem("Contacts",R.drawable.ic_launcher),
            new NavDrawerItem("Help",R.drawable.ic_launcher),
            new NavDrawerItem("Logout",R.drawable.ic_launcher)
    };

    public static List<NavDrawerItem> getNavDrawerItems(){
        return Arrays.asList(navtitles);
    }

    public static String urlDomain = "http://www.docbizz.org/";
    public static String urlRegister = urlDomain + "register.php";
    public static String urlLogin = urlDomain + "login.php";
    public static String urlInbox = urlDomain + "inbox.php";
    public static String urlSent = urlDomain + "sent.php";
    public static String urlContacts = urlDomain + "contacts.php";
    public static String urlReports = urlDomain + "reports.php";
    public static String urlSendReferral = urlDomain + "refer.php";
    public static String urlApproveDeclineReferral = urlDomain + "appdec.php";

    public static String SENDER_ID = ""; //TODO : project ID from Google Console

}


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
            new NavDrawerItem("Send Referral", R.drawable.calendar),
            new NavDrawerItem("Referrals",R.drawable.registered),
            new NavDrawerItem("Reports",R.drawable.reports),
            new NavDrawerItem("Contacts",R.drawable.ic_launcher),
            new NavDrawerItem("Invite a friend",R.drawable.inviteafriend),
            new NavDrawerItem("Help",R.drawable.help),
            new NavDrawerItem("Logout",R.drawable.logout)
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
    public static String urlReferralDetails = urlDomain + "referal_details.php";

    public static String SENDER_ID = "915552494810"; //TODO : project ID from Google Console


    public static String getStatusFromFlag(int status) {
        switch(status) {
            case 0 :
                return "Pending";
            case 1 :
                return "Approved";
            case 2 :
                return "Declined";
            default :
                return "";
        }
    }

}


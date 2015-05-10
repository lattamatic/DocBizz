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
            new NavDrawerItem("Logout",R.drawable.ic_launcher)
    };

    public static List<NavDrawerItem> getNavDrawerItems(){
        return Arrays.asList(navtitles);
    }
}


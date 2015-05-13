package referrals;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.docbizz.MainActivity;
import com.example.docbizz.R;

import util.InfiniteRecyclerViewOnScrollListener;

/**
 * Created by Suganprabu on 13/5/15.
 */
public class InboxReferralItem extends Fragment {

    public static Fragment newInstance(Context context) {
        InboxReferralItem f = new InboxReferralItem();

        return f;
    }

    public void loadMoreItems(int rLimit, int rOffset) {
        InboxReferralItem.rOffset = rOffset + rLimit;
        //TODO : ping to the same URL with different offset and limit
    }

    final int rLimit = 10;
    static int rOffset = 0;
    public static RecyclerView recyclerViewInbox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_referrals_inbox, null);

        recyclerViewInbox = (RecyclerView) root.findViewById(R.id.recyclerViewReferralsInbox);
        final LinearLayoutManager layoutManagerInbox = new LinearLayoutManager(root.getContext());

        layoutManagerInbox.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewInbox.setLayoutManager(layoutManagerInbox);
        recyclerViewInbox.setItemAnimator(new DefaultItemAnimator());
        recyclerViewInbox.setHasFixedSize(true);

        MainActivity.inboxAdapter = new ReferralRecyclerViewAdapter(MainActivity.inboxItemsList,root.getContext());

        RecyclerView.OnScrollListener onScrollListenerInbox = new InfiniteRecyclerViewOnScrollListener(layoutManagerInbox) {
            @Override
            public void onLoadMore() {
                loadMoreItems(rLimit, rOffset);
            }
        };
        recyclerViewInbox.setOnScrollListener(onScrollListenerInbox);

        recyclerViewInbox.setAdapter(MainActivity.inboxAdapter);

        return root;
    }
}

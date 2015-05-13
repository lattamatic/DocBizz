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
public class SentReferralItem extends Fragment {

    public static Fragment newInstance(Context context) {
        SentReferralItem f = new SentReferralItem();

        return f;
    }

    public void loadMoreItems(int rLimit, int rOffset) {
        SentReferralItem.rOffset = rOffset + rLimit;
        //TODO : ping to the same URL with different offset and limit
    }

    final int rLimit = 10;
    static int rOffset = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_referrals_sent, null);

        MainActivity.sentAdapter = new ReferralRecyclerViewAdapter(MainActivity.sentItemsList,root.getContext());

        RecyclerView recyclerViewSent = (RecyclerView) root.findViewById(R.id.recyclerViewReferralsSent);
        final LinearLayoutManager layoutManagerSent = new LinearLayoutManager(root.getContext());

        layoutManagerSent.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSent.setLayoutManager(layoutManagerSent);
        recyclerViewSent.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSent.setHasFixedSize(true);

        RecyclerView.OnScrollListener onScrollListenerSent = new InfiniteRecyclerViewOnScrollListener(layoutManagerSent) {
            @Override
            public void onLoadMore() {
                loadMoreItems(rLimit, rOffset);
            }
        };
        recyclerViewSent.setOnScrollListener(onScrollListenerSent);

        recyclerViewSent.setAdapter(MainActivity.sentAdapter);

        MainActivity.sentHandler = new Handler(){
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1) {
                    MainActivity.sentAdapter.notifyDataSetChanged();
                    MainActivity.sentAdapter.notifyItemRangeInserted(0,msg.arg2-1);
                    MainActivity.sentAdapter.notifyItemRangeChanged(0,msg.arg2-1);
                    Log.i("sentItem",MainActivity.sentItemsList.get(0).doctorName);
                    Log.i("sentAdapterItemCount", String.valueOf(MainActivity.sentAdapter.getItemCount()));
                    Log.i("Inside", "sentHandler");
                }

            }
        };

        return root;
    }
}
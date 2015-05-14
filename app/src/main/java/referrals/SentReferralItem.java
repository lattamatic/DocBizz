package referrals;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.docbizz.MainActivity;
import com.example.docbizz.R;

import util.InfiniteRecyclerViewOnScrollListener;

/**u 
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
    public static RecyclerView recyclerViewSent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_referrals_sent, null);

        MainActivity.sentAdapter = new SentReferralRecyclerViewAdapter(MainActivity.sentItemsList,root.getContext());

        recyclerViewSent = (RecyclerView) root.findViewById(R.id.recyclerViewReferralsSent);
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
        //recyclerViewSent.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        recyclerViewSent.setAdapter(MainActivity.sentAdapter);

        return root;
    }
}

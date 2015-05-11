package util;

/**
 * Created by aravind on 11/5/15.
 */


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class InfiniteRecyclerViewOnScrollListener extends
            RecyclerView.OnScrollListener {

        int lastVisibleItem, totalItemCount;
        private LinearLayoutManager mLinearLayoutManager;

        public InfiniteRecyclerViewOnScrollListener(
                LinearLayoutManager linearLayoutManager) {
            this.mLinearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            totalItemCount = mLinearLayoutManager.getItemCount();
            lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
            if (totalItemCount == lastVisibleItem + 1) onLoadMore();
        }
        public abstract void onLoadMore();
    }


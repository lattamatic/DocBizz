package referrals;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.docbizz.R;

import java.util.ArrayList;

/**
 * Created by aravind on 12/5/15.
 */

public class ReferralRecyclerViewAdapter extends RecyclerView.Adapter<ReferralRecyclerViewAdapter.ListItemViewHolder>{

    private ArrayList<ReferralItem> items;
    private Context context;

    public ReferralRecyclerViewAdapter(ArrayList<ReferralItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtDoctorName, txtSentApproved, txtSentDeclined, txtReceivedApproved, txtReceivedDeclined;
        public ReferralItem item;

        public ListItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.referral_recyclerview_element,
                        viewGroup,
                        false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder listItemViewHolder, int position) {
        listItemViewHolder.item = items.get(position);
        ReferralItem item = items.get(position);
    }

    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
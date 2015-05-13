package reports;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.docbizz.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by aravind on 10/5/15.
 */

public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportRecyclerViewAdapter.ListItemViewHolder>{

    private ArrayList<ReportItem> items;
    private Context context;

    public ReportRecyclerViewAdapter(ArrayList<ReportItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtDoctorName, txtSentApproved, txtSentDeclined, txtReceivedApproved, txtReceivedDeclined;
        public ReportItem item;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            txtDoctorName = (TextView) itemView.findViewById(R.id.txtDoctorName);
            txtSentApproved = (TextView) itemView.findViewById(R.id.txtSentApproved);
            txtSentDeclined = (TextView) itemView.findViewById(R.id.txtSentDeclined);
            txtReceivedApproved = (TextView) itemView.findViewById(R.id.txtReceivedApproved);
            txtReceivedDeclined = (TextView) itemView.findViewById(R.id.txtReceivedDeclined);
        }
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_reports_recyclerview_element,
                        viewGroup,
                        false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder listItemViewHolder, int position) {
        listItemViewHolder.item = items.get(position);
        ReportItem item = items.get(position);
        listItemViewHolder.txtDoctorName.setText(item.doctorName);
        listItemViewHolder.txtReceivedDeclined.setText(item.receivedDeclined + "");
        listItemViewHolder.txtReceivedApproved.setText(item.receivedApproved + "");
        listItemViewHolder.txtSentDeclined.setText(item.sentDeclined + "");
        listItemViewHolder.txtSentApproved.setText(item.sentApproved + "");
    }

    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
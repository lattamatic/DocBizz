package referrals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.docbizz.IncomingReferralDetails;
import com.example.docbizz.R;
import com.example.docbizz.SentReferralDetails;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aravind on 12/5/15.
 */

public class IncomingReferralRecyclerViewAdapter extends RecyclerView.Adapter<IncomingReferralRecyclerViewAdapter.ListItemViewHolder>{

    private ArrayList<ReferralItem> items;
    private Context context;

    public IncomingReferralRecyclerViewAdapter(ArrayList<ReferralItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtDoctorName, txtReferralTime, txtReferralPatientName, txtReferralStatus,txtReferralPatientReason;
        CircleImageView imgDoctorPic;

        public ReferralItem item;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            imgDoctorPic = (CircleImageView) itemView.findViewById(R.id.circleImagedoctorPic);

            txtDoctorName = (TextView) itemView.findViewById(R.id.txtDoctorName);
            txtReferralPatientName = (TextView) itemView.findViewById(R.id.txtReferralPatientName);
            txtReferralStatus = (TextView) itemView.findViewById(R.id.txtReferralStatus);
            txtReferralTime = (TextView) itemView.findViewById(R.id.txtReferralTime);
            //txtReferralPatientReason = (TextView) itemView.findViewById(R.id.txtReferralPatientReason);
        }
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.referral_recyclerview_element,
                        viewGroup,
                        false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = InboxReferralItem.recyclerViewInbox.getChildPosition(v);
                Intent intent = new Intent(context, IncomingReferralDetails.class);
                intent.putExtra("referralID", items.get(position).id);
                intent.putExtra("index",position);
                context.startActivity(intent);
            }
        });

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder listItemViewHolder, int position) {
        listItemViewHolder.item = items.get(position);
        ReferralItem item = items.get(position);

        listItemViewHolder.txtDoctorName.setText(item.doctorName);
        listItemViewHolder.txtReferralTime.setText(item.referralDate);
        listItemViewHolder.txtReferralPatientName.setText(item.patientName);
        listItemViewHolder.txtReferralStatus.setText(item.status);
        //listItemViewHolder.txtReferralPatientReason.setText(item.patientReason);

        if(!item.doctorPicURL.equals("")) {
            //TODO : Set the image of the circle image view to the image at the URL..
        }

        if(item.status.equals("Declined")) {
            listItemViewHolder.txtReferralStatus.setTextColor(Color.parseColor("#FF0000"));
        }
        else if(item.status.equals("Approved")) {
            listItemViewHolder.txtReferralStatus.setTextColor(Color.parseColor("#00FF00"));
        }
        else if(item.status.equals("Pending")) {
            listItemViewHolder.txtReferralStatus.setTextColor(Color.parseColor("#000000"));
        }
    }

    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
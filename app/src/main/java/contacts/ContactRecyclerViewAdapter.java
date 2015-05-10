package contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.docbizz.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by aravind on 10/5/15.
 */

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ListItemViewHolder>{

    private ArrayList<ContactItem> items;
    private Context context;

    public ContactRecyclerViewAdapter(ArrayList<ContactItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtDoctorName, txtDoctorPhone, txtDoctorEmail, txtDoctorHospital, txtDoctorCity, txtDoctorSpecification;
        ImageView imgDoctorImage;

        public ContactItem item;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            txtDoctorName = (TextView) itemView.findViewById(R.id.txtDoctorName);
            txtDoctorCity = (TextView) itemView.findViewById(R.id.txtDoctorCity);
            txtDoctorEmail = (TextView) itemView.findViewById(R.id.txtDoctorEmail);
            txtDoctorHospital = (TextView) itemView.findViewById(R.id.txtDoctorHospital);
            txtDoctorSpecification = (TextView) itemView.findViewById(R.id.txtDoctorSpecification);
            txtDoctorPhone = (TextView) itemView.findViewById(R.id.txtDoctorPhone);

            imgDoctorImage = (ImageView) itemView.findViewById(R.id.imgDoctorImage);
        }
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.contacts_recyclerview_element,
                        viewGroup,
                        false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder listItemViewHolder, int position) {
        listItemViewHolder.item = items.get(position);
        ContactItem item = items.get(position);
        listItemViewHolder.txtDoctorName.setText(item.doctorName);
        listItemViewHolder.txtDoctorCity.setText(item.doctorCity);
        listItemViewHolder.txtDoctorPhone.setText(item.doctorPhone);
        listItemViewHolder.txtDoctorSpecification.setText(item.doctorSpecification);
        listItemViewHolder.txtDoctorHospital.setText(item.doctorHospital);
        listItemViewHolder.txtDoctorEmail.setText(item.doctorEmail);
    }

    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}
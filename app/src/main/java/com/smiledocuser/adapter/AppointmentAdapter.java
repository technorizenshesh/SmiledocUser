package com.smiledocuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiledocuser.R;
import com.smiledocuser.databinding.ItemAppointmentsBinding;
import com.smiledocuser.databinding.ItemBidsBinding;
import com.smiledocuser.model.BidsModel;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {
    Context context;
    ArrayList<BidsModel.Result> arrayList;

    public AppointmentAdapter(Context context, ArrayList<BidsModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_appointments,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(arrayList.get(position)!=null) {
            holder.binding.tvName.setText(arrayList.get(position).getDoctorDetial().getFirstName() + " " + arrayList.get(position).getDoctorDetial().getLastName());
            holder.binding.tvPrice.setText("$"+arrayList.get(position).getAmount());
            holder.binding.tvDateTime.setText(arrayList.get(position).getBookingDate() + " " + arrayList.get(position).getDateTime() );
            holder.binding.tvDesiss.setText(arrayList.get(position).getProblem());

            Glide.with(context).load(arrayList.get(position).getDoctorDetial().getImage())
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_user_circle_24)
                    .error(R.drawable.ic_user_circle_24)
                    .into(holder.binding.img1);

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemAppointmentsBinding binding;
        public MyViewHolder(@NonNull ItemAppointmentsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

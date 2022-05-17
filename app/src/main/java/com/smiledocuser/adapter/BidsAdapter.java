package com.smiledocuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiledocuser.R;
import com.smiledocuser.act.EditProfile;
import com.smiledocuser.databinding.ItemBidsBinding;
import com.smiledocuser.model.BidsModel;

import java.util.ArrayList;

public class BidsAdapter extends RecyclerView.Adapter<BidsAdapter.MyViewHolder> {
    Context context;
    ArrayList<BidsModel.Result>arrayList;

    public BidsAdapter(Context context, ArrayList<BidsModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBidsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_bids,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       if(arrayList.get(position)!=null) {
           holder.binding.tvName.setText(arrayList.get(position).getDoctorDetial().getFirstName() + " " + arrayList.get(position).getDoctorDetial().getLastName());
           holder.binding.tvPrice.setText("$"+arrayList.get(position).getAmount());
           holder.binding.tvDateTime.setText(arrayList.get(position).getBookingDate() + " " + arrayList.get(position).getDateTime() );
           holder.binding.tvStatus.setText(arrayList.get(position).getStatus());
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
        ItemBidsBinding binding;
        public MyViewHolder(@NonNull ItemBidsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

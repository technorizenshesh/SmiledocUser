package com.smiledocuser.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiledocuser.R;
import com.smiledocuser.databinding.AdapterDoctorBinding;
import com.smiledocuser.databinding.ItemDoctorBinding;
import com.smiledocuser.model.DoctoreListModal;
import com.smiledocuser.model.DoctorsModel;

import java.util.ArrayList;

public class ShowDrAdapter extends RecyclerView.Adapter<ShowDrAdapter.MyViewHolder> {
    Context context;
    ArrayList<DoctorsModel.Result> arrayList;

    public ShowDrAdapter(Context context, ArrayList<DoctorsModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public ShowDrAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDoctorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_doctor,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowDrAdapter.MyViewHolder holder, int position) {
        holder.binding.nameParticipates.setText(arrayList.get(position).getFirstName()+" "+arrayList.get(position).getLastName());
        holder.binding.specility.setText(arrayList.get(position).getCategoryName());
        holder.binding.price.setText(" $ " + arrayList.get(position).getFees());
        if (arrayList.get(position).getImage() != null) {
            Glide.with(context)
                    .load(arrayList.get(position).getImage())
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.ic_user_circle_24)
                    .error(R.drawable.ic_user_circle_24)
                    .into(holder.binding.img1);
        } else {
            holder.binding.img1.setImageResource(R.drawable.ic_user_circle_24);
        }


        holder.binding.addTv.setOnClickListener (v ->{
            Bundle bundle = new Bundle();
            bundle.putString("id", arrayList.get(position).getId());
            Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_bookingFragment, bundle) ;
                });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemDoctorBinding binding;
        public MyViewHolder(@NonNull ItemDoctorBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
package com.smiledocuser.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiledocuser.R;
import com.smiledocuser.databinding.AdapterBookingBinding;
import com.smiledocuser.databinding.AdapterDoctorBinding;
import com.smiledocuser.listener.TimeSlotClickListener;
import com.smiledocuser.model.DoctoreListModal;
import com.smiledocuser.model.TimeSlote;

import java.util.ArrayList;

public class AdapterDoctor extends RecyclerView.Adapter<AdapterDoctor.ViewHolder> {
    Context context;
    ArrayList<DoctoreListModal> arrayList;

    public AdapterDoctor(Context context, ArrayList<DoctoreListModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterDoctorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_doctor, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binding.nameParticipates.setText(arrayList.get(position).getFirst_name()+" "+arrayList.get(position).getLast_name());
        holder.binding.specility.setText(arrayList.get(position).getCategory_name());
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
        Bundle bundle = new Bundle();
        bundle.putString("id", arrayList.get(position).getId());

        holder.binding.addTv.setOnClickListener (v ->
                Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_bookingFragment, bundle)
        );

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterDoctorBinding binding;
        public ViewHolder(@NonNull AdapterDoctorBinding itemView) {
            super(itemView.getRoot());
            binding =itemView;



        }
    }
}

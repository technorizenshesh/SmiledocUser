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
import com.smiledocuser.databinding.AdapterSearchDoctorBinding;
import com.smiledocuser.fragment.SearchDoctorFragment;
import com.smiledocuser.model.DoctoreListModal;

import java.util.ArrayList;

public class AdapterSearchDoctor extends RecyclerView.Adapter<AdapterSearchDoctor.ViewHolder> {
    Context context;
    ArrayList<DoctoreListModal> arrayList;

    public AdapterSearchDoctor(Context context, ArrayList<DoctoreListModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterSearchDoctorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_search_doctor, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.drName.setText(arrayList.get(position).getFirst_name()+" "+arrayList.get(position).getLast_name());
        Bundle bundle = new Bundle();
        bundle.putString("id", arrayList.get(position).getId());

        holder.binding.drLinear.setOnClickListener (v ->
                Navigation.findNavController(v).navigate(R.id.action_searchByDr_to_bookingFragment, bundle)
        );

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterSearchDoctorBinding binding;
        public ViewHolder(@NonNull AdapterSearchDoctorBinding itemView) {
            super(itemView.getRoot());
            binding =itemView;



        }
    }

    public void filterList(ArrayList<DoctoreListModal> filterlist) {

        arrayList = filterlist;
        if(filterlist.size()==0) SearchDoctorFragment.tvNotFound.setVisibility(View.VISIBLE);
        else SearchDoctorFragment.tvNotFound.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

}

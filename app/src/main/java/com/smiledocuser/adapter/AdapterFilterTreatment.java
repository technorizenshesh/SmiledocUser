package com.smiledocuser.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smiledocuser.FilterClickListener;
import com.smiledocuser.R;
import com.smiledocuser.databinding.AdapterDoctorBinding;
import com.smiledocuser.databinding.AdapterFilterTreatmentBinding;
import com.smiledocuser.listener.OnPosListener;
import com.smiledocuser.model.DoctoreListModal;
import com.smiledocuser.model.GetFilterCategoryModal;
import com.smiledocuser.model.GetFilterConsaltantModal;

import java.util.ArrayList;

public class AdapterFilterTreatment extends RecyclerView.Adapter<AdapterFilterTreatment.ViewHolder> {
    Context context;
    ArrayList<GetFilterCategoryModal> arrayList;
    OnPosListener listener;
    int lastSelectedPosition = -1;
    RadioButton lastCheckedRB = null;

    public AdapterFilterTreatment(Context context, ArrayList<GetFilterCategoryModal> arrayList,OnPosListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterFilterTreatmentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_filter_treatment, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.categoryName.setText(arrayList.get(position).getDrname());
        holder.binding.categoryName.setChecked(Boolean.parseBoolean(arrayList.get(position).getSet_filter()));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterFilterTreatmentBinding binding;
        public ViewHolder(@NonNull AdapterFilterTreatmentBinding itemView) {
            super(itemView.getRoot());
            binding =itemView;

            binding.categoryName.setOnClickListener(v -> {
              /*  if (lastCheckedRB != null) {
                    binding.categoryName.setChecked(false);
                }
                lastCheckedRB = binding.categoryName;*/
                if(arrayList.get(getAdapterPosition()).getSet_filter().equals("true"))  arrayList.get(getAdapterPosition()).setSet_filter("false");
                else arrayList.get(getAdapterPosition()).setSet_filter("true");
                listener.onPos(getAdapterPosition(),arrayList.get(getAdapterPosition()).getSet_filter());
                notifyItemChanged(getAdapterPosition());
            });

        }
    }
}
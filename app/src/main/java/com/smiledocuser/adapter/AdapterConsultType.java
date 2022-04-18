package com.smiledocuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smiledocuser.FilterClickListener;
import com.smiledocuser.R;
import com.smiledocuser.databinding.AdapterBookingBinding;
import com.smiledocuser.databinding.AdapterFilterTreatmentBinding;
import com.smiledocuser.listener.TimeSlotClickListener;
import com.smiledocuser.model.GetFilterConsaltantModal;
import com.smiledocuser.model.TimeSlote;

import java.util.ArrayList;

public class AdapterConsultType extends RecyclerView.Adapter<AdapterConsultType.ViewHolder> {
    Context context;
    ArrayList<GetFilterConsaltantModal> arrayList;
    FilterClickListener listener;
    int lastSelectedPosition = -1;
    RadioButton lastCheckedRB = null;

    public AdapterConsultType(Context context, ArrayList<GetFilterConsaltantModal> arrayList, FilterClickListener listener) {
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
        holder.binding.categoryName.setChecked(lastSelectedPosition == position);


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
                listener.consultTypeClick(arrayList.get(getAdapterPosition()).getDrId());
                if (lastCheckedRB != null) {
                    binding.categoryName.setChecked(false);
                }
                lastCheckedRB = binding.categoryName;
                notifyDataSetChanged();
            });


        }
    }
}
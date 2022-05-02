package com.smiledocuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smiledocuser.R;
import com.smiledocuser.databinding.AdapterBookingBinding;
import com.smiledocuser.listener.TimeSlotClickListener;
import com.smiledocuser.model.TimeSlote;

import java.util.ArrayList;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.ViewHolder> {
    Context context;
    ArrayList<TimeSlote> arrayList;
    TimeSlotClickListener listener;

    public AdapterBooking(Context context, ArrayList<TimeSlote> arrayList, TimeSlotClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      AdapterBookingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.adapter_booking, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.sloteI.setText(arrayList.get(position).getStart() + " TO " + arrayList.get(position).getEnd_name());
       if(arrayList.get(position).getStatus().equals("true")){
           holder.binding.sloteI.setBackground(context.getDrawable(R.drawable.border_edit_search));

       }
       else {
           if(arrayList.get(position).isChecked()){
               holder.binding.sloteI.setBackground(context.getDrawable(R.drawable.accept_border));

           }
           else {
               holder.binding.sloteI.setBackground(context.getDrawable(R.drawable.btn_baground));

           }
       }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterBookingBinding binding;
        public ViewHolder(@NonNull AdapterBookingBinding itemView) {
            super(itemView.getRoot());
            binding =itemView;

            binding.bookingAppoinment.setOnClickListener(v -> {
                for(int i = 0;i<arrayList.size();i++) {
                    arrayList.get(i).setChecked(false);
                }
                arrayList.get(getAdapterPosition()).setChecked(true);
                listener.onClick(arrayList.get(getAdapterPosition()));
                notifyDataSetChanged();
            });
        }
    }
}
